<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Ordine Confermato - AniTour</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="styles/style.css">
  <link rel="stylesheet" href="styles/success.css">
  <link href="https://fonts.googleapis.com/css2?family=Farro:wght@300;400;500;700&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-container">
  <header id="fixed-header">
    <div style="display:flex; justify-content:center; align-items:center; height:100%;">
      <h2 style="color:var(--primary); margin:0;">AniTour</h2>
    </div>
  </header>

  <div class="main-content">
    <div class="confirmation-container">
      <div class="confirmation-icon" style="font-size: 80px;">
        ✅
      </div>

      <h1>Ordine completato con successo!</h1>

      <p class="confirmation-message">
        Grazie per il tuo acquisto su AniTour.
      </p>

      <div class="order-details">
        <h2>Riepilogo Prenotazione</h2>
        <p>Il tuo ordine è stato registrato correttamente.</p>

        <div style="background: rgba(255,255,255,0.6); padding: 15px; border-radius: 8px; margin-top: 15px;">
          <p style="font-size: 1.2rem; color: var(--primary); font-weight: bold;">
            CODICE ORDINE: #<%= request.getAttribute("orderId") %>
          </p>
          <p>Stato: <span style="color:green; font-weight:bold;">CONFIRMED</span></p>
        </div>
      </div>

      <div class="next-steps">
        <h2>Cosa succede ora?</h2>
        <ul>
          <li>Riceverai una email di conferma con il voucher PDF.</li>
          <li>Stampa il voucher o salvalo sul tuo smartphone.</li>
          <li>Presentati al punto di ritrovo 15 minuti prima dell'inizio.</li>
        </ul>
      </div>

      <div class="confirmation-actions">
        <a href="home" class="btn-home">Torna alla Home</a>
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