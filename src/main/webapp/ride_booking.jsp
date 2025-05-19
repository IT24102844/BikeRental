<%@ page import="com.app.bikerental.model.Bike" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Book a Ride</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      background-color: #f5f5f5;
    }
    .booking-container {
      max-width: 800px;
      margin: 0 auto;
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .bike-list {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 15px;
      margin: 20px 0;
    }
    .bike-card {
      border: 1px solid #ddd;
      padding: 15px;
      border-radius: 5px;
      cursor: pointer;
      transition: all 0.3s;
      position: relative;
    }
    .bike-card:hover {
      border-color: #4CAF50;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    .bike-card.selected {
      border: 2px solid #4CAF50;
      background-color: #f8fff8;
    }
    .bike-card input[type="radio"] {
      position: absolute;
      opacity: 0;
    }
    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    input[type="text"], input[type="submit"], select {
      width: 100%;
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
    }
    input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      border: none;
      padding: 10px;
      font-size: 16px;
      cursor: pointer;
      margin-top: 10px;
    }
    input[type="submit"]:hover {
      background-color: #45a049;
    }
    .alert {
      padding: 10px;
      margin-bottom: 15px;
      border-radius: 4px;
    }
    .alert-error {
      background-color: #f2dede;
      color: #a94442;
      border: 1px solid #ebccd1;
    }
    .alert-info {
      background-color: #e7f4ff;
      color: #0c63e4;
      border: 1px solid #d1e3ff;
    }
    .bike-type-tag {
      display: inline-block;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 12px;
      margin-left: 5px;
    }
    .electric {
      background-color: #e3f2fd;
      color: #1565c0;
    }
    .manual {
      background-color: #e8f5e9;
      color: #2e7d32;
    }
    .queue-status {
      font-size: 14px;
      color: #666;
      margin-top: 5px;
    }
  </style>
</head>
<body>
<div class="booking-container">
  <h1>Book Your Ride</h1>

  <%-- Message display --%>
  <% if (request.getParameter("error") != null) { %>
  <div class="alert alert-error">
    <%= request.getParameter("error") %>
  </div>
  <% } %>
  <% if (request.getParameter("info") != null) { %>
  <div class="alert alert-info">
    <%= request.getParameter("info") %>
  </div>
  <% } %>

  <form action="ride" method="post">
    <input type="hidden" name="action" value="book">

    <h2>1. Select Bike</h2>
    <div class="bike-list">
      <% List<Bike> bikes = (List<Bike>) request.getAttribute("availableBikes");
        if (bikes != null && !bikes.isEmpty()) {
          for (Bike bike : bikes) { %>
      <div class="bike-card" onclick="selectBike(this, '<%= bike.getId() %>', '<%= bike.getType() %>')">
        <input type="radio" name="bikeId" value="<%= bike.getId() %>" id="bike_<%= bike.getId() %>" required>
        <input type="hidden" name="bikeType" value="<%= bike.getType() %>">
        <strong><%= bike.getModel() %></strong>
        <span class="bike-type-tag <%= bike.getType().equalsIgnoreCase("Electric") ? "electric" : "manual" %>">
          <%= bike.getType() %>
        </span>
        <div class="queue-status">
          <% if (bike.getAvailability().equals(Bike.STATUS_AVAILABLE)) { %>
          Available now
          <% } else { %>
          <%= bike.getAvailability() %>
          <% } %>
        </div>
        <br>Condition: <%= bike.getCondition() %>
        <br>Driver: <%= bike.getDriverId() %>
      </div>
      <%   }
      } else { %>
      <p>No available bikes at this time. Please check back later.</p>
      <% } %>
    </div>

    <h2>2. Ride Details</h2>
    <div class="form-group">
      <label for="pickupLocation">Pickup Location:</label>
      <input type="text" id="pickupLocation" name="pickupLocation" required placeholder="Enter pickup address">
    </div>

    <div class="form-group">
      <label for="dropLocation">Drop Location:</label>
      <input type="text" id="dropLocation" name="dropLocation" required placeholder="Enter destination address">
    </div>

    <input type="submit" value="Request Ride">
  </form>
</div>

<script>
  function selectBike(cardElement, bikeId, bikeType) {
    // Update radio button and hidden type field
    document.getElementById('bike_' + bikeId).checked = true;
    document.querySelector('input[name="bikeType"]').value = bikeType;

    // Update UI
    document.querySelectorAll('.bike-card').forEach(card => {
      card.classList.remove('selected');
    });
    cardElement.classList.add('selected');
  }

  // Initialize selected states
  document.addEventListener('DOMContentLoaded', function() {
    // Select first bike by default if available
    const firstBikeCard = document.querySelector('.bike-card');
    if (firstBikeCard) {
      const bikeId = firstBikeCard.getAttribute('onclick').match(/'([^']+)'/)[1];
      const bikeType = firstBikeCard.getAttribute('onclick').match(/'([^']+)'/g)[1].replace(/'/g, "");
      selectBike(firstBikeCard, bikeId, bikeType);
    }
  });

  // Form validation
  document.querySelector('form').addEventListener('submit', function(e) {
    const pickup = document.getElementById('pickupLocation').value.trim();
    const drop = document.getElementById('dropLocation').value.trim();

    if (pickup === '' || drop === '') {
      e.preventDefault();
      alert('Please enter both pickup and drop locations');
      return false;
    }

    if (pickup === drop) {
      e.preventDefault();
      alert('Pickup and drop locations cannot be the same');
      return false;
    }

    return true;
  });
</script>
</body>
</html>