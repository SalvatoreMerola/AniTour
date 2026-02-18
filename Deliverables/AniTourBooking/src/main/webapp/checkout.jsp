<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.anitour.model.Tour" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Checkout - AniTour</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="styles/style.css">
    <link rel="stylesheet" href="styles/checkout.css">
    <link href="https://fonts.googleapis.com/css2?family=Farro:wght@300;400;500;700&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-container">
    <header id="fixed-header">
        <div style="display:flex; justify-content:center; align-items:center; height:100%;">
            <h2 style="color:var(--primary); margin:0;">AniTour Checkout</h2>
        </div>
    </header>

    <div class="main-content">
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error-banner">
            ⚠️ <%= error %>
        </div>
        <% } %>

        <div class="checkout-container">
            <div class="checkout-form">
                <h2>Dati di Pagamento</h2>

                <form action="prenota" method="POST">
                    <input type="hidden" name="tourId" value="<%= request.getAttribute("selectedTourId") != null ? request.getAttribute("selectedTourId") : "1" %>">

                    <div class="form-group">
                        <label for="shippingEmail">Email Cliente</label>
                        <input type="email" id="shippingEmail" name="shippingEmail" placeholder="tuamail@esempio.com" required>
                    </div>

                    <div class="form-group">
                        <label for="cardNumber">Carta di Credito</label>
                        <input type="text" id="cardNumber" name="cardNumber" placeholder="xxxx-xxxx-xxxx-xxxx" required>
                        <small style="color: #666;">*Demo: Usa una carta che finisce con 9999 per simulare errore.</small>
                    </div>

                    <div class="form-row">
                        <div class="form-group half">
                            <label>Scadenza</label>
                            <input type="text" placeholder="MM/YY">
                        </div>
                        <div class="form-group half">
                            <label>CVV</label>
                            <input type="text" placeholder="123">
                        </div>
                    </div>

                    <div class="checkout-actions">
                        <a href="home" class="btn-back">Annulla</a>
                        <button type="submit" class="btn-place-order">Conferma Ordine</button>
                    </div>
                </form>
            </div>

            <div class="order-summary">
                <h2>Riepilogo Ordine</h2>
                <div class="summary-items">
                    <%
                        // Recuperiamo il tour selezionato dalla lista passata dalla Servlet
                        List<Tour> items = (List<Tour>) request.getAttribute("cartItems");
                        if (items != null && !items.isEmpty()) {
                            for (Tour item : items) {
                    %>
                    <div class="summary-item">
                        <img src="<%= item.getImagePath() %>" alt="Foto Tour" style="width: 80px; height: 60px; object-fit: cover; border-radius: 5px; margin-right: 15px;">

                        <div class="summary-item-details">
                            <h3><%= item.getName() %></h3>
                            <p>Quantità: 1</p>
                        </div>
                        <div class="summary-item-price">
                            <%= item.getPrice() %> €
                        </div>
                    </div>
                    <%
                        }
                    } else {
                    %>
                    <p>Nessun tour selezionato.</p>
                    <% } %>
                </div>

                <div class="summary-total">
                    <span>Totale da Pagare</span>
                    <span class="total-price">
                            <%
                                if (items != null && !items.isEmpty()) {
                                    out.print(items.get(0).getPrice() + " €");
                                } else {
                                    out.print("0.00 €");
                                }
                            %>
                        </span>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <div id="footer-content">
            <p>&copy; 2026 AniTour Demo</p>
        </div>
    </footer>
</div>
</body>
</html>