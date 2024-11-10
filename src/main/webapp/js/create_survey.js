document.addEventListener('DOMContentLoaded', function () {
    const surveyCategory = document.getElementById('surveyCategory');
    const customCategoryContainer = document.getElementById('customCategoryContainer');
    const customCategory = document.getElementById('customCategory');
    const questionsContainer = document.getElementById('questionsContainer');
    const addQuestionButton = document.getElementById('addQuestionButton');
    const createSurveyForm = document.getElementById('createSurveyForm');

    surveyCategory.addEventListener('change', function () {
        if (surveyCategory.value === 'CUSTOM') {
            customCategoryContainer.style.display = 'block';
            customCategory.required = true;
        } else {
            customCategoryContainer.style.display = 'none';
            customCategory.required = false;
        }
    });

    addQuestionButton.addEventListener('click', function () {
        const questionId = `question${Date.now()}`;
        const questionCard = document.createElement('div');
        questionCard.classList.add('card', 'mt-3', 'p-3', 'question-card', 'position-relative');
        questionCard.setAttribute('data-question-id', questionId);
        questionCard.innerHTML = `
        <button type="button" class="delete-question btn btn-sm btn-outline-danger col position-absolute top-0 end-0 mt-2 me-2" aria-label="Close"><i class="bi bi-dash-lg"></i></button>
        <div class="mb-3">
            <label class="form-label">Question Text</label>
            <input type="text" class="form-control question-text" placeholder="Enter question text" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Question Type</label>
            <select class="form-select question-type" required>
                <option value="SINGLE_CHOICE">Single Choice</option>
                <option value="MULTIPLE_CHOICE">Multiple Choice</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Answer Options</label>
            <div class="options-container"></div>
            <button type="button" class="btn btn-outline-success btn-sm add-option-button"><i class="bi-plus-lg"></i></button>
        </div>
    `;
        questionsContainer.appendChild(questionCard);

        questionCard.scrollIntoView({behavior: 'smooth', block: 'start'});

        questionCard.querySelector('.add-option-button').addEventListener('click', function () {
            addOption(questionCard);
        });
        questionCard.querySelector('.delete-question').addEventListener('click', function () {
            questionCard.remove();
        });
    });


    function addOption(questionCard) {
        const optionsContainer = questionCard.querySelector('.options-container');
        const optionInput = document.createElement('div');
        optionInput.classList.add('input-group', 'mt-2', 'mb-2', 'option-input');
        optionInput.innerHTML = `
                <input type="text" class="form-control option-text" placeholder="Enter option text" required>
                <button type="button" class="btn btn-danger btn-sm remove-option-button">Remove</button>
            `;
        optionsContainer.appendChild(optionInput);

        optionInput.querySelector('.remove-option-button').addEventListener('click', function () {
            optionInput.remove();
        });
    }

    createSurveyForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const surveyData = {
            name: document.getElementById('surveyName').value,
            description: document.getElementById('surveyDescription').value,
            category: surveyCategory.value === 'CUSTOM' ? customCategory.value : surveyCategory.value,
            questions: []
        };

        const questionCards = document.querySelectorAll('.question-card');
        if (questionCards.length === 0) {
            displayMessage("danger", "Survey must contain at least one question");
            return;
        }

        let isValid = true;

        questionCards.forEach(function (questionCard) {
            const questionData = {
                description: questionCard.querySelector('.question-text').value,
                type: questionCard.querySelector('.question-type').value,
                options: []
            };

            questionCard.querySelectorAll('.option-text').forEach(function (option) {
                questionData.options.push(option.value);
            });

            if (questionData.options.length === 0) {
                isValid = false;
                questionCard.scrollIntoView({behavior: 'smooth', block: 'start'});
                displayMessage("danger", "Question must contain at least one option");
                return;
            }

            surveyData.questions.push(questionData);
        });

        if (!isValid) return;

        try {
            const response = await fetch('/surveys/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(surveyData)
            });

            let result;
            const contentType = response.headers.get("Content-Type");

            if (contentType && contentType.includes("application/json")) {
                result = await response.json();
            } else {
                result = { message: await response.text() };
            }

            if (response.ok) {
                displayMessage("success", "Survey created successfully!");
                createSurveyForm.reset();
                customCategoryContainer.style.display = 'none';
                setTimeout(() => {
                    window.location.href = `/survey/${result.surveyId}`;
                }, 1000);
            } else {
                displayMessage("danger", result.message);
            }
        } catch (error) {
            displayMessage("danger", "Error creating survey: " + error.message);
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