<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User" %>
<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType" %>
</style>
<nav class="navbar navbar-expand-lg navbar-light bg-light bd-navbar">
    <div class="container-fluid">
        <%
            User user = (User) request.getAttribute("user");
            User userView = (User) request.getAttribute("userView");
        %>
        <a class="navbar-brand" href="/">
            <img src="/static/images/survey.png" alt="" width="30" height="30" class="d-inline-block align-text-top">
            Surveys
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <% if (user != null) { if (user.getRole() != RoleType.BANNED) { %>
                <li class="nav-item">
                    <a class="nav-link <% if (request.getServletPath().equals("/WEB-INF/pages/my_surveys_list.jsp")) { %> disabled <% } else {%>active<%}%>"
                       aria-current="page" href="/surveys/my">My surveys</a>
                </li>
                <% if (user.getRole() == RoleType.ADMIN) { %>
                <li class="nav-item">
                    <a class="nav-link <% if (request.getServletPath().equals("/WEB-INF/pages/users_list.jsp")) { %> disabled <% } else {%>active<%}%>"
                       aria-current="page" href="/users">Manage users</a>
                </li>
                <% } } } %>
            </ul>
            <% if (request.getAttribute("jakarta.servlet.error.status_code") == null) { %>
            <form class="d-flex">
                <% if (user != null) { if (user.getRole() != RoleType.BANNED) { %>
                <span class="navbar-text me-2">
                  Welcome, <a
                        class="nav-link d-inline ps-0 <% if (request.getServletPath().equals("/WEB-INF/pages/profile_self.jsp")) { %> disabled <% } else {%>active<%}%>"
                        href="/profile">
                    <%= user.getName() %>
                </a>
                </span>
                <button type="button" class="btn btn-outline-danger" onclick="handleLogout()">Logout</button>
                <% } } else { %>
                <a class="btn btn-outline-primary me-2" href="/login">Login</a>
                <a class="btn btn-primary" href="/signup">Sign Up</a>
                <% } %>
            </form>
            <% } %>
        </div>
    </div>
</nav>