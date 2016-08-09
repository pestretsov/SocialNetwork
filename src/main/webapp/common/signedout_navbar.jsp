<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/6/16
  Time: 5:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul class="nav navbar-nav">
    <li><a href="<c:url value="/discover"/>" ><span class="glyphicon glyphicon-move"></span> Discover</a></li>
</ul>
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