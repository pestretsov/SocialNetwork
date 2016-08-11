<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/11/16
  Time: 2:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="sessionUser" scope="session" type="model.User"/>

<jsp:useBean id="users" scope="request" type="java.util.List<model.User>"/>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <title>Admin page</title>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1>Admin users</h1>
            <table class="table">
                <tr>
                    <th>id</th>
                    <th>username</th>
                    <th>first name</th>
                    <th>last name</th>
                    <th>role</th>
                    <th>change role</th>
                    <th>delete</th>
                </tr>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.role}</td>
                        <c:if test="${not (user.id eq sessionUser.id)}">
                            <td><form action="<c:url value="/admin/changerole"/>" method="post"><button name="user_id" value="${user.id}" class="btn btn-xs btn-default btn-primary">Change role</button></form></td>
                            <td><form action="<c:url value="/admin/removeuser"/>" method="post"><button name="user_id" value="${user.id}" class="btn btn-xs btn-default btn-danger">Delete</button></form></td>
                        </c:if>
                        <c:if test="${(user.id eq sessionUser.id)}">
                            <td>(you)</td>
                            <td>(you)</td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
            </div>
        </div>
    </div>
</body>
</html>
