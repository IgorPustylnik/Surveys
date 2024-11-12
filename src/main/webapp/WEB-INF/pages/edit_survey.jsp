<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Survey</title>
    <%@include file="../templates/resources.jsp" %>
    <script src="/static/js/edit_survey.js"></script>
</head>
<body>

<%
    Survey survey = (Survey) request.getAttribute("survey");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>

<%@include file="../templates/navbar.jsp" %>

<div class="container mt-4">
    <h2>Edit Survey</h2>

    <form id="editSurveyForm">
        <input type="hidden" id="surveyId" value="<%= survey.getId() %>">

        <div class="mb-3">
            <label for="surveyName" class="form-label">Survey Name</label>
            <input type="text" class="form-control" id="surveyName" name="name" value="<%= survey.getName() %>" required>
        </div>

        <div class="mb-3">
            <label for="surveyDescription" class="form-label">Survey Description</label>
            <textarea class="form-control" id="surveyDescription" name="description" rows="3"><%= survey.getDescription() %></textarea>
        </div>

        <div class="mb-3">
            <label for="surveyCategory" class="form-label">Category</label>
            <select class="form-select" id="surveyCategory" name="category">
                <option value="">Select category</option>
                <% for (Category category : categories) { %>
                <option value="<%=category.getName()%>" <%= category.getName().equals(survey.getCategoryName()) ? "selected" : "" %>>
                    <%=category.getName()%>
                </option>
                <% } %>
                <option value="CUSTOM" <%= survey.getCategoryId() == null ? "selected" : "" %>>Custom</option>
            </select>
        </div>

        <div class="mb-3" id="customCategoryContainer" style="<%= survey.getCategoryId() == null ? "" : "display:none;" %>">
            <label for="customCategory" class="form-label">Custom Category Name</label>
            <input type="text" class="form-control" id="customCategory" name="customCategory" value="">
        </div>

        <h5>Questions</h5>
        <div id="questionsContainer">
            <% for (Question question : questions) { %>
            <div class="card mt-3 p-3 question-card" data-question-id="<%= question.getId() %>">
                <button type="button" class="delete-question btn btn-sm btn-outline-danger col position-absolute top-0 end-0 mt-2 me-2"><i class="bi bi-dash-lg"></i></button>
                <div class="mb-3">
                    <label class="form-label">Question Text</label>
                    <input type="text" class="form-control question-text" value="<%= question.getText() %>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Question Type</label>
                    <select class="form-select question-type" required>
                        <option value="SINGLE_CHOICE" <%= question.getType() == QuestionType.SINGLE_CHOICE ? "selected" : "" %>>Single Choice</option>
                        <option value="MULTIPLE_CHOICE" <%= question.getType() == QuestionType.MULTIPLE_CHOICE ? "selected" : "" %>>Multiple Choice</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Answer Options</label>
                    <div class="options-container">
                        <% for (Option option : question.getOptions()) { %>
                        <div class="input-group mt-2 mb-2 option-input">
                            <input type="text" class="form-control option-text" data-option-id="<%= option.getId() %>" value="<%= option.getDescription() %>" required>
                            <button type="button" class="btn btn-danger btn-sm remove-option-button">Remove</button>
                        </div>
                        <% } %>
                    </div>
                    <button type="button" class="btn btn-outline-success btn-sm add-option-button"><i class="bi-plus-lg"></i></button>
                </div>
            </div>
            <% } %>
        </div>
        <button type="button" class="btn btn-outline-success btn-sm mt-2" id="addQuestionButton"><i class="bi-plus-lg"></i></button>

        <div class="mt-4 mb-4">
            <button type="submit" class="btn btn-primary" id="saveSurveyButton">Save Changes</button>
        </div>
    </form>
</div>

</body>
</html>