<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/9/16
  Time: 2:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-md-4">
    <div id="user-info" class="panel">
        <div class="row">
            <div class="col-md-12">
                <div class="pull-left">
                    <img class="user-avatar" src="<c:url value='/images/artemy.jpg'/>">
                </div>
                <h3 id="requestUserFullName">${follower.firstName} ${follower.lastName}</h3>
                <h4 id="requestUserUsername" data-user-id="${follower.id}">@${follower.username}</h4>
                <div class="clearfix"></div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <%--<c:choose>--%>
                    <%--<c:when test="${requestScope.canFollow}">--%>
                        <%--<form action="<c:url value='/secure/follow'/>" method="post">--%>
                            <%--<button type="submit" class="btn btn-primary btn-sm follow-button" name="requestUserId" value="${requestUser.id}">Follow ${requestUser.firstName}</button>--%>
                        <%--</form>--%>
                    <%--</c:when>--%>
                    <%--<c:when test="${(not requestScope.canFollow) && (not (empty sessionUser))}">--%>
                        <%--<form action="<c:url value='/secure/unfollow'/>" method="post">--%>
                            <%--<button type="submit" class="btn btn-default btn-sm follow-button" name="requestUserId" value="${requestUser.id}">Following ${requestUser.firstName}</button>--%>
                        <%--</form>--%>
                    <%--</c:when>--%>
                <%--</c:choose>--%>
            </div>
        </div>
    </div>
</div>