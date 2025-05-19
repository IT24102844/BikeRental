<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Successful</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <meta http-equiv="refresh" content="4;URL=ride?action=dashboard">

    <script>
        window.onload = function () {
            // Trigger the bill download when the page loads
            var iframe = document.createElement("iframe");
            iframe.style.display = "none";
            iframe.src = "downloadBill";
            document.body.appendChild(iframe);
        };
    </script>

</head>
<body class="bg-light">
<div class="container my-5 text-center">
    <h2 class="text-success">Thank you, <%= request.getAttribute("name") %>!</h2>

    <%
        Double total = (Double) request.getAttribute("amount");
    %>

    <p>Your payment of  <%= String.format("%.2f", total) %> LKR has been successfully processed.</p>
    <p>You will be redirected to your course dashboard shortly...</p>
</div>
</body>
</html>
