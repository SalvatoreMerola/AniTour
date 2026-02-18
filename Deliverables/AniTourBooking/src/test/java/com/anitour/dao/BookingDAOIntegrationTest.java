package com.anitour.dao;

import com.anitour.model.Booking;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOIntegrationTest {

    private BookingDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new BookingDAO();

        // 1. Pulizia tabella ordini
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM bookings");
        }

        // 2. RESET dello stato del Tour 1 (Bloodborne) per i test di inventario
        // Lo riportiamo a 50 posti per partire da una situazione nota
        updateTourSeats(1, 50);
    }

    // --- TEST 1: SALVATAGGIO ---
    @Test
    void testSalvataggioRealeSuDB() {
        System.out.println("TEST INTEGRATION: Tentativo salvataggio su DB reale...");

        Booking nuovoOrdine = new Booking();
        nuovoOrdine.setTourId(1);
        nuovoOrdine.setUserId(99);
        nuovoOrdine.setPrice(2500.0);
        nuovoOrdine.setStatus("CONFIRMED");
        nuovoOrdine.setCustomerEmail("test@integration.com");

        int newId = dao.save(nuovoOrdine);

        System.out.println("Salvataggio completato. Nuovo ID: " + newId);
        assertTrue(newId > 0, "L'ID generato dal DB deve essere maggiore di 0");
    }

    // --- TEST 2: CONTROLLO DISPONIBILITÀ ---
    @Test
    void testCheckAvailability() {
        System.out.println("TEST INTEGRATION: Verifica disponibilità posti...");

        // Caso A: Ci sono 50 posti, ne chiedo 1 -> Deve tornare TRUE
        boolean resultOk = dao.checkAvailability(1, 1);
        assertTrue(resultOk, "Dovrebbe esserci disponibilità per 1 posto");

        // Caso B: Ne chiedo 100 -> Deve tornare FALSE
        boolean resultKo = dao.checkAvailability(1, 100);
        assertFalse(resultKo, "Non dovrebbe esserci disponibilità per 100 posti");
    }

    // --- TEST 3: DECREMENTO POSTI ---
    @Test
    void testDecrementSeats() {
        System.out.println("TEST INTEGRATION: Verifica decremento inventario...");

        // 1. Verifico posti iniziali (Setup li ha messi a 50)
        int postiIniziali = getTourSeats(1);
        assertEquals(50, postiIniziali);

        // 2. Chiamo il metodo DAO per togliere 5 posti
        dao.decrementSeats(1, 5);

        // 3. Verifico direttamente sul DB che siano diventati 45
        int postiFinali = getTourSeats(1);
        assertEquals(45, postiFinali, "I posti nel DB dovevano scendere da 50 a 45");
    }

    // --- Metodi Helper per il Test (servono per verificare il DB) ---

    private void updateTourSeats(int tourId, int seats) {
        String sql = "UPDATE tour SET available_seats = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seats);
            ps.setInt(2, tourId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTourSeats(int tourId) {
        String sql = "SELECT available_seats FROM tour WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tourId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_seats");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Errore
    }
}