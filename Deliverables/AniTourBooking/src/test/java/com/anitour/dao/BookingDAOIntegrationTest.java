package com.anitour.dao;

import com.anitour.model.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOIntegrationTest {

    @Test
    @DisplayName("Salvataggio su DB vero")
    public void testSalvataggioSuDBVero() {
        BookingDAO dao = new BookingDAO();
        Booking b = new Booking();
        b.setStatus("CONFIRMED");

        int id = dao.save(b);

        System.out.println("Nuovo ID generato dal DB: " + id);
        assertTrue(id > 0, "Il database dovrebbe aver generato un ID");
    }
}