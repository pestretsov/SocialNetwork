<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/18/16
  Time: 6:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <h1 class="card-title">Sign up</h1>
            <form id="signup" action="<c:url value='/signup'/>" method="post">
                <div class="form-group row">
                    <label for="user-firstname-input">First name</label>
                    <input id="user-firstname-input" class="form-control" type="text" name="first_name" required>
                </div>
                <div class="form-group row">
                    <label for="user-lastname-input">Last name</label>
                    <input id="user-lastname-input" class="form-control" type="text" name="last_name" required>
                </div>
                <div class="form-group row">
                    <label for="user-dob-input">Birthday</label>
                    <input id="user-dob-input" class="form-control" type="date" name="birth_date">
                </div>
                <div class="form-group row">
                    <label for="user-sex-select">Gender</label>
                    <select id="user-sex-select" name="sex" form="signup" required>
                        <option value="">gender</option>
                        <option value="1">Female</option>
                        <option value="2">Male</option>
                    </select>
                </div>
                <div class="form-group row">
                    <label for="user-username-input">Username</label>
                    <input id="user-username-input" type="text" class="form-control" name="j_username" required>
                </div>
                <div class="form-group row">
                    <label for="user-password-input">Password</label>
                    <input id="user-password-input" type="password" class="form-control" name="j_password" required>
                </div>
                <div class="form-group row">
                    <label for="user-password-confirm-input">Repeat password</label>
                    <input id="user-password-confirm-input" type="password" class="form-control" name="j_password_test" required>
                </div>
                <div class="form-group row">
                    <label for="user-bio-input">Bio</label>
                    <textarea form ="signup" id="user-bio-input" class="form-control" name="bio" rows="5" wrap="soft" placeholder="Describe yourself here..."></textarea>
                </div>
                <div class="row">
                    <button class="pull-right btn btn-primary btn-sm" type="submit" name="Submit">Sign up</button>
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
