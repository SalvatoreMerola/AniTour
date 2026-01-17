package com.anitour.dao;

import com.anitour.model.Booking;
import java.sql.*;

public class BookingDAO implements IBookingRepository {

    @Override
    public int save(Booking booking) {
        String sql = "INSERT INTO bookings (status) VALUES (?)";
        int generatedId = -1; // Stato di partenza

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, booking.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1); // Aggiorno id con quello reale
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}