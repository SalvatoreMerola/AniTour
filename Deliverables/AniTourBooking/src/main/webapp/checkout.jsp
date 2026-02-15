<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Checkout AniTour</title>
    <style>
        .error { color: red; font-weight: bold; padding: 10px; border: 1px solid red; background-color: #ffeeee; }
        .container { font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; padding: 20px; border: 1px solid #ddd; }
        input { width: 100%; padding: 8px; margin: 5px 0; }
        button { background-color: #28a745; color: white; padding: 10px; border: none; width: 100%; cursor: pointer; }
        button:hover { background-color: #218838; }
    </style>
</head>
<body>

<div class="container">
    <h2>Checkout AniTour</h2>

    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if (error != null) { %>
    <div class="error">
        ATTENZIONE: <%= error %>
    </div>
    <% } %>

    <form action="prenota" method="POST">

        <label>Email Cliente:</label>
        <input type="email" name="email" placeholder="mariorossi@email.com" required>

        <label>Numero Carta:</label>
        <input type="text" name="cardNumber" placeholder="xxxx-xxxx-xxxx-xxxx" required>

        <input type="hidden" name="tourId" value="1">

        <button type="submit">Conferma e Paga</button>
    </form>

    <p style="font-size: small; color: #666;">
        *Demo Info: Usa una carta che finisce con <b>9999</b> per testare il rifiuto pagamento.
    </p>
</div>

</body>
</html>