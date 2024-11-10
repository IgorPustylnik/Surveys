document.addEventListener("DOMContentLoaded", function () {
    const startButton = document.getElementById("startSurveyButton");
    const editButton = document.getElementById("editSurveyButton");
    const deleteButton = document.getElementById("deleteSurveyButton");

    const surveyId = startButton.getAttribute("data-survey-id");

    startButton.addEventListener("click", async function (e) {
        e.preventDefault();
        try {
            const response = await fetch(`/survey/${surveyId}/start`, {
                method: "POST"
            });

            if (response.ok) {
                const result = await response.json();
                window.location.href = `/session/${result.sessionId}`;
            } else {
                const errorText = await response.text();
                displayMessage("danger", `Failed to start survey: ${errorText}`);
            }
        } catch (error) {
            displayMessage("danger", "An error occurred while starting survey.");
        }
    });

    editButton.addEventListener("click", function (e) {
        e.preventDefault();
        window.location.href = `/survey/${surveyId}/edit`;
    });

    deleteButton.addEventListener("click", async function (e) {
        e.preventDefault();
        try {
            const response = await fetch(`/survey/${surveyId}/delete`, {
                method: "POST"
            });

            if (response.ok) {
                const result = await response.text();
                displayMessage("success", result.message || "Survey deleted successfully.");
                setTimeout(() => {
                    window.location.href = "/surveys";
                }, 1000);
            } else {
                const errorText = await response.text();
                displayMessage("danger", `Failed to delete survey: ${errorText}`);
            }
        } catch (error) {
            displayMessage("danger", "An error occurred while deleting survey.");
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
