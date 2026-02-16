package com.anitour.dao;

import com.anitour.model.Booking;
import java.sql.*;

public class BookingDAO implements IBookingRepository {

    @Override
    public int save(Booking booking) {
        // Query aggiornata con tutti i campi del DB
        String sql = "INSERT INTO bookings (user_id, tour_id, customer_email, price, status) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Usiamo i valori dell'oggetto Booking
            ps.setInt(1, booking.getUserId() > 0 ? booking.getUserId() : 99); // Fallback a 99 per demo se null
            ps.setInt(2, booking.getTourId() > 0 ? booking.getTourId() : 1);  // Fallback a 1 per demo
            ps.setString(3, booking.getCustomerEmail());
            ps.setDouble(4, booking.getPrice());
            ps.setString(5, booking.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    @Override
    public boolean checkAvailability(int tourId, int requiredSeats) {
        // CONTROLLO REALE SU DB
        String sql = "SELECT available_seats FROM tour WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tourId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int available = rs.getInt("available_seats");
                    return available >= requiredSeats;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Se errore o tour non trovato, nega l'accesso
    }

    // Metodo per scalare i posti (necessario per il Sold Out dinamico)
    public void decrementSeats(int tourId, int quantity) {
        String sql = "UPDATE tour SET available_seats = available_seats - ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, tourId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() { return 0; } // Non serve per la demo
}