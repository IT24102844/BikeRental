<%@ page import="com.app.bikerental.model.User" %>
<%@ page import="com.app.bikerental.model.Bike" %>
<%@ page import="com.app.bikerental.model.BikeRequest" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"Admin".equals(user.getRole())) {
        response.sendRedirect("login.jsp?error=Unauthorized+access");
        return;
    }
%>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .dashboard-container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 1px solid #ddd;
        }
        .tab {
            padding: 10px 20px;
            cursor: pointer;
            background-color: #f1f1f1;
            border: 1px solid #ddd;
            border-bottom: none;
            border-radius: 5px 5px 0 0;
            margin-right: 5px;
        }
        .tab.active {
            background-color: #4CAF50;
            color: white;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
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
        .action-buttons {
            display: flex;
            gap: 5px;
        }
        .btn {
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
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
        .btn-warning {
            background-color: #ff9800;
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
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 50%;
            border-radius: 5px;
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover {
            color: black;
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <h1>Admin Dashboard</h1>

    <%-- Message display --%>
    <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success">
        <%= request.getParameter("msg") %>
    </div>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
    <div class="alert alert-danger">
        <%= request.getParameter("error") %>
    </div>
    <% } %>

    <div class="tabs">
        <div class="tab active" onclick="openTab(event, 'users')">Users</div>
        <div class="tab" onclick="openTab(event, 'bikes')">Bikes</div>
        <div class="tab" onclick="openTab(event, 'queue')">Request Queue</div>
        <a class="tab" href="viewPayments">Payment History</a>
        <a href="feedback?action=manage" class="btn btn-primary">Manage Feedbacks</a>
    </div>

    <%-- Users Tab --%>
    <div id="users" class="tab-content active">
        <h2>User Management</h2>
        <a href="#" class="btn btn-primary" onclick="openUserModal()">Add New User</a>
        <div style="margin: 10px 0;">
            <label>Filter by Role:</label>
            <select id="roleFilter" onchange="filterUsers()">
                <option value="ALL">All</option>
                <option value="Admin">Admins</option>
                <option value="Driver">Drivers</option>
                <option value="Rider">Riders</option>
            </select>
        </div>
        <table id="usersTable">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% List<User> users = (List<User>) request.getAttribute("users");
                if (users != null && !users.isEmpty()) {
                    for (User user1 : users) { %>
            <tr data-role="<%= user1.getRole() %>">
                <td><%= user1.getId() %></td>
                <td><%= user1.getName() %></td>
                <td><%= user1.getUsername() %></td>
                <td><%= user1.getEmail() %></td>
                <td><%= user1.getRole() %></td>
                <td class="action-buttons">
                    <a href="#" class="btn btn-secondary"
                       onclick="openUserModal('<%= user1.getId() %>', '<%= user1.getName() %>',
                               '<%= user1.getUsername() %>', '<%= user1.getEmail() %>', '<%= user1.getRole() %>')">
                        Edit
                    </a>
                    <% if (!user1.getRole().equals("Admin")) { %>
                    <a href="admin?action=deleteUser&userId=<%= user1.getId() %>"
                       class="btn btn-danger"
                       onclick="return confirm('Are you sure you want to delete this user?')">
                        Delete
                    </a>
                    <% } else { %>
                    <span class="btn btn-danger disabled">Delete</span>
                    <% } %>
                </td>
            </tr>
            <%   }
            } else { %>
            <tr>
                <td colspan="6">No users found</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <%-- Bikes Tab --%>
    <div id="bikes" class="tab-content">
        <h2>Bike Management</h2>
        <a href="#" class="btn btn-primary" onclick="openBikeModal()">Add New Bike</a>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Model</th>
                <th>Type</th>
                <th>Condition</th>
                <th>Driver</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% List<Bike> bikes = (List<Bike>) request.getAttribute("bikes");
                if (bikes != null && !bikes.isEmpty()) {
                    for (Bike bike : bikes) { %>
            <tr>
                <td><%= bike.getId() %></td>
                <td><%= bike.getModel() %></td>
                <td><%= bike.getType() %></td>
                <td><%= bike.getCondition() %></td>
                <td><%= bike.getDriverId() %></td>
                <td><%= bike.getAvailability() %></td>
                <td class="action-buttons">
                    <a href="#" class="btn btn-secondary" onclick="openBikeModal('<%= bike.getId() %>', '<%= bike.getModel() %>', '<%= bike.getType() %>', '<%= bike.getCondition() %>', '<%= bike.getDriverId() %>', '<%= bike.getAvailability() %>')">Edit</a>
                    <a href="admin?action=deleteBike&bikeId=<%= bike.getId() %>" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this bike?')">Delete</a>
                </td>
            </tr>
            <%   }
            } else { %>
            <tr>
                <td colspan="7">No bikes found</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <%-- Queue Tab --%>
    <div id="queue" class="tab-content">
        <h2>Request Queue</h2>
        <div class="action-buttons" style="margin-bottom: 15px;">
            <a href="admin?action=processNextRequest" class="btn btn-primary">Process Next Request</a>
            <a href="admin?action=clearQueue" class="btn btn-danger" onclick="return confirm('Are you sure you want to clear the queue?')">Clear Queue</a>
        </div>
        <table>
            <thead>
            <tr>
                <th>Request ID</th>
                <th>User ID</th>
                <th>Bike ID</th>
                <th>Bike Type</th>
                <th>Pickup</th>
                <th>Drop</th>
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
                <td><%= request1.getStatus() %></td>
                <td class="action-buttons">
                    <% if (request1.getStatus().equals("PENDING")) { %>
                    <a href="admin?action=processRequest&requestId=<%= request1.getRequestId() %>" class="btn btn-primary">Process</a>
                    <a href="admin?action=cancelRequest&requestId=<%= request1.getRequestId() %>" class="btn btn-danger">Cancel</a>
                    <% } else { %>
                    <span>No actions</span>
                    <% } %>
                </td>
            </tr>
            <%   }
            } else { %>
            <tr>
                <td colspan="9">No requests in queue</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<%-- User Modal --%>
<div id="userModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeUserModal()">&times;</span>
        <h2 id="userModalTitle">Add New User</h2>
        <form id="userForm" action="admin" method="post">
            <input type="hidden" name="action" id="userAction" value="addUser">
            <input type="hidden" name="userId" id="userId">

            <div class="form-group">
                <label for="name">Full Name:</label>
                <input type="text" id="name" name="name" required>
            </div>

            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" id="password">
            </div>

            <div class="form-group">
                <label for="role">Role:</label>
                <select id="role" name="role" required>
                    <option value="Rider">Rider</option>
                    <option value="Driver">Driver</option>
                    <option value="Admin">Admin</option>
                </select>
            </div>

            <input type="submit" class="btn btn-primary" value="Save">
        </form>
    </div>
</div>

<%-- Bike Modal --%>
<div id="bikeModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeBikeModal()">&times;</span>
        <h2 id="bikeModalTitle">Add New Bike</h2>
        <form id="bikeForm" action="admin" method="post">
            <input type="hidden" name="action" id="bikeAction" value="addBike">
            <input type="hidden" name="bikeId" id="bikeId">

            <div class="form-group">
                <label for="model">Model:</label>
                <input type="text" id="model" name="model" required>
            </div>

            <div class="form-group">
                <label for="type">Type:</label>
                <select id="type" name="type" required>
                    <option value="Electric">Electric</option>
                    <option value="Manual">Manual</option>
                </select>
            </div>

            <div class="form-group">
                <label for="condition">Condition:</label>
                <input type="text" id="condition" name="condition" required>
            </div>

            <div class="form-group">
                <label for="driverId">Driver ID:</label>
                <input type="text" id="driverId" name="driverId">
            </div>

            <div class="form-group">
                <label for="availability">Availability:</label>
                <select id="availability" name="availability" required>
                    <option value="Available">Available</option>
                    <option value="Unavailable">Unavailable</option>
                    <option value="Maintenance">Maintenance</option>
                </select>
            </div>

            <input type="submit" class="btn btn-primary" value="Save">
        </form>
    </div>
</div>

<script>
    // Tab functionality
    function openTab(evt, tabName) {
        var i, tabcontent, tablinks;

        tabcontent = document.getElementsByClassName("tab-content");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].className = tabcontent[i].className.replace(" active", "");
        }

        tablinks = document.getElementsByClassName("tab");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }

        document.getElementById(tabName).className += " active";
        evt.currentTarget.className += " active";
    }

    // User modal functions
    // In admin_dashboard.jsp
    function openUserModal(id, name, username, email, role) {
        const modal = document.getElementById("userModal");
        const title = document.getElementById("userModalTitle");

        if (id) {
            title.textContent = "Edit User";
            document.getElementById("userAction").value = "updateUser";
            document.getElementById("userId").value = id;
            document.getElementById("name").value = name;
            document.getElementById("username").value = username;
            document.getElementById("email").value = email;
            document.getElementById("role").value = role;
            document.getElementById("password").required = false;
        } else {
            title.textContent = "Add New User";
            document.getElementById("userAction").value = "addUser";
            document.getElementById("userId").value = "";
            document.getElementById("userForm").reset();
            document.getElementById("password").required = true;
        }

        modal.style.display = "block";
    }

    function filterUsers() {
        const role = document.getElementById("roleFilter").value;
        const rows = document.querySelectorAll("#usersTable tbody tr");

        rows.forEach(row => {
            if (role === "ALL" || row.getAttribute("data-role") === role) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    }

    function closeUserModal() {
        document.getElementById("userModal").style.display = "none";
    }

    // Bike modal functions
    function openBikeModal(id, model, type, condition, driverId, availability) {
        const modal = document.getElementById("bikeModal");
        const title = document.getElementById("bikeModalTitle");

        if (id) {
            title.textContent = "Edit Bike";
            document.getElementById("bikeAction").value = "updateBike";
            document.getElementById("bikeId").value = id;
            document.getElementById("model").value = model;
            document.getElementById("type").value = type;
            document.getElementById("condition").value = condition;
            document.getElementById("driverId").value = driverId;
            document.getElementById("availability").value = availability;
        } else {
            title.textContent = "Add New Bike";
            document.getElementById("bikeAction").value = "addBike";
            document.getElementById("bikeId").value = "";
            document.getElementById("bikeForm").reset();
        }

        modal.style.display = "block";
    }

    function closeBikeModal() {
        document.getElementById("bikeModal").style.display = "none";
    }

    // Close modals when clicking outside
    window.onclick = function(event) {
        const userModal = document.getElementById("userModal");
        const bikeModal = document.getElementById("bikeModal");

        if (event.target == userModal) {
            userModal.style.display = "none";
        }
        if (event.target == bikeModal) {
            bikeModal.style.display = "none";
        }
    }
</script>
</body>
</html>