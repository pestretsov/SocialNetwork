<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/6/16
  Time: 5:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<ul class="nav navbar-nav">
    <li><a href="<c:url value="/discover"/>" ><span class="glyphicon glyphicon-move"></span> <fmt:message key="header.discover"/></a></li>
</ul>
<ul class="nav navbar-nav navbar-right">
    <li>
        <form class="navbar-form navbar-right" action="<c:url value="/login"/>" method="post">
            <div class="form-group">
                <input type="text" class="form-control" name="j_username"  placeholder="<fmt:message key="signin.usernamePlaceholder"/>" required>
                <input type="password" class="form-control" name="j_password" placeholder="<fmt:message key="signin.passwordPlaceholder"/>" required>
                <button type="submit" class="btn btn-primary"><fmt:message key="signin.button"/>"</button>
            </div>
        </form>
    </li>
    <li><a href="<c:url value="/signup"/>"> <fmt:message key="signup.button"/>"</a> </li>
</ul>