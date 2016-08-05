<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/5/16
  Time: 6:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="sessionUser" scope="session" type="model.User"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>

    <title>${sessionUser.firstName} ${sessionUser.lastName}</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="main-center panel col-md-6 col-md-offset-3">
            <h1 id="welcome-to-socnet">Change user personal info</h1>
            <form id="settings" action="<c:url value='/secure/usersettings'/>" method="post">
                <div class="form-group">
                    <label for="user-new-firstname-input">First name</label>
                    <input id="user-new-firstname-input" class="form-control" type="text" name="first_name" value="${sessionUser.firstName}" required>
                </div>
                <div class="form-group">
                    <label for="user-new-lastname-input">Last name</label>
                    <input id="user-new-lastname-input" class="form-control" type="text" name="last_name" value="${sessionUser.lastName}" required>
                </div>
                <div class="form-group">
                    <label for="user-new-dob-input">Birthday</label>
                    <input id="user-new-dob-input" class="form-control" type="date" name="birth_date" value="${sessionUser.birthDate.toString()}" required>
                </div>
                <div class="form-group">
                    <label for="user-new-bio-input">Bio</label>
                    <textarea form ="settings" id="user-new-bio-input" class="form-control" name="bio" rows="5" wrap="soft" placeholder="Describe yourself here..." >${sessionUser.bio}</textarea>
                </div>
                <div class="form-group">
                    <button class="pull-right btn btn-primary btn-sm" type="submit" name="Update">Change</button>
                </div>
            </form>
        </div>
    </div>
</div>


<script src="<c:url value='/js/jquery-3.1.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/moment.min.js'/>"></script>
<script src="<c:url value="/js/script.js"/>"></script>

</body>
</html>
