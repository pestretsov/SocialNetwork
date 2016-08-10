<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/6/16
  Time: 5:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul class="nav navbar-nav">
    <li><a href="<c:url value='/'/>"><span class="glyphicon glyphicon-home"></span> Home</a></li>
    <li><a href="<c:url value="/discover"/>" ><span class="glyphicon glyphicon-move"></span> Discover</a></li>
</ul>
<ul class="nav navbar-nav navbar-right">
    <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-user"></span><span id="sessionUserFirstName" data-user-id="${sessionUser.id}"> ${sessionUser.firstName} </span><span class="caret"></span></a>
        <ul class="dropdown-menu">
            <li><a href="<c:url value="/secure/usersettings"/>">Settings</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="<c:url value='/logout'/>">Logout</a></li>
        </ul>
    </li>
    <li><a href="<c:url value='/secure/createpost'/>" ><span class="glyphicon glyphicon-pencil"></span></a></li>
</ul>
