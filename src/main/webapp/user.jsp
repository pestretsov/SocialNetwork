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
<jsp:useBean id="userPosts" scope="request" type="java.util.List<model.Post>"/>
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
                        <ul class="nav navbar-nav">
                            <li class="active"><a href="<c:url value='/'/>"><span class="glyphicon glyphicon-home"></span> Home</a></li>
                            <li><a href="#" ><span class="glyphicon glyphicon-envelope"></span> Messages</a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-user"></span><span id="sessionUserFirstName" data-user-id="${sessionUser.id}"> ${sessionUser.firstName} </span><span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Settings</a></li>
                                    <li role="separator" class="divider"></li>
                                    <li><a href="<c:url value='/logout'/>">Logout</a></li>
                                </ul>
                            </li>
                            <li><a href="<c:url value='/secure/createpost'/>" ><span class="glyphicon glyphicon-pencil"></span></a></li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <form class="navbar-form navbar-right" action="<c:url value="/login"/>" method="post">
                                    <div class="form-group">
                                        <input type="text" class="form-control" name="j_username"  placeholder="username" required>
                                        <input type="password" class="form-control" name="j_password" placeholder="password" required>
                                        <button type="submit" class="btn btn-primary">Sign in</button>
                                    </div>
                                </form>
                            </li>
                            <li><a href="<c:url value="/signup"/>"> Sign Up</a> </li>
                        </ul>
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
                <div class="pull-left">
                    <img class="user-avatar" src="<c:url value='/images/artemy.jpg'/>">
                </div>
                <h3 id="requestUserFullName">${requestUser.firstName} ${requestUser.lastName}</h3>
                <h4 id="requestUserUsername" data-user-id="${requestUser.id}">@${requestUser.username}</h4>
                <div class="clearfix"></div>
                <p>${requestUser.bio}</p>
                <button type="submit" class="btn btn-primary btn-sm follow-button">Follow ${requestUser.firstName}</button>
            </div>
        </div>
        <div class="col-md-6">
            <%--AJAX HERE--%>
            <div id="posts"></div>
        </div>
        <div class="col-md-2">
            <div class="panel">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
            </div>
        </div>
    </div>
</div>

<script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/moment.js'/>"></script>
<script src="<c:url value="/js/script.js"/>"></script>

</body>
</html>

