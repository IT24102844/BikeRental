<%@ page import="java.util.List" %>
<%@ page import="com.app.bikerental.model.Bike" %>
<%@ page session="true" %>

<%
  // Check logged-in user and role (must be Driver)
  Object userObj = session.getAttribute("user");
  if (userObj == null || !userObj.getClass().getSimpleName().equals("Driver")) {
    response.sendRedirect("login.jsp");
    return;
  }

  List<Bike> bikes = (List<Bike>) request.getAttribute("bikes");
  if (bikes == null) bikes = new java.util.ArrayList<>();

  String msg = request.getParameter("msg");
  String error = request.getParameter("error");
%>

<html>
<head><title>Driver Dashboard - Bike Management</title></head>
<body>

<h2>Welcome, Driver <%= ((com.app.bikerental.model.User) userObj).getName() %></h2>

<% if (msg != null) { %>
<p style="color:green;"><%= msg %></p>
<% } %>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<h3>Your Bikes</h3>
<form method="get" action="bike">
  <input type="hidden" name="action" value="sortAvailability" />
  <button type="submit">Sort by Availability</button>
</form>


<table border="1" cellpadding="5">
  <tr>
    <th>ID</th><th>Model</th><th>Condition</th><th>Available</th><th>Type</th><th>Actions</th>
  </tr>
  <% for (Bike b : bikes) { %>
  <tr>
    <td><%= b.getId() %></td>
    <td><%= b.getModel() %></td>
    <td><%= b.getCondition() %></td>
    <td><%= b.isAvailable() ? "Yes" : "No" %></td>
    <td><%= b.getType() %></td>
    <td>
      <a href="bike?action=edit&id=<%= b.getId() %>">Edit</a> |
      <a href="bike?action=delete&id=<%= b.getId() %>" onclick="return confirm('Delete this bike?');">Delete</a>
    </td>
  </tr>
  <% } %>
</table>

<br>
<a href="add_bike.jsp">Add New Bike</a> |
<a href="user?action=logout">Logout</a>

</body>
</html>
