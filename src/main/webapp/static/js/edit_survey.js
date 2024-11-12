document.addEventListener("DOMContentLoaded", function () {
    const surveyCategory = document.getElementById('surveyCategory');
    const customCategory = document.getElementById('customCategory');
    const customCategoryContainer = document.getElementById('customCategoryContainer');
    const questionsContainer = document.getElementById("questionsContainer");
    const addQuestionButton = document.getElementById("addQuestionButton");
    const editSurveyForm = document.getElementById('editSurveyForm');

    surveyCategory.addEventListener('change', function () {
        if (surveyCategory.value === 'CUSTOM') {
            customCategoryContainer.style.display = 'block';
            customCategory.required = true;
        } else {
            customCategoryContainer.style.display = 'none';
            customCategory.required = false;
        }
    });

    questionsContainer.addEventListener('click', function (event) {
        const target = event.target;

        if (target.classList.contains('delete-question')) {
            target.closest('.question-card').remove();
        }

        if (target.classList.contains('add-option-button')) {
            const questionCard = target.closest('.question-card');
            addOption(questionCard);
        }

        if (target.classList.contains('remove-option-button')) {
            target.closest('.option-input').remove();
        }
    });

    addQuestionButton.addEventListener('click', function () {
        const questionCard = document.createElement('div');
        questionCard.classList.add('card', 'mt-3', 'p-3', 'question-card', 'position-relative');
        questionCard.setAttribute("data-question-id", "-1");
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
    });

    function addOption(questionCard) {
        const optionsContainer = questionCard.querySelector('.options-container');
        const optionInput = document.createElement('div');
        optionInput.classList.add('input-group', 'mt-2', 'mb-2', 'option-input');
        optionInput.innerHTML = `
            <input type="text" class="form-control option-text" data-option-id="-1" placeholder="Enter option text" required>
            <button type="button" class="btn btn-danger btn-sm remove-option-button">Remove</button>
        `;
        optionsContainer.appendChild(optionInput);
    }

    editSurveyForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const surveyData = {
            id: document.getElementById("surveyId").value,
            name: document.getElementById("surveyName").value,
            description: document.getElementById("surveyDescription").value,
            category: surveyCategory.value === 'CUSTOM' ? customCategory.value : surveyCategory.value,
            questions: []
        };

        const questionCards = questionsContainer.querySelectorAll(".question-card");
        if (questionCards.length === 0) {
            displayMessage("danger", "Survey must contain at least one question");
            return;
        }

        let isValid = true;

        questionCards.forEach(questionCard => {
            const questionData = {
                id: parseInt(questionCard.getAttribute("data-question-id"), 10),
                text: questionCard.querySelector(".question-text").value,
                type: questionCard.querySelector(".question-type").value,
                options: []
            };

            const optionInputs = questionCard.querySelectorAll(".option-text");
            optionInputs.forEach(optionInput => {
                questionData.options.push({
                    id: parseInt(optionInput.getAttribute("data-option-id"), 10),
                    description: optionInput.value
                });
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
            const response = await fetch(`/surveys/edit`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(surveyData)
            });

            let result = {message: await response.text()};

            if (response.ok) {
                displayMessage("success", "Survey saved successfully!");
                setTimeout(() => {
                    window.location.href = `/survey/${document.getElementById("surveyId").value}`;
                }, 1000);
            } else {
                console.log(result.message);
                displayMessage("danger", result.message);
            }
        } catch (error) {
            displayMessage("danger", "Error saving survey: " + error.message);
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