package com.anitour.control;

import com.anitour.dao.BookingDAO;
import com.anitour.dao.RealPaymentGateway; // La classe creata al passo 2
import com.anitour.model.Booking;
import com.anitour.model.Cart;
import com.anitour.model.PaymentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BookingServlet", urlPatterns = {"/prenota"})
public class BookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. Recupero dati dalla form HTML
            String email = request.getParameter("email");
            String cardNumber = request.getParameter("cardNumber");
            // Per semplicità demo, fissiamo un Tour ID e quantità
            int tourId = 1;

            // 2. Costruisco gli oggetti reali (Bottom-Up)
            Cart cart = new Cart();
            cart.addTour(tourId, "Tour Demo", 100.0); // Prezzo fisso per demo

            PaymentDTO payment = new PaymentDTO(cardNumber);

            BookingDAO dao = new BookingDAO();
            RealPaymentGateway gateway = new RealPaymentGateway();
            BookingControl control = new BookingControl(dao, gateway);

            // 3. Eseguo il checkout
            Booking booking = control.processCheckout(cart, payment);

            // CASO SUCCESSO:
            // Passo l'ID ordine alla JSP di successo
            request.setAttribute("orderId", booking.getId());
            // Inoltro (Forward) l'utente alla pagina di successo
            request.getRequestDispatcher("success.jsp").forward(request, response);

        } catch (Exception e) {
            // CASO ERRORE (SoldOut o Pagamento Fallito):
            // Metto il messaggio dell'eccezione nell'attributo "errorMessage"
            request.setAttribute("errorMessage", e.getMessage());
            // Ricarico la stessa pagina di checkout per mostrare l'errore
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }
}