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
<% Integer errorCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code"); %>

<div class="container mt-5">
    <div class="alert alert-danger text-center p-4 shadow-sm rounded" role="alert">
        <h1 class="display-4 fw-bold">Error <%=errorCode%></h1>
        <p class="lead"><%= request.getAttribute("jakarta.servlet.error.message") %></p>

        <% if (errorCode != null && errorCode >= 500) { %>
        <p class="mt-3">
            If this issue persists, please contact the administrator at
            <a href="mailto:admin@surveys.com" class="text-decoration-underline">admin@surveys.com</a>.
        </p>
        <% } %>

        <p class="mt-4">
            <a href="/" class="btn btn-primary btn-lg">
                <i class="bi bi-house-door-fill"></i> Return to Home
            </a>
        </p>
    </div>
</div>
</body>
</html>
