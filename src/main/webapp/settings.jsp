<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/5/16
  Time: 6:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>"/>

    <title>${sessionUser.firstName} ${sessionUser.lastName}</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="main-center panel col-md-6 col-md-offset-3">
            <h1 class="card-title"><fmt:message key="settings.title"/></h1>
            <form id="settings" action="<c:url value='/secure/usersettings'/>" method="post">
                <div class="form-group">
                    <label for="user-new-firstname-input"><fmt:message key="settings.firstName"/></label>
                    <input id="user-new-firstname-input" class="form-control" type="text" name="first_name" value="${sessionUser.firstName}" required>
                </div>
                <div class="form-group">
                    <label for="user-new-lastname-input"><fmt:message key="settings.lastName"/></label>
                    <input id="user-new-lastname-input" class="form-control" type="text" name="last_name" value="${sessionUser.lastName}" required>
                </div>
                <div class="form-group">
                    <label for="user-new-dob-input"><fmt:message key="settings.birthday"/></label>
                    <input id="user-new-dob-input" class="form-control" type="date" name="birth_date" value="${sessionUser.birthDate.toString()}" required>
                </div>
                <div class="form-group">
                    <label for="user-new-bio-input"><fmt:message key="settings.bio"/></label>
                    <textarea form ="settings" id="user-new-bio-input" class="form-control" name="bio" rows="5" wrap="soft" placeholder="<fmt:message key="settings.bioPlaceholder"/>" >${sessionUser.bio}</textarea>
                </div>
                <div class="form-group">
                    <button class="pull-right btn btn-primary btn-sm" type="submit" name="Submit"><fmt:message key="settings.submitPersonalInfoChange"/></button>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="main-center panel col-md-6 col-md-offset-3">
            <h1 class="card-title"><fmt:message key="settings.changePasswordTitle"/></h1>
            <form id="changepassword" action="<c:url value='/secure/changepassword'/>" method="post">
                <div class="form-group">
                    <label for="user-old-password-input"><fmt:message key="settings.oldPassword"/></label>
                    <input id="user-old-password-input" class="form-control" type="password" name="old_password" required>
                </div>
                <div class="form-group">
                    <label for="user-new-password-input"><fmt:message key="settings.newPassword"/></label>
                    <input id="user-new-password-input" class="form-control" type="password" name="new_password" required>
                </div>
                <div class="form-group">
                    <label for="user-new-password-confirm-input"><fmt:message key="settings.repeatNewPassword"/></label>
                    <input id="user-new-password-confirm-input" class="form-control" type="password" name="new_password_confirm" required>
                </div>
                <div class="form-group">
                    <button class="pull-right btn btn-primary btn-sm" type="submit" name="Submit"><fmt:message key="settings.submitPasswordChange"/></button>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="main-center panel col-md-6 col-md-offset-3">
            <h1 class="card-title"><fmt:message key="settings.changeUsernameTitle"/></h1>
            <form id="changeusername" action="<c:url value='/secure/changeusername'/>" method="post">
                <div class="form-group">
                    <label for="user-username-input"><fmt:message key="settings.username"/></label>
                    <input id="user-username-input" class="form-control" type="text" name="new_username" value="${sessionUser.username}" required>
                </div>
                <div class="form-group">
                    <button class="pull-right btn btn-primary btn-sm" type="submit" name="Submit"><fmt:message key="settings.submitUsernameChange"/></button>
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
