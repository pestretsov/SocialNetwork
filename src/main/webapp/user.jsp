<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/29/16
  Time: 2:16 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="requestUser" scope="request" type="model.User"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>

    <title>${requestUser.firstName} ${requestUser.lastName}</title>
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
        <div class="col-md-4">
            <div id="user-info" class="panel">
                <div class="row">
                    <div class="col-md-12">
                        <div class="pull-left">
                            <img class="user-avatar" src="<c:url value='/images/artemy.jpg'/>">
                        </div>
                        <h3 id="requestUserFullName">${requestUser.firstName} ${requestUser.lastName}</h3>
                        <h4 id="requestUserUsername" data-user-id="${requestUser.id}">@${requestUser.username}</h4>
                        <div class="clearfix"></div>
                        <p class="user-bio">${requestUser.bio}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6"><a>Followers ${requestScope.followersCount}</a></div>
                    <div class="col-md-6"><a>Following ${requestScope.followingsCount}</a></div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:choose>
                            <c:when test="${requestScope.canFollow}">
                                <form action="<c:url value='/secure/follow'/>" method="post">
                                    <button type="submit" class="btn btn-primary btn-sm follow-button" name="requestUserId" value="${requestUser.id}">Follow ${requestUser.firstName}</button>
                                </form>
                            </c:when>
                            <c:when test="${(not requestScope.canFollow) && (not (empty sessionUser))}">
                                <form action="<c:url value='/secure/unfollow'/>" method="post">
                                    <button type="submit" class="btn btn-default btn-sm follow-button" name="requestUserId" value="${requestUser.id}">Following ${requestUser.firstName}</button>
                                </form>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <%--AJAX HERE--%>
            <div id="posts"></div>
        </div>
        <div class="col-md-2">
            <%--<div class="panel">--%>
            <%--<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>--%>
            <%--</div>--%>
        </div>
    </div>
</div>

<script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/moment.min.js'/>"></script>
<script src="<c:url value="/js/script.js"/>"></script>

</body>
</html>

