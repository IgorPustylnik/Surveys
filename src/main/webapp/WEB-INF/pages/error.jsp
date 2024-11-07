<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <%@include file="../templates/resources.jsp"%>
</head>
<body>

<%@include file="../templates/navbar.jsp"%>

<div class="container mt-4">
    <div class="alert alert-danger text-center" role="alert">
        <h4 class="alert-heading">Error</h4>
        <p><%= request.getAttribute("errorMessage") %></p>
    </div>
</div>
</body>
</html>
