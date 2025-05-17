<%@ page import="com.app.bikerental.model.Ride" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Error message display --%>
<% if (request.getParameter("error") != null) { %>
<div class="alert alert-danger">
    <%= request.getParameter("error") %>
</div>
<% } %>

<%-- Success message display --%>
<% if (request.getParameter("msg") != null) { %>
<div class="alert alert-success">
    <%= request.getParameter("msg") %>
</div>
<% } %>
<html>
<head>
    <title>Shared Rides</title>
</head>
<body>
<h1>Available Shared Rides</h1>

<form method="get">
    <label>Pickup Location:</label>
    <input type="text" name="pickup" value="<%= request.getParameter("pickup") != null ? request.getParameter("pickup") : "" %>">

    <label>Drop Location:</label>
    <input type="text" name="drop" value="<%= request.getParameter("drop") != null ? request.getParameter("drop") : "" %>">

    <button type="submit">Search</button>
</form>

<% List<Ride> sharedRides = (List<Ride>) request.getAttribute("sharedRides");
    if (sharedRides != null && !sharedRides.isEmpty()) { %>
<table border="1">
    <tr>
        <th>Ride ID</th>
        <th>Departure Time</th>
        <th>Available Seats</th>
        <th>Action</th>
    </tr>
    <% for (Ride ride : sharedRides) { %>
    <tr>
        <td><%= ride.getRideId() %></td>
        <td><%= ride.getTimestamp() %></td>
        <td>1/2</td> <!-- Hardcoded for example -->
        <td>
            <form action="ride" method="post">
                <input type="hidden" name="action" value="findShared">
                <input type="hidden" name="rideId" value="<%= ride.getRideId() %>">
                <button type="submit">Join Ride</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
<% } else { %>
<p>No shared rides matching your criteria.</p>
<% } %>
</body>
</html>