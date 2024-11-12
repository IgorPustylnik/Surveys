<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <%@include file="../templates/resources.jsp"%>
</head>
<body>

<%@include file="../templates/navbar.jsp"%>

<!-- Page Content -->
<div class="content">
    <div class="container col-md-6 col-lg-4 mt-md-5">
        <h1 class="text-center mb-4">Login</h1>

        <!-- Login Form -->
        <form id="loginForm">
            <div class="form-group mb-3">
                <label for="username" class="form-label">Username:</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="Enter your username"
                       required>
            </div>

            <div class="form-group mb-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="Enter your password" required>
            </div>

            <div id="errorMessage" class="alert alert-danger" style="display: none;"></div>

            <button type="submit" class="btn btn-primary btn-block w-100">Login</button>
        </form>


        <!-- Register Link -->
        <div class="text-center mt-3">
            <p>Don't have an account? <a class="text-decoration-none" href="signup">Sign Up</a></p>
        </div>
    </div>
</div>
<script src="/static/js/login.js"></script>
</html>