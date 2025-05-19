<%@ page import="com.app.bikerental.model.Ride" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shared Rides</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .search-form {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
        .alert {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .action-btn {
            background-color: #2196F3;
            padding: 6px 12px;
            font-size: 14px;
        }
        .action-btn:hover {
            background-color: #0b7dda;
        }
        .no-rides {
            text-align: center;
            padding: 20px;
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Available Shared Rides</h1>

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

    <div class="search-form">
        <form method="get" action="ride">
            <input type="hidden" name="action" value="findShared">
            <div class="form-group">
                <label for="pickup">Pickup Location:</label>
                <input type="text" id="pickup" name="pickup"
                       value="<%= request.getParameter("pickup") != null ?
                                  request.getParameter("pickup") : "" %>"
                       placeholder="Enter pickup address" required>
            </div>
            <div class="form-group">
                <label for="drop">Drop Location:</label>
                <input type="text" id="drop" name="drop"
                       value="<%= request.getParameter("drop") != null ?
                                  request.getParameter("drop") : "" %>"
                       placeholder="Enter destination address" required>
            </div>
            <button type="submit">Search Rides</button>
        </form>
    </div>

    <% List<Ride> sharedRides = (List<Ride>) request.getAttribute("sharedRides"); %>
    <% if (sharedRides != null && !sharedRides.isEmpty()) { %>
    <table>
        <thead>
        <tr>
            <th>Ride ID</th>
            <th>Pickup Location</th>
            <th>Drop Location</th>
            <th>Departure Time</th>
            <th>Available Seats</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (Ride ride : sharedRides) { %>
        <tr>
            <td><%= ride.getRideId() %></td>
            <td><%= ride.getPickupLocation() %></td>
            <td><%= ride.getDropLocation() %></td>
            <td><%= ride.getTimestamp() %></td>
            <td><%= ride.getAvailableSeats() %>/2</td>
            <td>
                <form action="ride" method="post">
                    <input type="hidden" name="action" value="joinShared">
                    <input type="hidden" name="rideId" value="<%= ride.getRideId() %>">
                    <button type="submit" class="action-btn"
                            <%= ride.getAvailableSeats() <= 0 ? "disabled" : "" %>>
                        <%= ride.getAvailableSeats() <= 0 ? "Full" : "Join Ride" %>
                    </button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else if (sharedRides != null) { %>
    <div class="no-rides">
        <p>No shared rides found matching your criteria.</p>
        <% if (request.getParameter("pickup") != null || request.getParameter("drop") != null) { %>
        <p>Try different pickup or drop locations.</p>
        <% } %>
    </div>
    <% } %>
</div>

<script>
    // Client-side form validation
    document.querySelector('form').addEventListener('submit', function(e) {
        const pickup = document.getElementById('pickup').value.trim();
        const drop = document.getElementById('drop').value.trim();

        if (!pickup || !drop) {
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