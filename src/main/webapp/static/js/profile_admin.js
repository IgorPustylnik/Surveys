document.addEventListener("DOMContentLoaded", function () {
    const userId = document.querySelector('input[name="userId"]').value;
    const roleSelect = document.getElementById("roleSelect");
    const updateButton = document.getElementById("updateRoleButton");

    if (roleSelect != null) {
        const originalRole = roleSelect.value;

        roleSelect.addEventListener("change", function () {
            updateButton.disabled = roleSelect.value === originalRole;
        });

        document.getElementById("updateRoleForm").addEventListener("submit", async function (e) {
            e.preventDefault();
            const role = roleSelect.value;

            try {
                const response = await fetch(`/user/${userId}/update-role?role=${role}`, {
                    method: "POST"
                });

                if (response.ok) {
                    const result = await response.text();
                    displayMessage("success", result.message || "Role updated successfully.");
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                } else {
                    const errorText = await response.text();
                    displayMessage("danger", `Failed to update role: ${errorText}`);
                }
            } catch (error) {
                displayMessage("danger", "An error occurred while updating role.");
            }
        });
    }

    document.getElementById("deleteUserForm").addEventListener("submit", async function (e) {
        e.preventDefault();
        try {
            const response = await fetch(`/user/${userId}/delete`, {
                method: "POST"
            });

            if (response.ok) {
                const result = await response.text();
                displayMessage("success", result.message || "User deleted successfully.");
                setTimeout(() => {
                    window.location.href = "/users";
                }, 1000);
            } else {
                const errorText = await response.text();
                displayMessage("danger", `Failed to delete user: ${errorText}`);
            }
        } catch (error) {
            displayMessage("danger", "An error occurred while deleting user.");
        }
    });

    document.getElementById("banUserForm").addEventListener("submit", async function (e) {
        e.preventDefault();
        try {
            const response = await fetch(`/user/${userId}/toggle-ban`, {
                method: "POST"
            });

            if (response.ok) {
                const result = await response.text();
                displayMessage("success", result.message || "User banned successfully.");
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
            } else {
                const errorText = await response.text();
                displayMessage("danger", `Failed to ban user: ${errorText}`);
            }
        } catch (error) {
            displayMessage("danger", "An error occurred while banning user.");
        }
    });
});

function displayMessage(type, message) {
    const messageDiv = document.createElement("div");
    messageDiv.className = `alert alert-${type}`;
    messageDiv.textContent = message;

    Object.assign(messageDiv.style, {
        position: "fixed",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        zIndex: "1000",
        width: "60%",
        maxWidth: "500px",
        textAlign: "center",
        opacity: "0.8",
        padding: "15px",
    });

    document.body.appendChild(messageDiv);
    setTimeout(() => messageDiv.remove(), 3000);
}
