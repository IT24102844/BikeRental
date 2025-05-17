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
    }
    .bike-card:hover {
      border-color: #4CAF50;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    .bike-card.selected {
      border: 2px solid #4CAF50;
      background-color: #f8fff8;
    }
    input[type="radio"] {
      margin-right: 10px;
    }
    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    input[type="text"], input[type="submit"] {
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
    .ride-type-options {
      display: flex;
      gap: 15px;
      margin: 15px 0;
    }
    .ride-type-option {
      flex: 1;
      border: 1px solid #ddd;
      padding: 10px;
      border-radius: 5px;
      text-align: center;
      cursor: pointer;
    }
    .ride-type-option.selected {
      border-color: #4CAF50;
      background-color: #f8fff8;
    }
  </style>
</head>
<body>
<div class="booking-container">
  <h1>Book Your Ride</h1>

  <%-- Error message display --%>
  <% if (request.getParameter("error") != null) { %>
  <div class="alert alert-error">
    <%= request.getParameter("error") %>
  </div>
  <% } %>

  <form action="ride" method="post">
    <input type="hidden" name="action" value="book">

    <h2>1. Choose Ride Type</h2>
    <div class="ride-type-options">
      <div class="ride-type-option" onclick="selectRideType('Solo')">
        <input type="radio" name="rideType" value="Solo" id="rideTypeSolo" checked>
        <label for="rideTypeSolo">Solo Ride</label>
        <p>Private ride just for you</p>
      </div>
      <div class="ride-type-option" onclick="selectRideType('Shared')">
        <input type="radio" name="rideType" value="Shared" id="rideTypeShared">
        <label for="rideTypeShared">Shared Ride</label>
        <p>Share with others and save 30%</p>
      </div>
    </div>

    <h2>2. Select Bike</h2>
    <div class="bike-list">
      <% List<Bike> bikes = (List<Bike>) request.getAttribute("availableBikes");
        if (bikes != null && !bikes.isEmpty()) {
          for (Bike bike : bikes) { %>
      <div class="bike-card" onclick="selectBike(this, '<%= bike.getId() %>')">
        <input type="radio" name="bikeId" value="<%= bike.getId() %>" id="bike_<%= bike.getId() %>" required>
        <strong><%= bike.getModel() %></strong> (<%= bike.getType() %>)
        <br>Condition: <%= bike.getCondition() %>
        <br>Driver: <%= bike.getDriverId() %>
      </div>
      <%   }
      } else { %>
      <p>No available bikes at this time.</p>
      <% } %>
    </div>

    <h2>3. Ride Details</h2>
    <div class="form-group">
      <label for="pickupLocation">Pickup Location:</label>
      <input type="text" id="pickupLocation" name="pickupLocation" required>
    </div>

    <div class="form-group">
      <label for="dropLocation">Drop Location:</label>
      <input type="text" id="dropLocation" name="dropLocation" required>
    </div>

    <input type="submit" value="Confirm Booking">
  </form>
</div>

<script>
  function selectBike(cardElement, bikeId) {
    // Update radio button
    document.getElementById('bike_' + bikeId).checked = true;

    // Update UI
    document.querySelectorAll('.bike-card').forEach(card => {
      card.classList.remove('selected');
    });
    cardElement.classList.add('selected');
  }

  function selectRideType(type) {
    // Update radio button
    document.getElementById('rideType' + type).checked = true;

    // Update UI
    document.querySelectorAll('.ride-type-option').forEach(option => {
      option.classList.remove('selected');
    });
    document.querySelector(`.ride-type-option[onclick="selectRideType('${type}')"]`)
            .classList.add('selected');
  }

  // Initialize selected states
  document.addEventListener('DOMContentLoaded', function() {
    // Select first bike by default if available
    const firstBikeCard = document.querySelector('.bike-card');
    if (firstBikeCard) {
      const bikeId = firstBikeCard.getAttribute('onclick').match(/'([^']+)'/)[1];
      selectBike(firstBikeCard, bikeId);
    }

    // Select solo ride by default
    document.querySelector('.ride-type-option[onclick="selectRideType(\'Solo\')"]')
            .classList.add('selected');
  });
</script>
</body>
</html>