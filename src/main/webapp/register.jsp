<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register - Bike Rental</title>
</head>
<body>
<h2>User Registration</h2>

<% String error = request.getParameter("error"); %>
<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<form method="post" action="user?action=register">
    Name: <input type="text" name="name" required><br><br>
    Username: <input type="text" name="username" required><br><br>
    Password: <input type="password" name="password" required><br><br>
    Email: <input type="email" name="email" required><br><br>
    Role:
    <select name="role" required>
        <option value="">Select Role</option>
        <option value="Rider">Rider</option>
        <option value="Driver">Driver</option>
        <option value="Admin">Admin</option>
    </select><br><br>

    <button type="submit">Register</button>
</form>

<p>Already have an account? <a href="login.jsp">Login here</a>.</p>
</body>
</html>
