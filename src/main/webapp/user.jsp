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
<jsp:useBean id="sessionUser" scope="session" type="model.User"/>
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
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#" ><span class="glyphicon glyphicon-home"></span> Home</a></li>
                    <li><a href="#" ><span class="glyphicon glyphicon-envelope"></span> Messages</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#" ><span class="glyphicon glyphicon-user"></span> ${sessionUser.firstName}</a></li>
                    <li><a href="#" ><span class="glyphicon glyphicon-pencil"></span></a></li>
                </ul>
            </div>
        </div>
    </nav>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-4">
            <div id="user-info" class="panel">
                <div class="pull-left">
                    <img class="user-avatar" src="images/artemy.jpg">
                </div>
                <h3>${requestUser.firstName} ${requestUser.lastName}</h3>
                <h4>@${requestUser.username}</h4>
                <div class="clearfix"></div>
                <p>${requestUser.bio}</p>
            </div>
        </div>
        <div class="col-md-6">
            <c:forEach items="${userPosts}" var="post">
                <div class="row post panel">
                    <div class="col-md-1">
                        <img class="user-avatar" src="images/artemy.jpg">
                    </div>
                    <div class="col-md-11">
                        <h3>${requestUser.firstName} ${requestUser.lastName}<span> @${requestUser.username}</span><span>;</span><span> ${post.publishTime}</span> </h3>
                        <p>${post.text}</p>
                        <span class="glyphicon glyphicon-star-empty"></span>
                    </div>
                </div>
            </c:forEach>
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

</body>
</html>
