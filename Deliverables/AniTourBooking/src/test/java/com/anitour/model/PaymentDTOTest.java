package com.anitour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentDTOTest {

    @Test
    void testCartaValida() {
        // Carta > 10 caratteri
        PaymentDTO payment = new PaymentDTO("1234-5678-9012");
        assertTrue(payment.isValid(), "Una carta lunga deve essere valida");
    }

    @Test
    void testCartaTroppoCorta() {
        PaymentDTO payment = new PaymentDTO("123");
        assertFalse(payment.isValid(), "Una carta corta non deve essere valida");
    }

    @Test
    void testCartaNulla() {
        PaymentDTO payment = new PaymentDTO(null);
        assertFalse(payment.isValid(), "Se il numero è null, deve restituire false");
    }
}