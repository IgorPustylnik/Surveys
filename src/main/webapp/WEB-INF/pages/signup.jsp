<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <%@include file="../templates/resources.jsp"%>
    <script src="/js/signup.js"></script>
</head>
<body>

<%@include file="../templates/navbar.jsp"%>

<!-- Page Content -->
<div class="content">
    <div class="container col-md-6 col-lg-4 mt-md-5">
        <h1 class="text-center mb-4">Create an account</h1>

        <!-- Registration Form -->
        <form id="signupForm">
            <div class="form-group mb-3">
                <label for="username" class="form-label">Username:</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="Enter your username" required>
                <div id="usernameError" class="error-message text-danger"></div>
            </div>

            <div class="form-group mb-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Enter your password" required>
                <div id="passwordError" class="form-text error-message text-danger"></div>
            </div>

            <div class="form-group mb-3">
                <label for="confirmPassword" class="form-label">Confirm password:</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirm your password" required>
                <div id="confirmPasswordError" class="form-text error-message text-danger"></div>
            </div>

            <div id="errorMessage" class="alert alert-danger" style="display: none;"></div>

            <button type="submit" class="btn btn-primary btn-block w-100">Sign Up</button>
        </form>

        <!-- Login Link -->
        <div class="text-center mt-3">
            <p>Already have an account? <a class="text-decoration-none" href="login">Login</a></p>
        </div>
    </div>
</div>
</body>
</html>