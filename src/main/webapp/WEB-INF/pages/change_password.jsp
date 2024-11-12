<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change password</title>
    <%@include file="../templates/resources.jsp"%>
    <script src="/static/js/change_password.js"></script>
</head>
<body>

<%@ include file="../templates/navbar.jsp"%>

<!-- Page Content -->
<div class="content">
    <div class="container col-md-6 col-lg-4 mt-md-5">
        <h1 class="text-center mb-4">Change password</h1>

        <form id="changePasswordForm">
            <div class="form-group mb-3">
                <label for="oldPassword" class="form-label">Old password:</label>
                <input type="password" class="form-control" id="oldPassword" name="oldPassword"
                       placeholder="Enter old password" required>
            </div>

            <div class="form-group mb-3">
                <label for="newPassword" class="form-label">New password:</label>
                <input type="password" class="form-control" id="newPassword" name="newPassword"
                       placeholder="Enter new password" required>
                <div id="passwordError" class="form-text error-message text-danger"></div>
            </div>

            <div class="form-group mb-3">
                <label for="confirmPassword" class="form-label">Confirm new password:</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                       placeholder="Confirm new password" required>
                <div id="confirmPasswordError" class="form-text error-message text-danger"></div>
            </div>

            <button type="submit" class="btn btn-primary btn-block w-100 mb-3">Change password</button>

            <div id="successMessage" class="alert alert-success" style="display: none;">Password changed successfully!
            </div>
            <div id="errorMessage" class="alert alert-danger" style="display: none;"></div>
        </form>
    </div>
</div>
</body>
</html>