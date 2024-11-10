document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const errorMessageDiv = document.getElementById("errorMessage");

    errorMessageDiv.style.display = "none";
    errorMessageDiv.textContent = "";

    try {
        const response = await fetch("login", {
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
            window.location.href = "/";
        } else {
            errorMessageDiv.style.display = "block";
            errorMessageDiv.textContent = result.message || "Login failed.";
        }
    } catch (error) {
        errorMessageDiv.style.display = "block";
        errorMessageDiv.textContent = "An error occurred. Please try again later.";
    }
});

function getCookie(name) {
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        cookie = cookie.trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring((name + '=').length);
        }
    }
    return null;
}