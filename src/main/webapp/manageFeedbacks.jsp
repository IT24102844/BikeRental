<%@ page import="com.app.bikerental.model.Feedback" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<Feedback> feedbacks = (List<Feedback>) request.getAttribute("feedbacks"); %>
<html>
<head>
    <title>Manage Feedbacks</title>
    <style>
        .feedback-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .feedback-table th, .feedback-table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        .feedback-table th {
            background-color: #f2f2f2;
        }
        .rating-stars {
            color: gold;
        }
        .action-btns {
            display: flex;
            gap: 5px;
        }
    </style>
</head>
<body>
<h1>Feedback Management</h1>

<% if (request.getParameter("msg") != null) { %>
<div style="color:green;"><%= request.getParameter("msg") %></div>
<% } %>
<% if (request.getParameter("error") != null) { %>
<div style="color:red;"><%= request.getParameter("error") %></div>
<% } %>

<table class="feedback-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Ride ID</th>
        <th>Rider</th>
        <th>Driver</th>
        <th>Rating</th>
        <th>Comment</th>
        <th>Date</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% for (Feedback fb : feedbacks) { %>
    <tr>
        <td><%= fb.getId() %></td>
        <td><%= fb.getRideId() %></td>
        <td><%= fb.getRiderId() %></td>
        <td><%= fb.getDriverId() %></td>
        <td class="rating-stars">
            <% for (int i=0; i<5; i++) { %>
            <%= i < fb.getRating() ? "★" : "☆" %>
            <% } %>
        </td>
        <td><%= fb.getComment() %></td>
        <td><%= fb.getTimestamp() %></td>
        <td class="action-btns">
            <a href="feedback?action=edit&id=<%= fb.getId() %>">Edit</a>
            <a href="feedback?action=delete&id=<%= fb.getId() %>"
               onclick="return confirm('Delete this feedback?')">Delete</a>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>