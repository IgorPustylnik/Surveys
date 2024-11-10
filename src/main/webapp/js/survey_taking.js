async function submitAnswers(event) {
    event.preventDefault();

    const form = document.getElementById('surveyForm');
    const questionsAmount = form.getAttribute('data-questions-amount');
    const sessionId = form.getAttribute("data-session-id");
    const options = [];

    for (let i = 1; i <= questionsAmount; i++) {
        const a = `question_${i}`;
        const questionOptions = form.querySelectorAll(`[name="question_${i}"]`);
        let selectedOptions = Array.from(questionOptions)
            .filter(option => option.checked)
            .map(option => parseInt(option.value));

        if (selectedOptions.length === 0) {
            displayMessage('danger', `Please, provide an answer for question ${i}.`);
            return;
        }
        options.push(...selectedOptions);
    }

    const data = {
        options: options
    };

    try {
        const response = await fetch(`/session/${sessionId}/submit`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });

        const message = await response.text();

        if (response.ok) {
            window.location.reload();
        } else {
            displayMessage('danger', message);
        }
    } catch (error) {
        displayMessage('danger', 'Error submitting session: ' + error.message);
    }
}

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