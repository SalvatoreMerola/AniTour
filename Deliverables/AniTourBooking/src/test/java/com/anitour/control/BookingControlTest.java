package com.anitour.control;

import com.anitour.dao.IBookingRepository;
import com.anitour.dao.IPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class BookingControlTest {

    // Utilizzo i mock per simulare le interfacce dell'ODD
    @Mock
    private IBookingRepository mockRepo;
    @Mock
    private IPaymentGateway mockGateway;

    private BookingControl controller;

    @BeforeEach
    void setUp() {
        // Inizializza i mock e iniettali nel controller
        MockitoAnnotations.openMocks(this);
        controller = new BookingControl(mockRepo, mockGateway);
    }

    @Test
    @DisplayName("Test ProcessCheckout: Successo (ODD 3.1.1)")
    void testProcessCheckout_Success() throws Exception {
        // 1. SETUP (Given)
        Cart cart = new Cart();
        cart.addTour(1, "One Piece", 2050.0);
        PaymentDTO payment = new PaymentDTO("1234-5678-9012-3456");

        // Istruzioni mock:
        // "Quando ti chiedono di pagare, dì di SÌ" (Simula ODD 3.2.1)
        when(mockGateway.processPayment(anyDouble(), any(PaymentDTO.class))).thenReturn(true);
        // "Quando ti chiedono di salvare, restituisci ID 100" (Simula ODD 3.3.1)
        when(mockRepo.save(any(Booking.class))).thenReturn(100);

        // 2. ACTION (When)
        Booking result = controller.processCheckout(cart, payment);

        // 3. VERIFY (Then)
        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals("CONFIRMED", result.getStatus());

        // Verifica Post-Condizione ODD: Il carrello deve essere svuotato
        assertTrue(cart.isEmpty(), "Il carrello deve essere vuoto dopo l'acquisto");

        // Verifica interazioni: Siamo sicuri che abbia chiamato la banca?
        verify(mockGateway).processPayment(2050.0, payment);
    }

    @Test
    @DisplayName("Test ProcessCheckout: Fallimento Banca")
    void testProcessCheckout_PaymentRefused() {
        // 1. SETUP
        Cart cart = new Cart();
        cart.addTour(1, "Dragon Ball", 4500.0);
        PaymentDTO payment = new PaymentDTO("CARD-SCADUTA");

        // La banca rifiuta
        when(mockGateway.processPayment(anyDouble(), any())).thenReturn(false);

        // 2. ACTION & ASSERT
        Exception e = assertThrows(Exception.class, () -> {
            controller.processCheckout(cart, payment);
        });

        assertEquals("Pagamento rifiutato dalla banca", e.getMessage());

        // Verifica che NON abbiamo salvato nulla nel DB
        verify(mockRepo, never()).save(any());
    }
}