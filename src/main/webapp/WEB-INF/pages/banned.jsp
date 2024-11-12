<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <%@include file="../templates/resources.jsp" %>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>

<div class="container mt-5">
    <div class="alert alert-danger text-center p-4 shadow-sm rounded" role="alert">
        <h1 class="display-4 fw-bold">You have been banned</h1>
        <p class="lead"></p>

        <p class="mt-3">
            If you think this is a mistake, contact the administrator at
            <a href="mailto:admin@surveys.com" class="text-decoration-underline">admin@surveys.com</a>.
        </p>

        <p class="mt-4">
            <a onclick="handleLogout()" class="btn btn-primary btn-lg">Log out</a>
        </p>
    </div>
</div>
</body>
</html>