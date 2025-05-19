<%@ page import="com.app.bikerental.model.Ride" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Ride ride = (Ride) request.getAttribute("ride"); %>
<html>
<head>
    <title>Submit Feedback</title>
    <style>
        .feedback-form {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background: #f9f9f9;
        }
        .rating-stars {
            font-size: 2em;
            margin: 10px 0;
        }
        .star {
            color: #ddd;
            cursor: pointer;
        }
        .star.selected {
            color: gold;
        }
    </style>
</head>
<body>
<div class="feedback-form">
    <h2>Submit Feedback for Ride <%= ride.getRideId() %></h2>

    <form action="feedback" method="post">
        <input type="hidden" name="action" value="submit">
        <input type="hidden" name="rideId" value="<%= ride.getRideId() %>">
        <input type="hidden" name="driverId" value="<%= ride.getDriverId() %>">

        <div class="form-group">
            <label>Driver:</label>
            <p><%= ride.getDriverId() %></p>
        </div>

        <div class="form-group">
            <label>Rating:</label>
            <div class="rating-stars">
                <% for (int i=1; i<=5; i++) { %>
                <span class="star" data-rating="<%= i %>">★</span>
                <% } %>
                <input type="hidden" name="rating" id="ratingValue" required>
            </div>
        </div>

        <div class="form-group">
            <label for="comment">Comments:</label>
            <textarea id="comment" name="comment" rows="4" required></textarea>
        </div>

        <button type="submit">Submit Feedback</button>
    </form>
</div>

<script>
    document.querySelectorAll('.star').forEach(star => {
        star.addEventListener('click', function() {
            const rating = this.getAttribute('data-rating');
            document.getElementById('ratingValue').value = rating;

            // Update star display
            document.querySelectorAll('.star').forEach(s => {
                if (s.getAttribute('data-rating') <= rating) {
                    s.classList.add('selected');
                } else {
                    s.classList.remove('selected');
                }
            });
        });
    });
</script>
</body>
</html>