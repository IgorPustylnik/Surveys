<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.util.PaginationUtil" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage users</title>
    <%@include file="../templates/resources.jsp"%>
</head>
<body>

<%@include file="../templates/navbar.jsp"%>

<div class="container mt-4">

    <!-- Users list -->
    <div class="row">
        <%
            ServiceResponse<PagedEntity<List<User>>> serviceResponse = (ServiceResponse<PagedEntity<List<User>>>) request.getAttribute("serviceResponseSurveys");
            if (serviceResponse.success()) {
                List<User> users = serviceResponse.body().page();
                for (User usr : users) {
        %>
        <div class="col-md-6 mb-4">
            <div class="card">
                <div class="card-body">
                    <a href="/profile/<%= usr.getId() %>" class="text-decoration-none">
                        <h5 class="card-title"><%= usr.getName() %>
                        </h5>
                    </a>
                    <p class="card-text">ID: <%=usr.getId()%>
                    </p>
                    <p class="card-text">Role: <%=usr.getRole().toString()%>
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
                    PagedEntity<List<User>> pagedList = serviceResponse.body();
                    int currentPage = (int) request.getAttribute("currentPage");
                    int[] visiblePages = PaginationUtil.getVisiblePagesNumbers(currentPage, 5, pagedList.size());
                    for (int i : visiblePages) {
                        if (i == currentPage) { %>
                <li class="page-item active"><span class="page-link"><%=i%></span></li>
                <% } else { %>
                <li class="page-item"><a class="page-link" href="/users?page=<%=i%>"><%=i%>
                </a></li>
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
        <h4 class="alert-heading"><%= serviceResponse.message() %>
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
</body>
</html>