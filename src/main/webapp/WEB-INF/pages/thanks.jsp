<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thank you</title>
    <%@include file="../templates/resources.jsp" %>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>

<div class="container mt-5">
    <div class="alert alert-success text-center p-5 shadow-sm rounded" role="alert">
        <h1 class="display-3 fw-bold text-success">
            <i class="bi bi-check-circle-fill"></i> Thank You!
        </h1>
        <h4 class="fw-semibold mb-4">We appreciate you for taking this survey.</h4>
        <p class="lead">Your answers have been successfully submitted.</p>

        <p class="mt-4">
            <a href="/" class="btn btn-primary btn-lg">
                <i class="bi bi-house-door-fill"></i> Return to Home
            </a>
        </p>
    </div>
</div>

</body>
</html>
