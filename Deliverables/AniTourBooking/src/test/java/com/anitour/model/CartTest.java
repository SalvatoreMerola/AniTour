package com.anitour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testAggiuntaTour() {
        Cart cart = new Cart();
        cart.addTour(1, "Tour One Piece", 2000.0);

        assertEquals(1, cart.getItemCount(), "Il carrello dovrebbe avere 1 elemento");
        assertFalse(cart.isEmpty(), "Il carrello non deve essere vuoto");
        assertEquals(2000.0, cart.getTotal(), "Il totale deve essere 2000.0");
    }

    @Test
    void testCalcoloTotaleMultiplo() {
        Cart cart = new Cart();
        cart.addTour(1, "Tour One Piece", 2000.0);
        cart.addTour(2, "Tour Sekiro", 3000.0);

        // 2000 + 3000 = 5000
        assertEquals(5000.0, cart.getTotal(), 0.01, "Il totale matematico è errato");
    }

    @Test
    void testSvuotaCarrello() {
        Cart cart = new Cart();
        cart.addTour(1, "Tour One Piece", 2000.0);

        cart.clear(); // Azione

        assertTrue(cart.isEmpty(), "Il carrello deve essere vuoto dopo il clear()");
        assertEquals(0.0, cart.getTotal(), "Il totale deve essere 0 dopo il clear()");
    }
}