<%@ page import="com.app.bikerental.model.BikeRequest" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bike Request Queue</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .queue-container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
            position: sticky;
            top: 0;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .status-pending { color: #FF9800; font-weight: bold; }
        .status-processing { color: #2196F3; font-weight: bold; }
        .status-completed { color: #4CAF50; font-weight: bold; }
        .status-cancelled { color: #F44336; font-weight: bold; }
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
        .btn-danger {
            background-color: #f44336;
            color: white;
        }
    </style>
</head>
<body>
<div class="queue-container">
    <h1>Bike Request Queue</h1>

    <%-- Action Buttons --%>
    <div class="action-buttons">
        <a href="ride?action=dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>

    <%-- Message display --%>
    <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success">
        <%= request.getParameter("msg") %>
    </div>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
    <div class="alert alert-error">
        <%= request.getParameter("error") %>
    </div>
    <% } %>

    <table>
        <thead>
        <tr>
            <th>Request ID</th>
            <th>User ID</th>
            <th>Bike ID</th>
            <th>Bike Type</th>
            <th>Pickup Location</th>
            <th>Drop Location</th>
            <th>Request Time</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% List<BikeRequest> requests = (List<BikeRequest>) request.getAttribute("requests");
            if (requests != null && !requests.isEmpty()) {
                for (BikeRequest request1 : requests) { %>
        <tr>
            <td><%= request1.getRequestId() %></td>
            <td><%= request1.getUserId() %></td>
            <td><%= request1.getBikeId() %></td>
            <td><%= request1.getBikeType() %></td>
            <td><%= request1.getPickupLocation() %></td>
            <td><%= request1.getDropLocation() %></td>
            <td><%= request1.getRequestTime() %></td>
            <td class="status-<%= request1.getStatus().toLowerCase() %>">
                <%= request1.getStatus() %>
            </td>
            <td>
                <% if (request1.getStatus().equals("PENDING")) { %>
                <form action="ride" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="processRequest">
                    <input type="hidden" name="requestId" value="<%= request.getRequestId() %>">
                    <button type="submit" class="btn btn-primary">Process</button>
                </form>
                <form action="ride" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="cancelRequest">
                    <input type="hidden" name="requestId" value="<%= request.getRequestId() %>">
                    <button type="submit" class="btn btn-danger">Cancel</button>
                </form>
                <% } else if (request1.getStatus().equals("PROCESSING")) { %>
                <span>Processing...</span>
                <% } else { %>
                <span>Completed</span>
                <% } %>
            </td>
        </tr>
        <%   }
        } else { %>
        <tr>
            <td colspan="9">No requests in the queue</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>