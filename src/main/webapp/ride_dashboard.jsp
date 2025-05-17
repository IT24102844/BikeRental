<%@ page import="com.app.bikerental.model.Ride" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Rider Dashboard</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      background-color: #f5f5f5;
    }
    .dashboard-container {
      max-width: 1000px;
      margin: 0 auto;
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .action-buttons {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
    }
    .btn {
      padding: 10px 15px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-weight: bold;
      text-decoration: none;
      display: inline-block;
      text-align: center;
    }
    .btn-primary {
      background-color: #4CAF50;
      color: white;
    }
    .btn-secondary {
      background-color: #2196F3;
      color: white;
    }
    .ride-card {
      border: 1px solid #ddd;
      padding: 15px;
      margin-bottom: 15px;
      border-radius: 4px;
      background-color: #f9f9f9;
    }
    .status-booked { color: #FF9800; font-weight: bold; }
    .status-inprogress { color: #2196F3; font-weight: bold; }
    .status-completed { color: #4CAF50; font-weight: bold; }
    .status-cancelled { color: #F44336; font-weight: bold; }
    .alert {
      padding: 10px;
      margin-bottom: 15px;
      border-radius: 4px;
    }
    .alert-success {
      background-color: #dff0d8;
      color: #3c763d;
    }
    .alert-danger {
      background-color: #f2dede;
      color: #a94442;
    }
  </style>
</head>
<body>
<div class="dashboard-container">
  <h1>Rider Dashboard</h1>

  <%-- Display success/error messages --%>
  <% if (request.getParameter("msg") != null) { %>
  <div class="alert alert-success"><%= request.getParameter("msg") %></div>
  <% } %>
  <% if (request.getParameter("error") != null) { %>
  <div class="alert alert-danger"><%= request.getParameter("error") %></div>
  <% } %>

  <%-- Action Buttons --%>
  <div class="action-buttons">
    <a href="ride?action=book" class="btn btn-primary">Book New Ride</a>
    <a href="ride?action=history" class="btn btn-secondary">View Ride History</a>
    <a href="ride?action=findShared" class="btn btn-secondary">Find Shared Rides</a>
    <a href="profile.jsp" class="btn btn-secondary">My Profile</a>
  </div>

  <h2>Active Rides</h2>
  <% List<Ride> activeRides = (List<Ride>) request.getAttribute("activeRides");
    if (activeRides != null && !activeRides.isEmpty()) {
      for (Ride ride : activeRides) { %>
  <div class="ride-card">
    <h3>Ride ID: <%= ride.getRideId() %></h3>
    <p><strong>Bike:</strong> <%= ride.getBikeId() %></p>
    <p><strong>From:</strong> <%= ride.getPickupLocation() %></p>
    <p><strong>To:</strong> <%= ride.getDropLocation() %></p>
    <p><strong>Type:</strong> <%= ride.getRideType() %></p>
    <p class="status-<%= ride.getStatus().toLowerCase().replace(" ", "") %>">
      <strong>Status:</strong> <%= ride.getStatus() %>
    </p>

    <div class="ride-actions">
      <% if (ride.getStatus().equals(Ride.STATUS_BOOKED)) { %>
      <form action="ride" method="post" style="display: inline;">
        <input type="hidden" name="action" value="updateStatus">
        <input type="hidden" name="rideId" value="<%= ride.getRideId() %>">
        <input type="hidden" name="status" value="<%= Ride.STATUS_IN_PROGRESS %>">
        <button type="submit" class="btn btn-primary">Start Ride</button>
      </form>
      <form action="ride" method="post" style="display: inline;">
        <input type="hidden" name="action" value="updateStatus">
        <input type="hidden" name="rideId" value="<%= ride.getRideId() %>">
        <input type="hidden" name="status" value="<%= Ride.STATUS_CANCELLED %>">
        <button type="submit" class="btn btn-danger">Cancel Ride</button>
      </form>
      <% } else if (ride.getStatus().equals(Ride.STATUS_IN_PROGRESS)) { %>
      <form action="ride" method="post" style="display: inline;">
        <input type="hidden" name="action" value="updateStatus">
        <input type="hidden" name="rideId" value="<%= ride.getRideId() %>">
        <input type="hidden" name="status" value="<%= Ride.STATUS_COMPLETED %>">
        <button type="submit" class="btn btn-success">Complete Ride</button>
      </form>
      <% } %>
    </div>
  </div>
  <%   }
  } else { %>
  <p>No active rides found. Click "Book New Ride" to get started!</p>
  <% } %>
</div>
</body>
</html>