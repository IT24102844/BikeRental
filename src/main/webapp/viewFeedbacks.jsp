<%@ page import="com.app.bikerental.model.Feedback" %>
<%@ page import="java.util.List" %>
<%@ page import="com.app.bikerental.dao.FeedbackDAO" %>
<%@ page import="com.app.bikerental.model.User" %>
<%@ page import="com.app.bikerental.model.Admin" %>
<%@ page import="com.app.bikerental.model.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    boolean isAdmin = user instanceof Admin;
    List<Feedback> feedbacks;

    if (isAdmin) {
        feedbacks = FeedbackDAO.getAllFeedbacks();
    } else if (user instanceof Driver) {
        feedbacks = FeedbackDAO.getFeedbacksByDriver(user.getId());
    } else {
        feedbacks = FeedbackDAO.getFeedbacksByRider(user.getId());
    }
%>
<html>
<head>
    <title><%= isAdmin ? "Manage" : "View" %> Feedbacks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 20px;
        }
        .feedback-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .feedback-table th, .feedback-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        .feedback-table th {
            background-color: #f2f2f2;
            position: sticky;
            top: 0;
        }
        .feedback-table tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .feedback-table tr:hover {
            background-color: #f1f1f1;
        }
        .rating-stars {
            color: gold;
            font-size: 1.2em;
        }
        .action-buttons {
            display: flex;
            gap: 5px;
        }
        .btn {
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
        }
        .btn-view {
            background-color: #2196F3;
            color: white;
        }
        .btn-delete {
            background-color: #f44336;
            color: white;
        }
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
        .empty-message {
            text-align: center;
            padding: 20px;
            color: #666;
        }
        .filter-section {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #e9ecef;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1><%= isAdmin ? "Manage Feedbacks" : "My Feedback History" %></h1>

    <%-- Display messages --%>
    <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success"><%= request.getParameter("msg") %></div>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
    <div class="alert alert-danger"><%= request.getParameter("error") %></div>
    <% } %>

    <%-- Admin filters --%>
    <% if (isAdmin) { %>
    <div class="filter-section">
        <form method="get" action="feedback">
            <input type="hidden" name="action" value="filter">
            <div style="margin-bottom: 10px;">
                <label>Filter by:</label>
                <select name="filterType">
                    <option value="all">All Feedbacks</option>
                    <option value="driver">By Driver</option>
                    <option value="rider">By Rider</option>
                </select>
                <input type="text" name="filterValue" placeholder="Enter ID">
                <button type="submit" class="btn btn-view">Apply Filter</button>
                <a href="feedback" class="btn">Clear Filters</a>
            </div>
        </form>
    </div>
    <% } %>

    <% if (feedbacks.isEmpty()) { %>
    <div class="empty-message">
        No feedbacks found.
    </div>
    <% } else { %>
    <table class="feedback-table">
        <thead>
        <tr>
            <th>ID</th>
            <% if (isAdmin) { %>
            <th>Rider</th>
            <th>Driver</th>
            <% } %>
            <th>Ride ID</th>
            <th>Rating</th>
            <th>Comment</th>
            <th>Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Feedback feedback : feedbacks) { %>
        <tr>
            <td><%= feedback.getId() %></td>
            <% if (isAdmin) { %>
            <td><%= feedback.getRiderId() %></td>
            <td><%= feedback.getDriverId() %></td>
            <% } %>
            <td><%= feedback.getRideId() %></td>
            <td class="rating-stars">
                <% for (int i = 1; i <= 5; i++) { %>
                <%= i <= feedback.getRating() ? "★" : "☆" %>
                <% } %>
                (<%= feedback.getRating() %>/5)
            </td>
            <td><%= feedback.getComment() %></td>
            <td><%= feedback.getTimestamp() %></td>
            <td class="action-buttons">
                <a href="feedback?action=view&id=<%= feedback.getId() %>" class="btn btn-view">View</a>
                <% if (isAdmin || feedback.getRiderId().equals(user.getId())) { %>
                <form action="feedback" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= feedback.getId() %>">
                    <button type="submit" class="btn btn-delete"
                            onclick="return confirm('Are you sure you want to delete this feedback?')">
                        Delete
                    </button>
                </form>
                <% } %>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } %>

    <div style="margin-top: 20px;">
        <a href="admin" class="btn btn-view">Back to Dashboard</a>
    </div>
</div>
</body>
</html>