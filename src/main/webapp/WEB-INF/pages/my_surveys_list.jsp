<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.util.PaginationUtil" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Surveys</title>
    <%@include file="../templates/resources.jsp" %>
</head>
<body>

<%@ include file="../templates/navbar.jsp" %>

<%
    ServiceResponse<PagedEntity<List<Survey>>> serviceResponseSurveys = (ServiceResponse<PagedEntity<List<Survey>>>) request.getAttribute("serviceResponseSurveys");
    List<Survey> surveys = serviceResponseSurveys.body().page();
%>

<div class="container mt-4">

    <% if (!surveys.isEmpty()) { %>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <a href="/surveys/create" class="btn btn-outline-success btn-animated">
            <i class="bi bi-plus-lg"></i>
            <span>Create a survey</span>
        </a>
    </div>
    <% } %>

    <!-- Surveys list -->
    <div class="row">
        <%
            if (!surveys.isEmpty()) {
                for (Survey survey : surveys) {
        %>
        <div class="col-md-6 mb-4">
            <div class="card">
                <div class="card-body">
                    <a href="/survey/<%= survey.getId() %>" class="text-decoration-none">
                        <h5 class="card-title"><%= survey.getName() %>
                        </h5>
                    </a>
                    <p class="card-text">Questions: <%=survey.getQuestionsAmount()%>
                    </p>
                    <p class="card-text">Category: <%=survey.getCategoryName()%>
                    </p>
                    <p class="card-text">Author: <%=survey.getAuthorName()%>
                    </p>
                    <p class="card-text">Created: <%=DateUtil.formatShort(survey.getCreatedAt())%>
                    </p>
                </div>
            </div>
        </div>
        <%
            }
        %>
    </div>

    <!-- Pagination -->
    <div class="d-flex justify-content-center mt-4">
        <nav>
            <ul class="pagination">
                <%
                    PagedEntity<List<Survey>> pagedList = serviceResponseSurveys.body();
                    int currentPage = (int) request.getAttribute("currentPage");
                    int[] visiblePages = PaginationUtil.getVisiblePagesNumbers(currentPage, 5, pagedList.size());
                    for (int i : visiblePages) {
                        if (i == currentPage) { %>
                <li class="page-item active"><span class="page-link"><%=i%></span></li>
                <% } else { %>
                <%
                    String currentParams = request.getQueryString();
                    if (currentParams != null && currentParams.contains("page=")) {
                        currentParams = currentParams.replaceAll("page=[^&]*&?", "").replaceAll("&$", "");
                    }
                %>

                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/surveys/my?page=<%=i%><%= currentParams != null ? "&" + currentParams : "" %>">
                        <%= i %>
                    </a>
                </li>
                <% }
                }
                %>
            </ul>
        </nav>
    </div>
    <%
    } else {
    %>

    <!-- Empty State Message -->
    <div class="text-center my-5">
        <h4 class="mb-3">You haven't created any surveys yet.</h4>
        <p class="mb-4">Start by creating your first survey to gather insights!</p>
        <a href="/surveys/create" class="btn btn-success">
            <i class="bi bi-plus-lg"></i> Create a survey
        </a>
    </div>

    <% } %>
</div>

</body>
</html>
