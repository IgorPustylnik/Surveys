<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Survey</title>
    <%@ include file="../templates/resources.jsp" %>
    <script src="/js/survey_taking.js"></script>
    <style>
        .question-button {
        }

        .question-button.current {
            border-width: 2px;
        }

        .question-button.answered {
            background-color: #d6dbea;
        }
    </style>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>
<%
    List<SessionQuestion> questions = (List<SessionQuestion>) request.getAttribute("questions");
    Integer sessionId = (Integer) request.getAttribute("sessionId");
    String surveyName = (String) request.getAttribute("surveyName");

    Integer questionIndex = (Integer) request.getAttribute("questionIndex");
    SessionQuestion question = questions.get(questionIndex - 1);
%>
<div class="container mt-4 mb-4">
    <div class="row">
        <div class="col-md-3">
            <h5>Survey questions</h5>
            <div class="question-nav">
                <% int questionIndexNav = 1; %>
                <% for (SessionQuestion questionNav : questions) { %>
                <button
                        onclick="navigateToQuestion(event, <%=questionIndexNav%>)"
                        id="nav-question-<%=questionIndexNav%>"
                        class="btn btn-outline-secondary question-button <%= questionIndexNav == questionIndex ? "current" : "" %> <%= questionNav.isAnswered() ? "answered" : "" %>"
                >
                    <%= questionIndexNav %>
                </button>
                <% questionIndexNav++; %>
                <% } %>
            </div>
        </div>

        <div class="col-md-9">
            <h2 class="mb-4 text-start"><%=surveyName%>
            </h2>
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title"><%= questionIndex %>. <%= question.getText() %>
                    </h5>
                    <div class="form-group">
                        <% if (question.getType() == QuestionType.SINGLE_CHOICE) { %>
                        <% for (SessionOption option : question.getOptions()) { %>
                        <div class="form-check">
                            <input type="radio" class="form-check-input" name="question"
                                   value="<%= option.getId() %>" id="option_<%= option.getId() %>"
                                <%= option.isSelected() ? "checked" : "" %>>
                            <label class="form-check-label" for="option_<%= option.getId() %>">
                                <%= option.getDescription() %>
                            </label>
                        </div>
                        <% } %>
                        <% } else if (question.getType() == QuestionType.MULTIPLE_CHOICE) { %>
                        <% for (SessionOption option : question.getOptions()) { %>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="question"
                                   value="<%= option.getId() %>" id="option_<%= option.getId() %>"
                                <%= option.isSelected() ? "checked" : "" %>>
                            <label class="form-check-label" for="option_<%= option.getId() %>">
                                <%= option.getDescription() %>
                            </label>
                        </div>
                        <% } %>
                        <% } %>
                    </div>
                </div>
            </div>

            <%
                if (questionIndex >= questions.size()) {
                    boolean allAnsweredExceptLast = questions.size() <= 1 ||
                            questions.subList(0, questions.size() - 1).stream().allMatch(SessionQuestion::isAnswered);
            %>
            <form id="surveyForm" data-questions-amount="<%= questions.size() %>" data-session-id="<%= sessionId %>"
                  onsubmit="finishSurvey(event, <%= allAnsweredExceptLast %>)">
                <button type="submit" class="btn btn-primary btn-lg w-100">Finish</button>
            </form>
            <% } else { %>
            <form id="surveyForm" data-questions-amount="<%= questions.size() %>" data-session-id="<%= sessionId %>"
                  onsubmit="nextQuestion(event, <%= questionIndex %>)">
                <button type="submit" class="btn btn-primary btn-lg w-100">Next question</button>
            </form>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
