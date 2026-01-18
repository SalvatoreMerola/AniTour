package com.anitour.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    @DisplayName("Happy Path")
    void testHappyPath() {
        Cart cart = new Cart();
        cart.addTour(101, "Sekiro Tour", 25.50);

        assertFalse(cart.isEmpty(), "Il carrello non deve essere vuoto");
        assertEquals(1, cart.getItemCount());
        assertEquals(25.50, cart.getTotal());
    }

    @Test
    @DisplayName("Svuota carrello")
    void testSvuotaCarrello() {
        Cart cart = new Cart();
        cart.addTour(101, "Sekiro Tour", 25.50);
        cart.removeTour(101);

        assertTrue(cart.isEmpty(), "Ora dovrebbe essere vuoto");
        assertEquals(0.0, cart.getTotal());
    }
}