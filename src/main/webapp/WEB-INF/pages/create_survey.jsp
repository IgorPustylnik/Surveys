<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Survey</title>
    <%@include file="../templates/resources.jsp" %>
    <script src="/static/js/create_survey.js"></script>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>

<div class="container mt-4">
    <h2>Create a survey</h2>

    <form id="createSurveyForm">
        <div class="mb-3">
            <label for="surveyName" class="form-label">Survey name</label>
            <input type="text" class="form-control" id="surveyName" name="name" placeholder="Enter survey name"
                   required>
        </div>

        <div class="mb-3">
            <label for="surveyDescription" class="form-label">Survey description</label>
            <textarea class="form-control" id="surveyDescription" name="description" rows="3"
                      placeholder="Enter survey description"></textarea>
        </div>

        <div class="mb-3">
            <label for="surveyCategory" class="form-label">Category</label>
            <select class="form-select" id="surveyCategory" name="category">
                <option value="">Select category</option>
                <% List<Category> categories = (List<Category>) request.getAttribute("categories");
                    for (Category category : categories) { %>
                <option value="<%=category.getName()%>"><%=category.getName()%></option>
                <% } %>
                <option value="CUSTOM">Custom</option>
            </select>
        </div>

        <div class="mb-3" id="customCategoryContainer" style="display: none;">
            <label for="customCategory" class="form-label">Custom category name</label>
            <input type="text" class="form-control" id="customCategory" name="customCategory"
                   placeholder="Enter custom category name">
        </div>

        <h5>Questions</h5>
        <div id="questionsContainer"></div>
        <button type="button" class="btn btn-outline-success btn-sm mt-2" id="addQuestionButton"><i
                class="bi-plus-lg"></i></button>

        <div class="mt-4 mb-4">
            <button type="submit" class="btn btn-primary" id="saveSurveyButton">Save survey</button>
        </div>
    </form>
</div>

</body>
</html>
