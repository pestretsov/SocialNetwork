<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/27/16
  Time: 2:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <!-- Bootstrap -->
    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
    <!-- My styles -->
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">

    <title>Log in SocNet</title>
</head>
<body>
    <div class="container">
        <div class="main-center panel col-md-4 col-md-offset-4">
            <h1 class="card-title"><fmt:message key="signin.title"/></h1>
            <div class="row">
                <div class="col-md-12">
                    <form action="<c:url value='/login'/>" method="post">
                        <div class="form-group">
                            <input class="form-control" type="text" name="j_username" value="" placeholder="<fmt:message key="signin.usernamePlaceholder"/>" required>
                        </div>
                        <div class="form-group">
                            <input class="form-control" type="password" name="j_password" value="" placeholder="<fmt:message key="signin.passwordPlaceholder"/>" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm btn-block login-button"><fmt:message key="signin.button"/></button>
                    </form>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <form method="post" action="<c:url value="/changelocale"/>" class="navbar-form navbar-right">
                        <div class="btn-group" role="group" aria-label="...">
                            <c:if test="${locale eq 'ru'}">
                                <button class="btn btn-primary" name="locale" value="ru">RU</button>
                                <button class="btn btn-default" name="locale" value="en">EN</button>
                            </c:if>
                            <c:if test="${not(locale eq 'ru')}">
                                <button class="btn btn-default" name="locale" value="ru">RU</button>
                                <button class="btn btn-primary" name="locale" value="en">EN</button>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
            <p class="sign-up"><fmt:message key="signin.signupText"/> <a href="<c:url value="/signup"/>"><fmt:message key="signin.signupHref"/> >></a></p>
        </div>
    </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<c:url value="/js/jquery-3.1.0.min.js"/>"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
