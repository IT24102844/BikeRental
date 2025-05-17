<html>
<head><title>Add Bike</title></head>
<body>
<h2>Add New Bike</h2>

<form method="post" action="bike?action=add">
  Model: <input type="text" name="model" required><br><br>
  Condition: <input type="text" name="condition" required><br><br>
  Available: <input type="checkbox" name="available"><br><br>
  Type:
  <select name="type" required>
    <option value="">Select Type</option>
    <option value="Electric">Electric</option>
    <option value="Manual">Manual</option>
  </select><br><br>

  <button type="submit">Add Bike</button>
</form>

<br><a href="driverDashboard.jsp">Back to Dashboard</a>
</body>
</html>
