<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/27/16
  Time: 2:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <!-- Bootstrap -->
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>">
    <!-- My styles -->
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">

    <title>Log in SocNet</title>
</head>
<body>
    <div class="container">
        <div class="main-center panel col-md-4 col-md-offset-4">
            <h1 id="welcome-to-socnet">Welcome to SocNet</h1>
            <form action="login" method="post">
                <!-- <div class="input-group"> -->
                    <!-- <span id="username_addon" class="input-group-addon"><i class="glyphicon glyphicon-user" aria-hidden="true"></i></span> -->
                <div class="form-group">
                    <input class="form-control" type="text" name="j_username" value="" placeholder="username" required>
                </div>
                <!-- <div class="input-group"> -->
                    <!-- <span id="password_addon" class="input-group-addon"><i class="glyphicon glyphicon-lock" aria-hidden="true"></i></span> -->
                <div class="form-group">
                    <input class="form-control" type="password" name="j_password" value="" placeholder="password" required>
                </div>
                <button type="submit" class="btn btn-primary btn-sm btn-block login-button">Sign in</button>
            </form>
            <p class="sign-up">Don't have an account yet? <a href="<c:url value="/signup"/>">Sign up >></a></p>
        </div>
    </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<c:url value="/js/jquery-3.1.0.min.js"/>"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
