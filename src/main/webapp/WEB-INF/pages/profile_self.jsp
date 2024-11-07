<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My profile</title>
    <%@include file="../templates/resources.jsp"%>
</head>
<body>

<%@ include file="../templates/navbar.jsp"%>

<div class="container mt-4">
    <h2>Profile</h2>

    <!-- Информация о пользователе -->
    <div class="card mt-3">
        <div class="card-body">
            <h5 class="card-title">User information</h5>
            <p class="card-text"><strong>ID:</strong> <%= user.getId() %></p>
            <p class="card-text"><strong>Name:</strong> <%= user.getName() %></p>
            <p class="card-text"><strong>Role:</strong> <%= user.getRole() %></p>
        </div>
    </div>

    <!-- Кнопка смены пароля -->
    <div class="mt-3">
        <a href="/change-password" class="btn btn-primary">Change Password</a>
    </div>
</div>
</body>
</html>