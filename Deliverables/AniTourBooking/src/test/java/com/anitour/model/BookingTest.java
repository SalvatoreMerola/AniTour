package com.anitour.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testCreazioneDaCarrello() {
        // 1. Preparo un carrello finto
        Cart cart = new Cart();
        cart.addTour(10, "Tour Test", 1999.99);

        // 2. Creo il Booking
        Booking booking = new Booking(cart, Booking.CONFIRMED);

        // 3. Verifico che abbia preso i dati dal carrello
        assertEquals(1999.99, booking.getPrice(), 0.01, "Il prezzo del booking deve coincidere col totale carrello");
        assertEquals(Booking.CONFIRMED, booking.getStatus(), "Lo status deve essere quello passato");
    }

    @Test
    void testGestioneStati() {
        Booking b = new Booking();

        // 1. Verifico stato iniziale o PENDING
        b.setStatus(Booking.PENDING);
        assertEquals("PENDING", b.getStatus(), "Lo stato dovrebbe essere PENDING");

        // 2. Simulo conferma ordine
        b.setStatus(Booking.CONFIRMED);
        assertEquals("CONFIRMED", b.getStatus(), "Lo stato dovrebbe essere aggiornato a CONFIRMED");
    }

    @Test
    void testGetterSetter() {
        Booking b = new Booking();
        b.setCustomerEmail("test@email.com");
        b.setTourId(5);

        assertEquals("test@email.com", b.getCustomerEmail());
        assertEquals(5, b.getTourId());
    }
}