<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.anitour.model.Tour" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Catalogo Tour - AniTour</title>
  <link rel="stylesheet" href="styles/style.css">
  <link rel="stylesheet" href="styles/tours.css">
  <link href="https://fonts.googleapis.com/css2?family=Farro:wght@300;400;500;700&display=swap" rel="stylesheet">
</head>
<body>
<div class="page-container">
  <header id="fixed-header">
    <div style="display:flex; justify-content:center; align-items:center; height:100%;">
      <h1 style="color:var(--primary); font-family:'Farro', sans-serif;">AniTour Demo</h1>
    </div>
  </header>

  <div class="main-content">
    <h1 class="title1" style="color: var(--primary);">SCEGLI LA TUA AVVENTURA</h1>

    <div class="card-container">
      <%
        List<Tour> tours = (List<Tour>) request.getAttribute("tours");
        if (tours != null) {
          for (Tour tour : tours) {
      %>
      <a href="prenota?tourId=<%= tour.getId() %>">
        <div class="card">
          <img class="card-img" src="<%= tour.getImagePath() %>" alt="<%= tour.getName() %>">
          <div class="card-title"><%= tour.getName() %></div>
          <div class="card-text"><%= tour.getDescription() %></div>
          <div class="prezzo"><%= tour.getPrice() %> €</div>
          <div style="text-align:center; margin-top:1rem;">
            <button class="btn1">PRENOTA ORA</button>
          </div>
        </div>
      </a>
      <%      }
      } %>
    </div>
  </div>
</div>
</body>
</html>