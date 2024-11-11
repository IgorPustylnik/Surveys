async function navigateToQuestion(event, targetQuestionIndex) {
    event.preventDefault();

    const options = [];
    const form = document.getElementById('surveyForm');
    const optionsDiv = document.querySelectorAll(`input[name="question"]:checked`);

    optionsDiv.forEach(option => {
        options.push(option.value);
    });

    if (options.length !== 0) {
        const questionOptions = form.querySelectorAll(`[name="question"]`);
        options.push(...Array.from(questionOptions).filter(option => option.checked).map(option => parseInt(option.value)));

        const data = {options: options};

        await submitAnswer(event, data);
    }

    const url = new URL(window.location);
    url.searchParams.set('question', targetQuestionIndex);
    window.location.href = url.toString();
}

async function nextQuestion(event, questionIndex) {
    event.preventDefault();

    const success = await submitAnswer(event);
    if (success) {
        const url = new URL(window.location);
        url.searchParams.set('question', questionIndex + 1);
        window.location.href = url.toString();
    }
}

async function submitAnswer(event) {
    event.preventDefault();

    const options = [];
    const form = document.getElementById('surveyForm');
    const sessionId = form.getAttribute("data-session-id");
    const optionsDiv = document.querySelectorAll(`input[name="question"]:checked`);

    optionsDiv.forEach(option => {
        options.push(option.value);
    });

    if (options.length === 0) {
        displayMessage("danger", "Please, choose at least one answer.");
        return;
    }

    const questionOptions = form.querySelectorAll(`[name="question"]`);
    options.push(...Array.from(questionOptions).filter(option => option.checked).map(option => parseInt(option.value)));

    const data = {options: options};

    try {
        const response = await fetch(`/session/${sessionId}/submit`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const message = await response.text();
            displayMessage('danger', message);
            return false;
        }
    } catch (error) {
        displayMessage('danger', 'Error submitting session: ' + error.message);
        return false;
    }

    return true;
}

async function finishSurvey(event, allAnswered) {
    event.preventDefault();
    if (!allAnswered) {
        displayMessage("danger", "Please answer all questions");
        return;
    }

    const submission = await submitAnswer(event);
    if (!submission) {
        return;
    }

    const finished = await sendFinish(event);
    if (finished) {
        const url = new URL(window.location);
        url.searchParams.delete("question");
        window.location.href = url.toString();
    }
}

async function sendFinish(event) {
    event.preventDefault();

    const form = document.getElementById('surveyForm');
    const sessionId = form.getAttribute("data-session-id");

    try {
        const response = await fetch(`/session/${sessionId}/finish`, {
            method: 'POST'
        });

        if (!response.ok) {
            const message = await response.text();
            displayMessage('danger', message);
            return false;
        }
    } catch (error) {
        displayMessage('danger', 'Error submitting session: ' + error.message);
        return false;
    }

    return true;
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