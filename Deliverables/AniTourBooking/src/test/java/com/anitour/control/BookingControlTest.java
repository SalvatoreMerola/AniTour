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

class BookingControlTest {

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

    @Test
    @DisplayName("Test ProcessCheckout: Successo (ODD 3.1.1)")
    void testProcessCheckout_Success() throws Exception {
        // 1. SETUP
        Cart cart = new Cart();
        cart.addTour(1, "One Piece", 2050.0);
        PaymentDTO payment = new PaymentDTO("1234-5678-9012-3456");

        // Diciamo al Mock che c'è disponibilità, altrimenti ritorna false e lancia eccezione
        when(mockRepo.checkAvailability(anyInt(), anyInt())).thenReturn(true);

        when(mockGateway.processPayment(anyDouble(), any(PaymentDTO.class))).thenReturn(true);
        when(mockRepo.save(any(Booking.class))).thenReturn(100);

        // 2. ACTION
        Booking result = controller.processCheckout(cart, payment);

        // 3. VERIFY
        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals("CONFIRMED", result.getStatus());
        assertTrue(cart.isEmpty());

        verify(mockGateway).processPayment(2050.0, payment);
    }

    @Test
    @DisplayName("Test ProcessCheckout: Fallimento Banca")
    void testProcessCheckout_PaymentRefused() {
        // 1. SETUP
        Cart cart = new Cart();
        cart.addTour(1, "Dragon Ball", 4500.0);
        PaymentDTO payment = new PaymentDTO("CARD-SCADUTA");

        // Anche se la banca fallisce, il controllo disponibilità avviene PRIMA.
        // Se non mettiamo true, fallirebbe per SoldOutException invece che per la banca.
        when(mockRepo.checkAvailability(anyInt(), anyInt())).thenReturn(true);

        // La banca rifiuta
        when(mockGateway.processPayment(anyDouble(), any())).thenReturn(false);

        // 2. ACTION & ASSERT
        Exception e = assertThrows(Exception.class, () -> {
            controller.processCheckout(cart, payment);
        });

        assertEquals("Pagamento rifiutato dalla banca", e.getMessage());
        verify(mockRepo, never()).save(any());
    }
}