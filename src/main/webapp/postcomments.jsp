<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/10/16
  Time: 1:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="locale" scope="session" class="java.lang.String"/>
<fmt:setLocale value="${locale}" scope="application"/>
<script>
    var postId = ${requestScope.postId};
    var sessionUser = {
        userId: ${sessionScope.sessionUser.id}
    };
</script>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>

    <title>Comments</title>
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
        <div class="col-md-6 col-md-offset-3">
            <div id="post"></div>
            <div class="row">
                <div class="panel">
                    <div class="panel-body">
                        <div class="form-group">
                            <textarea id="commenttext" name="text" class="form-control" rows="2" placeholder="<fmt:message key="comments.placeholder"/>"
                                      maxlength="255"></textarea>
                        </div>
                        <button id="addcomment" type="submit" class="btn btn-primary pull-right"><fmt:message key="comments.submit"/></button>
                    </div>
                </div>
            </div>
            <div id="comments"><h4><fmt:message key="comments.title"/></h4></div>
        </div>
    </div>
    <div class="col-md-3">
    </div>
</div>

<script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/moment.min.js'/>"></script>
<script src="<c:url value="/js/script.js"/>"></script>

</body>
</html>

