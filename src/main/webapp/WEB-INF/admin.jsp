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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<jsp:useBean id="users" scope="request" type="java.util.List<model.User>"/>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <title>Admin page</title>
</head>
<body>
<header>
    <nav id="header-nav" class="navbar navbar-default navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a id="logo" class="navbar-brand" href="#">SocNet</a>
            </div>
            <div id="navbar-collapse" class="collapse navbar-collapse">
                <%--@elvariable id="sessionUser" type="model.User"--%>
                    <c:choose>
                        <c:when test="${not (empty sessionUser) && sessionUser.role == 'ADMIN'}">
                            <jsp:include page="common/admin_navbar.jsp"/>
                        </c:when>
                        <c:when test="${not (empty sessionUser)}">
                            <jsp:include page="common/singnedin_navbar.jsp"/>
                        </c:when>
                        <c:otherwise>
                            <jsp:include page="common/signedout_navbar.jsp"/>
                        </c:otherwise>
                    </c:choose>
            </div>
        </div>
    </nav>
</header>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1><fmt:message key="admin.title"/></h1>
            <table class="table">
                <tr>
                    <th><fmt:message key="admin.id"/></th>
                    <th><fmt:message key="admin.username"/></th>
                    <th><fmt:message key="admin.firstName"/></th>
                    <th><fmt:message key="admin.lastName"/></th>
                    <th><fmt:message key="admin.role"/></th>
                    <th><fmt:message key="admin.changeRole"/></th>
                    <th><fmt:message key="admin.remove"/></th>
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

    <script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
    <script src="<c:url value='/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/js/moment.min.js'/>"></script>
</body>
</html>
