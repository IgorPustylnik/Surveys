document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("signupForm").addEventListener("submit", handleFormSubmit);
});

async function handleFormSubmit(event) {
    event.preventDefault();

    if (!validateForm()) return;

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const errorMessageDiv = document.getElementById("errorMessage");

    errorMessageDiv.style.display = "none";
    errorMessageDiv.textContent = "";

    try {
        const response = await fetch("signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password })
        });

        let result;
        const contentType = response.headers.get("Content-Type");

        if (contentType && contentType.includes("application/json")) {
            result = await response.json();
        } else {
            result = { message: await response.text() };
        }

        if (response.ok) {
            document.cookie = `authToken=${result.token}; path=/; max-age=${60 * 60 * 24 * 14}`;
            window.location.href = "../..";
        } else {
            errorMessageDiv.style.display = "block";
            errorMessageDiv.textContent = result.message || "Registration failed.";
        }
    } catch (error) {
        errorMessageDiv.style.display = "block";
        errorMessageDiv.textContent = "An error occurred. Please try again later.";
    }
}

function validateForm() {
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const confirmPasswordInput = document.getElementById("confirmPassword");

    const usernameError = document.getElementById("usernameError");
    const passwordError = document.getElementById("passwordError");
    const confirmPasswordError = document.getElementById("confirmPasswordError");

    usernameError.innerHTML = "";
    passwordError.innerHTML = "";
    confirmPasswordError.innerHTML = "";

    let isValid = true;

    const username = usernameInput.value;
    if (username.length < 2) {
        usernameError.innerHTML = "The name must be at least 2 characters long";
        isValid = false;
    } else if (!/^[a-zA-Z0-9]+$/.test(username)) {
        usernameError.innerHTML = "The name can only contain latin letters and digits";
        isValid = false;
    }

    const password = passwordInput.value;
    if (password.length < 8) {
        passwordError.innerHTML = "The password must be at least 8 characters long";
        isValid = false;
    } else if (!/[A-Z]/.test(password)) {
        passwordError.innerHTML = "The password must contain at least one uppercase letter";
        isValid = false;
    } else if (!/[0-9]/.test(password)) {
        passwordError.innerHTML = "The password must contain at least one digit";
        isValid = false;
    } else if (!/^[a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+$/.test(password)) {
        passwordError.innerHTML = "The password contains invalid characters";
        isValid = false;
    }

    const confirmPassword = confirmPasswordInput.value;
    if (password !== confirmPassword) {
        confirmPasswordError.innerHTML = "Passwords do not match";
        isValid = false;
    }

    return isValid;
}
