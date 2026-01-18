package com.anitour.control;

import com.anitour.dao.IBookingRepository;
import com.anitour.dao.IPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class BookingSystemTest {

    @Mock
    private IBookingRepository mockRepo;
    @Mock
    private IPaymentGateway mockGateway;

    private BookingControl controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BookingControl(mockRepo, mockGateway);
    }

    // TC-BOOK-01 (Acquisto con successo)
    @Test
    @DisplayName("TC-BOOK-01: Acquisto con successo (Happy Path)")
    void testAcquistoConSuccesso() throws Exception {
        Cart cart = new Cart();
        cart.addTour(1, "Persona 5 Tour", 100.0);
        PaymentDTO payment = new PaymentDTO("4000-0000-0000-0000");

        when(mockRepo.checkAvailability(anyInt(), anyInt())).thenReturn(true);

        when(mockGateway.processPayment(anyDouble(), any())).thenReturn(true);
        when(mockRepo.save(any(Booking.class))).thenReturn(500);

        Booking result = controller.processCheckout(cart, payment);

        assertEquals(500, result.getId());
        assertEquals("CONFIRMED", result.getStatus());
    }

    // TC-BOOK-02 (Sold Out)
    @Test
    @DisplayName("TC-BOOK-02: Fallimento per posti esauriti (Concurrency)")
    void testAcquistoConSoldOut() {
        Cart cart = new Cart();
        cart.addTour(2, "Bloodborne Tour", 150.0);
        PaymentDTO payment = new PaymentDTO("4000-0000-0000-0000");

        // Stavolta qui vogliamo false
        when(mockRepo.checkAvailability(anyInt(), anyInt())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            controller.processCheckout(cart, payment);
        });

        assertTrue(exception.getMessage().contains("SoldOutException"));
        verify(mockGateway, never()).processPayment(anyDouble(), any());
    }

    // TC-BOOK-03 (Pagamento Rifiutato)
    @Test
    @DisplayName("TC-BOOK-03: Pagamento Rifiutato")
    void testPagamentoRifiutato() {
        Cart cart = new Cart();
        cart.addTour(1, "Tour Costoso", 2000.0);
        PaymentDTO payment = new PaymentDTO("4000-9999-9999-9999");

        when(mockRepo.checkAvailability(anyInt(), anyInt())).thenReturn(true);

        when(mockGateway.processPayment(anyDouble(), any())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            controller.processCheckout(cart, payment);
        });

        assertEquals("Pagamento rifiutato dalla banca", exception.getMessage());
    }

    // TC-BOOK-04 (Carrello Vuoto)
    @Test
    @DisplayName("TC-BOOK-04: Carrello Vuoto")
    void testAcquistoConCarrelloVuoto() {
        Cart emptyCart = new Cart();
        PaymentDTO payment = new PaymentDTO("1234-5678");

        assertThrows(IllegalArgumentException.class, () -> {
            controller.processCheckout(emptyCart, payment);
        });
    }
}