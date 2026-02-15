<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Ordine Confermato</title>
  <style>
    .success { color: green; font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
    .details { border: 1px solid #ddd; max-width: 400px; margin: 20px auto; padding: 20px; }
  </style>
</head>
<body>
<div class="success">
  <h1>✅ Ordine Confermato!</h1>
  <p>Grazie per aver scelto AniTour.</p>
</div>

<div class="details">
  <h3>Dettagli Ordine</h3>
  <p><strong>ID Ordine:</strong> <%= request.getAttribute("orderId") %></p>
  <p><strong>Stato:</strong> CONFIRMED</p>
  <p>Voucher inviato via email.</p>

  <br>
  <a href="checkout.jsp">Torna alla Home</a>
</div>
</body>
</html>