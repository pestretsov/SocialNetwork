<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/29/16
  Time: 5:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<jsp:useBean id="sessionUser" scope="session" type="model.User"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <!-- Bootstrap -->
    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>

    <title>${sessionUser.firstName}</title>
</head>
<body>
    <div class="container">
        <div class="main-center panel col-md-6 col-md-offset-3">
            <form id="post" method="post" action="<c:url value='/secure/createpost'/>">
                <h1 class="card-title"><fmt:message key="addpost.title"/></h1>
                <textarea id="post-text" form="post" name="postText" rows="4" class="panel form-control col-md-12" placeholder="<fmt:message key="addpost.placeholder"/>" maxlength="255" required></textarea>
                <label class="checkbox-inline"><input type="checkbox" name="postType" value="PRIVATE"><fmt:message key="addpost.privacy"/></label>
                <input type="submit" class="pull-right btn btn-primary" value="<fmt:message key="addpost.submit"/>">
            </form>
        </div>
    </div>
    <script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
    <script src="<c:url value='/js/bootstrap.min.js'/>"></script>
</body>
</html>
