<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Bike Rental</title>
</head>
<body>
<h2>User Login</h2>

<%
    String msg = request.getParameter("msg");
    String error = (String) request.getAttribute("error");
%>

<% if (msg != null) { %>
<p style="color:green;"><%= msg %></p>
<% } %>

<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<form method="post" action="user?action=login">
    Username: <input type="text" name="username" required><br><br>
    Password: <input type="password" name="password" required><br><br>

    <button type="submit">Login</button>
</form>

<p>Don't have an account? <a href="register.jsp">Register here</a>.</p>
</body>
</html>
