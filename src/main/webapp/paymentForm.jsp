<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card {
            max-width: 500px;
            margin: auto;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            text-align: center;
            font-size: 1.5rem;
        }
        .btn-custom {
            background-color: #007bff;
            color: white;
        }
        .btn-custom:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body class="bg-light">
<div class="container my-5">
    <div class="card">
        <div class="card-header bg-primary text-white">
            <h3>Payment Details</h3>
        </div>
        <div class="card-body">
            <form action="processRidePayment" method="post">
                <input type="hidden" name="rideId" value="${rideForPayment.rideId}">

                <!-- Name -->
                <div class="mb-3">
                    <label for="name" class="form-label">Full Name</label>
                    <input type="text" name="name" class="form-control" id="name" required>
                </div>

                <!-- Card Number -->
                <div class="mb-3">
                    <label for="cardNumber" class="form-label">Card Number</label>
                    <input type="text" name="cardNumber" class="form-control" id="cardNumber"
                           pattern="\d{16}" required maxlength="16"
                           placeholder="1234567812345678"
                           title="Card number must be exactly 16 digits">
                </div>

                <!-- Fare Amount -->
                <div class="mb-3">
                    <label for="amount" class="form-label">Fare Amount (LKR)</label>
                    <input type="number" step="0.01" class="form-control" id="amount"
                           name="amount" required min="1">
                </div>

                <!-- Expiry Date and CVV -->
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="expiryDate" class="form-label">Expiry Date</label>
                        <input type="month" name="expiryDate" class="form-control" id="expiryDate" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cvv" class="form-label">CVV</label>
                        <input type="text" name="cvv" class="form-control" id="cvv" required
                               maxlength="3" placeholder="123" pattern="\d{3}">
                    </div>
                </div>

                <!-- Submit Button -->
                <div class="d-flex justify-content-center">
                    <button type="submit" class="btn btn-primary btn-lg">Complete Payment</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>