document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("changePasswordForm").addEventListener("submit", handlePasswordChangeSubmit);
});

async function handlePasswordChangeSubmit(event) {
    event.preventDefault();

    if (!validatePasswordChangeForm()) return;

    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const errorMessageDiv = document.getElementById("errorMessage");
    const successMessageDiv = document.getElementById("successMessage");

    errorMessageDiv.style.display = "none";
    errorMessageDiv.textContent = "";
    successMessageDiv.textContent = "";

    try {
        const response = await fetch("change-password", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ oldPassword, newPassword })
        });

        const result = { message: await response.text() };

        if (response.ok) {
            successMessageDiv.style.display = "block";
            successMessageDiv.textContent = "Password was changed successfully!";
            document.getElementById("changePasswordForm").reset();
        } else {
            errorMessageDiv.style.display = "block";
            errorMessageDiv.textContent = result.message || "Failed to change password.";
        }
    } catch (error) {
        errorMessageDiv.style.display = "block";
        errorMessageDiv.textContent = "An error occurred. Please try again later.";
    }
}

function validatePasswordChangeForm() {
    const newPasswordInput = document.getElementById("newPassword");
    const confirmPasswordInput = document.getElementById("confirmPassword");

    const passwordError = document.getElementById("passwordError");
    const confirmPasswordError = document.getElementById("confirmPasswordError");

    passwordError.innerHTML = "";
    confirmPasswordError.innerHTML = "";

    let isValid = true;

    const newPassword = newPasswordInput.value;
    if (newPassword.length < 8) {
        passwordError.innerHTML = "The password must be at least 8 characters long";
        isValid = false;
    } else if (!/[A-Z]/.test(newPassword)) {
        passwordError.innerHTML = "The password must contain at least one uppercase letter";
        isValid = false;
    } else if (!/[0-9]/.test(newPassword)) {
        passwordError.innerHTML = "The password must contain at least one digit";
        isValid = false;
    } else if (!/^[a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+$/.test(newPassword)) {
        passwordError.innerHTML = "The password contains invalid characters";
        isValid = false;
    }

    const confirmPassword = confirmPasswordInput.value;
    if (newPassword !== confirmPassword) {
        confirmPasswordError.innerHTML = "Passwords do not match";
        isValid = false;
    }

    return isValid;
}
