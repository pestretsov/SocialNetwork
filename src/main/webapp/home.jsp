<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/5/16
  Time: 2:58 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<jsp:useBean id="sessionUser" scope="session" type="model.User"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>

    <title>${sessionUser.firstName} ${sessionUser.lastName}</title>
</head>
<body>
<header>
    <nav id="header-nav" class="navbar navbar-default navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a id="logo" class="navbar-brand" href="#">SocNet</a>
            </div>
            <div id="navbar-collapse" class="collapse navbar-collapse">
                <jsp:include page="common/singnedin_navbar.jsp"/>
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
                        <a href="<c:url value='/user/${sessionUser.username}'/>"><h3 id="requestUserFullName">${sessionUser.firstName} ${sessionUser.lastName}</h3></a>
                        <h4 id="requestUserUsername" data-user-id="${sessionUser.id}">@${sessionUser.username}</h4>
                        <div class="clearfix"></div>
                        <p class="user-bio">${sessionUser.bio}</p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="btn-group btn-group-justified" role="group" aria-label="...">
                        <a role="button" class="btn btn-default" href="<c:url value='/followers/${sessionUser.username}'/>"><fmt:message key="body.followers"/> ${requestScope.followersCount}</a>
                        <a role="button" class="btn btn-default" href="<c:url value='/followings/${sessionUser.username}'/>"><fmt:message key="body.following"/> ${requestScope.followingsCount}</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <%--AJAX HERE--%>
            <div id="timeline"></div>
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
