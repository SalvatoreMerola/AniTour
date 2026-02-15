package com.anitour.dao;

import com.anitour.model.Booking;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOIntegrationTest {

    private BookingDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new BookingDAO();

        // Pulizia tabella reale per avere un test pulito
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM bookings");
        }
    }

    @Test
    void testSalvataggioRealeSuDB() {
        System.out.println("TEST INTEGRATION: Tentativo salvataggio su DB reale...");

        // A. Arrange
        Booking nuovoOrdine = new Booking();
        nuovoOrdine.setTourId(1);
        nuovoOrdine.setUserId(99);
        nuovoOrdine.setPrice(2500.0);
        nuovoOrdine.setStatus("CONFIRMED");
        nuovoOrdine.setCustomerEmail("test@integration.com");

        // B. Act
        int newId = dao.save(nuovoOrdine);

        // C. Assert
        System.out.println("Salvataggio completato. Nuovo ID: " + newId);
        assertTrue(newId > 0, "L'ID generato dal database deve essere maggiore di 0");
    }
}