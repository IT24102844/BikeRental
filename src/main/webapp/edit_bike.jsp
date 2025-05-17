<%@ page import="com.app.bikerental.model.Bike" %>
<%
    Bike bike = (Bike) request.getAttribute("bike");
    if (bike == null) {
        response.sendRedirect("driverDashboard.jsp?error=Bike not found");
        return;
    }
%>

<html>
<head><title>Edit Bike</title></head>
<body>
<h2>Edit Bike - <%= bike.getId() %></h2>

<form method="post" action="bike?action=update">
    <input type="hidden" name="id" value="<%= bike.getId() %>">

    Model: <input type="text" name="model" value="<%= bike.getModel() %>" required><br><br>
    Condition: <input type="text" name="condition" value="<%= bike.getCondition() %>" required><br><br>
    Available: <input type="checkbox" name="available" <%= bike.isAvailable() ? "checked" : "" %>><br><br>
    Type: <strong><%= bike.getType() %></strong> (cannot change)<br><br>

    <button type="submit">Update Bike</button>
</form>

<br><a href="driverDashboard.jsp">Back to Dashboard</a>
</body>
</html>
