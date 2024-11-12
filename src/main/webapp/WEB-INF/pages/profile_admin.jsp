<%@ page import="ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <% User userTitle = (User) request.getAttribute("userView"); %>
    <title>Manage <%=userTitle.getName()%>
    </title>
    <%@include file="../templates/resources.jsp" %>
</head>
<body>

<%@include file="../templates/navbar.jsp" %>

<div class="container mt-4">
    <h2>Manage user</h2>

    <% if (userView != null) { %>
    <div class="card mt-3">
        <div class="card-body">
            <h5 class="card-title">User information</h5>
            <p class="card-text"><strong>ID:</strong> <%= userView.getId() %>
            </p>
            <p class="card-text"><strong>Name:</strong> <%= userView.getName() %>
            </p>
            <p class="card-text"><strong>Role:</strong> <%= userView.getRole() %>
            </p>
        </div>
    </div>

    <% if (userView.getRole() != RoleType.ADMIN) { %>
    <div class="container mt-3">
        <div class="row justify-content-start">
            <div class="col-md-3">
                <% if (userView.getRole() != RoleType.BANNED) { %>
                <form id="updateRoleForm" class="mb-3">
                    <input type="hidden" name="userId" value="<%= userView.getId() %>">

                    <div class="row align-items-center">
                        <div class="col-auto text-black">Role</div>
                        <div class="col">
                            <select class="form-select" id="roleSelect" name="role">
                                <%
                                    RoleType[] roles = (RoleType[]) request.getAttribute("roles");
                                    for (RoleType role : roles) {
                                %>
                                <option value="<%= role.name() %>" <%= role == userView.getRole() ? "selected" : "" %>>
                                    <%= role.name() %>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-auto">
                            <button type="submit" id="updateRoleButton" class="btn btn-primary" disabled>Update</button>
                        </div>
                    </div>
                </form>
                <% } %>

                <form id="banUserForm" class="mb-3">
                    <input type="hidden" name="userId" value="<%= userView.getId() %>">
                    <button type="submit" class="btn btn-warning w-100">
                        <% if (userView.getRole() != RoleType.BANNED) { %>
                        Ban
                        <% } else { %>
                        Unban
                        <% } %>
                    </button>
                </form>

                <form id="deleteUserForm" class="mb-3">
                    <input type="hidden" name="userId" value="<%= userView.getId() %>">
                    <button type="submit" class="btn btn-danger w-100">Delete</button>
                </form>

            </div>
        </div>
    </div>
    <% }
    } %>
</div>
</body>
<script src="/static/js/profile_admin.js"></script>
</html>