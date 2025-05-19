<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .no-records {
            min-height: 200px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body class="bg-light">
<div class="container my-5">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h2 class="mb-0">Payment History</h2>
        </div>
        <div class="card-body">
            <%
                List<String> list = (List<String>) request.getAttribute("paymentList");
                if (list == null || list.isEmpty()) {
            %>
            <div class="no-records">
                <div class="text-center">
                    <i class="bi bi-receipt" style="font-size: 3rem; color: #6c757d;"></i>
                    <h4 class="mt-3 text-muted">No payment records found</h4>
                    <a href="home" class="btn btn-primary mt-3">Back to Home</a>
                </div>
            </div>
            <%
            } else {
                for (String record : list) {
                    String last4 = "N/A";

                    try {
                        int cardIndex = record.indexOf("Card: ****");
                        if (cardIndex >= 0) {
                            String remaining = record.substring(cardIndex + 10);
                            int commaIndex = remaining.indexOf(',');
                            if (commaIndex > 0) {
                                last4 = remaining.substring(0, commaIndex).trim();
                            } else {
                                last4 = remaining.trim();
                            }

                            if (!last4.matches("\\d{4}")) {
                                last4 = "N/A";
                            }
                        }
                    } catch (Exception e) {
                        last4 = "N/A";
                    }
            %>
            <div class="list-group-item d-flex justify-content-between align-items-center mb-2">
                <div class="payment-info">
                    <%= record %>
                </div>
                <form action="deletePayment" method="post" class="ms-2">
                    <input type="hidden" name="cardLast4" value="<%= last4 %>">
                    <button type="submit" class="btn btn-danger btn-sm">
                        Delete
                    </button>
                </form>
            </div>
            <%
                    }
                }
            %>
        </div>
        <div class="card-footer text-center">
            <a href="admin" class="btn btn-secondary">Back to Home</a>
        </div>
    </div>
</div>

<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</body>
</html>