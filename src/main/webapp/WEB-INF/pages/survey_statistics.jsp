<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Statistics</title>
    <%@include file="../templates/resources.jsp" %>
    <script src="/static/js/survey.js"></script>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>
<%
    Survey survey = (Survey) request.getAttribute("survey");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    Map<Integer, OptionStats> stats = (Map<Integer, OptionStats>) request.getAttribute("stats");
%>

<div class="container mt-4 mb-4">
    <% if (questions != null && stats != null) { %>

    <h2 class="text-decoration-none mb-4">Statistics for <a href="/survey/<%= survey.getId() %>"
                                                            class="text-decoration-none"><%= survey.getName() %>
    </a></h2>
    <%
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            OptionStats optionStats = stats.get(question.getId());
            int totalResponses = optionStats.total();
    %>

    <div class="card mt-3">
        <div class="card-body">
            <h5><%=i + 1%>. <%= question.getText() %>
            </h5>
            <%
                for (Option option : question.getOptions()) {
                    int chosenCount = optionStats.optionChosen().getOrDefault(option.getId(), 0);
                    int percentage = totalResponses > 0 ? (chosenCount * 100 / totalResponses) : 0;
            %>

            <div class="mb-2">
                <span><%= option.getDescription() %> - <%= chosenCount %></span>
                <div class="progress" style="height: 30px">
                    <div class="progress-bar bg-success progress-bar-label" role="progressbar" style="width: <%= percentage %>%; font-size: 1.1rem;"
                         aria-valuenow="<%= percentage %>" aria-valuemin="0" aria-valuemax="100">
                        <%= percentage %>%
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>
    <% }
    } %>
</div>
</body>
</html>