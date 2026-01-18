package com.anitour.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentDTOTest {

    @Test
    @DisplayName("Carta valida")
    void testCartaValida() {
        // Caso positivo: carta lunga (più di 10 caratteri)
        PaymentDTO payment = new PaymentDTO("1234-5678-9012-3456");
        assertTrue(payment.isValid(), "La carta dovrebbe essere valida");
    }

    @Test
    @DisplayName("Carta non valida: troppo corta")
    void testCartaTroppoCorta() {
        // Caso negativo: carta corta
        PaymentDTO payment = new PaymentDTO("123");
        assertFalse(payment.isValid(), "La carta troppo corta non deve essere valida");
    }

    @Test
    @DisplayName("Carta nulla")
    void testCartaNulla() {
        // Caso limite: null
        PaymentDTO payment = new PaymentDTO(null);
        assertFalse(payment.isValid(), "Il null non deve essere valido");
    }
}