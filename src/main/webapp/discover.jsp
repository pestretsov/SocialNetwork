<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/9/16
  Time: 3:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="followingsList" scope="request" type="java.util.List<model.User>"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>
    <title>Discover</title>
</head>
<body>
<c:forEach items="${followingsList}" var="following">
    <div class="col-md-4">
        <div id="user-info" class="panel">
            <div class="row">
                <div class="col-md-12">
                    <div class="pull-left">
                        <img class="user-avatar" src="<c:url value='/images/artemy.jpg'/>">
                    </div>
                    <h3 id="requestUserFullName">${following.firstName} ${following.lastName}</h3>
                    <h4 id="requestUserUsername" data-user-id="${following.id}">@${following.username}</h4>
                    <div class="clearfix"></div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                        <%--<c:choose>--%>
                        <%--<c:when test="${requestScope.canFollow}">--%>
                        <%--<form action="<c:url value='/secure/follow'/>" method="post">--%>
                        <%--<button type="submit" class="btn btn-primary btn-sm follow-button" name="requestUserId" value="${requestUser.id}">Follow ${requestUser.firstName}</button>--%>
                        <%--</form>--%>
                        <%--</c:when>--%>
                        <%--<c:when test="${(not requestScope.canFollow) && (not (empty sessionUser))}">--%>
                        <%--<form action="<c:url value='/secure/unfollow'/>" method="post">--%>
                        <%--<button type="submit" class="btn btn-default btn-sm follow-button" name="requestUserId" value="${requestUser.id}">Following ${requestUser.firstName}</button>--%>
                        <%--</form>--%>
                        <%--</c:when>--%>
                        <%--</c:choose>--%>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
</body>
</html>