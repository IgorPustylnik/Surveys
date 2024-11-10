<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Survey</title>
    <%@ include file="../templates/resources.jsp" %>
    <script src="/js/survey_taking.js"></script>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>

<div class="container mt-4 mb-4">
    <%
        List<Question> questions = (List<Question>) request.getAttribute("questions");
        Integer sessionId = (Integer) request.getAttribute("sessionId");
        String surveyName = (String) request.getAttribute("surveyName");
    %>

    <h2 class="mb-4"><%=surveyName%></h2>

    <form id="surveyForm" data-questions-amount="<%= questions.size() %>" data-session-id="<%= sessionId %>" onsubmit="submitAnswers(event)">
        <%
            int questionNumber = 1;
            for (Question question : questions) {
        %>
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title"><%= questionNumber %>. <%= question.getText() %>
                </h5>
                <div class="form-group">

                    <% if (question.getType() == QuestionType.SINGLE_CHOICE) { %>
                    <% for (Option option : question.getOptions()) { %>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="question_<%= questionNumber %>"
                               value="<%= option.getId() %>" id="option_<%= option.getId() %>">
                        <label class="form-check-label" for="option_<%= option.getId() %>">
                            <%= option.getDescription() %>
                        </label>
                    </div>
                    <% } %>

                    <% } else if (question.getType() == QuestionType.MULTIPLE_CHOICE) { %>
                    <% for (Option option : question.getOptions()) { %>
                    <div class="form-check">
                        <input type="checkbox" class="form-check-input" name="question_<%= questionNumber %>"
                               value="<%= option.getId() %>" id="option_<%= option.getId() %>">
                        <label class="form-check-label" for="option_<%= option.getId() %>">
                            <%= option.getDescription() %>
                        </label>
                    </div>
                    <% } %>
                    <% }
                        questionNumber++; %>
                </div>
            </div>
        </div>
        <% } %>

        <button type="submit" class="btn btn-primary">Submit answers</button>
    </form>
</div>

</body>
</html>
