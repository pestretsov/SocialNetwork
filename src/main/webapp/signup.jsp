<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/18/16
  Time: 6:46 PM
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
    <title>Sign up</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
    <!-- My styles -->
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
    <div class="container">
        <div class="main-center panel col-md-6 col-md-offset-3">
            <h1 class="card-title"><fmt:message key="signup.title"/></h1>
            <form id="signup" action="<c:url value='/signup'/>" method="post">
                <div class="form-group row">
                    <label for="user-firstname-input"><fmt:message key="signup.firstName"/></label>
                    <input id="user-firstname-input" class="form-control" type="text" name="first_name" required>
                </div>
                <div class="form-group row">
                    <label for="user-lastname-input"><fmt:message key="signup.lastName"/></label>
                    <input id="user-lastname-input" class="form-control" type="text" name="last_name" required>
                </div>
                <div class="form-group row">
                    <label for="user-dob-input"><fmt:message key="signup.birthday"/></label>
                    <input id="user-dob-input" class="form-control" type="date" name="birth_date">
                </div>
                <div class="form-group row">
                    <label for="user-gender-select"><fmt:message key="signup.gender"/></label><br>
                    <select id="user-gender-select" name="gender" form="signup">
                        <option value="NOT_SPECIFIED"><fmt:message key="signup.genderDefault"/></option>
                        <option value="FEMALE"><fmt:message key="signup.genderFemale"/></option>
                        <option value="MALE"><fmt:message key="signup.genderMale"/></option>
                    </select>
                </div>
                <div class="form-group row">
                    <label for="user-username-input"><fmt:message key="signup.username"/></label>
                    <input id="user-username-input" type="text" class="form-control" name="j_username" required>
                </div>
                <div class="form-group row">
                    <label for="user-password-input"><fmt:message key="signup.password"/></label>
                    <input id="user-password-input" type="password" class="form-control" name="j_password" required>
                </div>
                <div class="form-group row">
                    <label for="user-password-confirm-input"><fmt:message key="signup.repeatPassword"/></label>
                    <input id="user-password-confirm-input" type="password" class="form-control" name="j_password_test" required>
                </div>
                <div class="form-group row">
                    <label for="user-bio-input"><fmt:message key="signup.bio"/></label>
                    <textarea form ="signup" id="user-bio-input" class="form-control" name="bio" rows="5" wrap="soft" placeholder="<fmt:message key="signup.bioPlaceholder"/>"></textarea>
                </div>
                <div class="row">
                    <button class="pull-right btn btn-primary btn-sm" type="submit" name="Submit"><fmt:message key="signup.button"/></button>
                </div>
            </form>
        </div>
    </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<c:url value="/js/jquery-3.1.0.min.js"/>"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
