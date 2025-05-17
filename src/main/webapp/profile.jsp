<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.app.bikerental.model.User" %>
<%@ page session="true" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  String msg = request.getParameter("msg");
  String error = request.getParameter("error");
%>

<html>
<head>
  <title>Profile - Bike Rental</title>
</head>
<body>
<h2>Welcome, <%= user.getName() %> (<%= user.getRole() %>)</h2>

<% if (msg != null) { %>
<p style="color:green;"><%= msg %></p>
<% } %>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<h3>Edit Profile</h3>
<form method="post" action="user?action=updateProfile">
  Name: <input type="text" name="name" value="<%= user.getName() %>" required><br><br>
  Email: <input type="email" name="email" value="<%= user.getEmail() %>" required><br><br>
  Password: <input type="password" name="password" placeholder="Leave blank to keep current"><br><br>

  <button type="submit">Update Profile</button>
</form>

<br>
<a href="user?action=logout">Logout</a> |
<a href="user?action=delete&id=<%= user.getId() %>" onclick="return confirm('Delete account?');">Delete Account</a>

</body>
</html>
