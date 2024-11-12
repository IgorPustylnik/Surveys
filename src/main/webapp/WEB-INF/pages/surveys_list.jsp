<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.util.PaginationUtil" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Surveys</title>
    <%@include file="../templates/resources.jsp" %>
</head>
<body>

<%@ include file="../templates/navbar.jsp" %>

<div class="container mt-4">

    <%
        ServiceResponse<PagedEntity<List<Category>>> serviceResponseCategories = (ServiceResponse<PagedEntity<List<Category>>>) request.getAttribute("serviceResponseCategories");
        List<Category> categories = serviceResponseCategories.body().page();
        String selectedCategoryId = request.getParameter("categoryId");
    %>

    <!-- Filters Button -->
    <div class="d-flex justify-content-end" style="position: relative;">
        <button id="filtersButton" class="btn btn-outline-secondary position-relative mb-4"
                data-bs-toggle="modal" data-bs-target="#filtersModal">
            <i class="bi bi-funnel"></i> Filters
            <span id="filterCountBadge" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                0
            </span>
        </button>
    </div>


    <!-- Filters Modal -->
    <div class="modal fade" id="filtersModal" tabindex="-1" aria-labelledby="filtersModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="filtersModalLabel">Filter Surveys</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="surveys" method="get">
                        <div class="mb-3">
                            <label for="categorySelect" class="form-label">Select Category:</label>
                            <select class="form-select" id="categorySelect" name="categoryId">
                                <option value="">All Categories</option>
                                <% for (Category category : categories) {
                                    String selected = String.valueOf(category.getId()).equals(selectedCategoryId) ? "selected" : "";
                                %>
                                <option value="<%= category.getId() %>" <%= selected %>><%= category.getName() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="fromDate" class="form-label">From Date:</label>
                            <input type="date" class="form-control" id="fromDate" name="fromDate"
                                   value="<%= request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "" %>">
                        </div>
                        <div class="mb-3">
                            <label for="toDate" class="form-label">To Date:</label>
                            <input type="date" class="form-control" id="toDate" name="toDate"
                                   value="<%= request.getParameter("toDate") != null ? request.getParameter("toDate") : "" %>">
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Apply Filter</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Surveys list -->
    <div class="row">
        <%
            ServiceResponse<PagedEntity<List<Survey>>> serviceResponseSurveys = (ServiceResponse<PagedEntity<List<Survey>>>) request.getAttribute("serviceResponseSurveys");
            if (serviceResponseSurveys.success()) {
                List<Survey> surveys = serviceResponseSurveys.body().page();
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
                       href="${pageContext.request.contextPath}/surveys?page=<%=i%><%= currentParams != null ? "&" + currentParams : "" %>">
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

    <div class="alert text-center" role="alert">
        <h4 class="alert-heading"><%= serviceResponseSurveys.message() %>
        </h4>
    </div>
    <%
        }
    %>

    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
    <div class="alert alert-danger text-center" role="alert">
        <%= errorMessage %>
    </div>
    <% } %>

</div>
<script src="/static/js/surveys_list_filter.js"></script>
</body>
</html>