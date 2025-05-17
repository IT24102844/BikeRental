<%@ page import="com.app.bikerental.model.Ride" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ride History</title>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
    </style>
</head>
<body>
<h1>Your Ride History</h1>

<table>
    <tr>
        <th>Ride ID</th>
        <th>Date</th>
        <th>Bike</th>
        <th>From</th>
        <th>To</th>
        <th>Status</th>
        <th>Type</th>
    </tr>
    <% List<Ride> rides = (List<Ride>) request.getAttribute("rides");
        if (rides != null) {
            for (Ride ride : rides) { %>
    <tr>
        <td><%= ride.getRideId() %></td>
        <td><%= ride.getTimestamp() %></td>
        <td><%= ride.getBikeId() %></td>
        <td><%= ride.getPickupLocation() %></td>
        <td><%= ride.getDropLocation() %></td>
        <td><%= ride.getStatus() %></td>
    </tr>
    <%   }
    } %>
</table>

<a href="ride?action=dashboard">Back to Dashboard</a>
</body>
</html>