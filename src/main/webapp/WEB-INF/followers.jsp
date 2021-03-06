<%--@elvariable id="requestUser" type="model.User"--%>
<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/9/16
  Time: 2:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<jsp:useBean id="followersList" scope="request" type="java.util.List<model.User>"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>
    <title>Followers</title>
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
    <h2>${requestUser.firstName}: <fmt:message key="followers.title"/></h2>

    <div class="row">
        <c:forEach items="${followersList}" var="follower">
            <div class="col-md-4">
                <div id="user-info" class="panel">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pull-left">
                                <img class="user-avatar" src="<c:url value='/images/artemy.jpg'/>">
                            </div>
                            <a href="<c:url value='/user/${follower.username}'/>"><h3 id="requestUserFullName">${follower.firstName} ${follower.lastName}</h3></a>
                            <h4 id="requestUserUsername" data-user-id="${follower.id}">@${follower.username}</h4>
                            <div class="clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/moment.min.js'/>"></script>

</body>
</html>
