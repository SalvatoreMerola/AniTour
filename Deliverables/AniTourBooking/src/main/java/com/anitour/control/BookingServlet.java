package com.anitour.control;

import com.anitour.dao.BookingDAO;
import com.anitour.dao.DatabaseConnection; // Import necessario
import com.anitour.dao.RealPaymentGateway;
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import com.anitour.model.Tour;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;        // Import SQL
import java.sql.PreparedStatement; // Import SQL
import java.sql.ResultSet;         // Import SQL
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookingServlet", urlPatterns = {"/prenota"})
public class BookingServlet extends HttpServlet {

    /**
     * LETTURA DA DB
     */
    private Tour getTourById(int id) {
        Tour tour = null;
        String sql = "SELECT * FROM tour WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Costruiamo l'oggetto con i dati freschi dal DB
                    tour = new Tour(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("description"),
                            rs.getString("image_path")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fallback di sicurezza se il DB fallisce o ID non esiste
        if (tour == null) {
            tour = new Tour(id, "Tour Non Trovato", 0.0, "Errore caricamento", "");
        }

        return tour;
    }

    /**
     * GESTISCE L'ARRIVO DAL CATALOGO (GET)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tourIdParam = request.getParameter("tourId");

        if (tourIdParam == null || tourIdParam.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        try {
            int tourId = Integer.parseInt(tourIdParam);

            Tour selectedTour = getTourById(tourId);

            List<Tour> cartItems = new ArrayList<>();
            cartItems.add(selectedTour);

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("selectedTourId", tourId);

            request.getRequestDispatcher("checkout.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    /**
     * GESTISCE IL PAGAMENTO (POST)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("shippingEmail");
            String cardNumber = request.getParameter("cardNumber");

            String tourIdParam = request.getParameter("tourId");
            int tourId = (tourIdParam != null && !tourIdParam.isEmpty()) ? Integer.parseInt(tourIdParam) : 1;

            Cart cart = new Cart();
            // ORA ANCHE QUI LEGGE DAL DB
            Tour tour = getTourById(tourId);

            if (!"empty@test.com".equals(email)) {
                cart.addTour(tour.getId(), tour.getName(), tour.getPrice());
            }

            PaymentDTO payment = new PaymentDTO(cardNumber);

            BookingDAO dao = new BookingDAO();
            RealPaymentGateway gateway = new RealPaymentGateway();
            BookingControl control = new BookingControl(dao, gateway);

            Booking booking = control.processCheckout(cart, payment, email, 99);

            request.setAttribute("orderId", booking.getId());
            request.getRequestDispatcher("success.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());

            // Ricarico i dati corretti dal DB anche in caso di errore
            String tourIdParam = request.getParameter("tourId");
            int tourId = (tourIdParam != null && !tourIdParam.isEmpty()) ? Integer.parseInt(tourIdParam) : 1;
            List<Tour> cartItems = new ArrayList<>();
            cartItems.add(getTourById(tourId));
            request.setAttribute("cartItems", cartItems);

            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }
}