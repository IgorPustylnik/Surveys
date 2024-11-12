<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.util.DateUtil" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Survey details</title>
    <%@include file="../templates/resources.jsp" %>
    <script src="/static/js/survey.js"></script>
</head>
<body>

<%@include file="../templates/navbar.jsp"%>

<div class="container mt-4">
    <%
        Survey survey = (Survey) request.getAttribute("survey");
    %>

    <h2><%= survey.getName() %>
    </h2>

    <!-- Survey info -->
    <div class="card mt-3 mb-3">
        <div class="card-body">
            <h5 class="card-title">About:</h5>
            <p><%= survey.getDescription() %>
            </p>
            <p><strong>Category:</strong> <%= survey.getCategoryName() %>
            </p>
            <p><strong>Questions:</strong> <%= survey.getQuestionsAmount() %>
            </p>
        </div>
    </div>

    <p class="text-muted text-start mb-4">Created by <%= survey.getAuthorName() %>
        on <%=DateUtil.formatFull(survey.getCreatedAt()) %>
    </p>

    <!-- Actions Section -->
    <div class="d-flex justify-content-start gap-3 mb-3">
        <button id="startSurveyButton" class="btn btn-md btn-primary" data-survey-id="<%= survey.getId() %>">Start
        </button>

        <% if (user != null && user.getName().equals(survey.getAuthorName())) { %>
        <button id="editSurveyButton" class="btn btn-md btn-secondary">Edit</button>
        <% } %>

        <% if (user != null && (user.getRole() == RoleType.ADMIN || user.getName().equals(survey.getAuthorName()))) { %>
        <button id="statisticsSurveyButton" class="btn btn-md btn-success">Statistics</button>
        <% } %>

        <% if (user != null && (user.getRole() == RoleType.ADMIN || user.getName().equals(survey.getAuthorName()))) { %>
        <button id="deleteSurveyButton" class="btn btn-md btn-danger">Delete</button>
        <% } %>
    </div>
</div>
</body>
</html>
