<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 7/18/16
  Time: 6:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        #bio {
            resize: none;
        }
    </style>
</head>
<body>
<h1>Sign up</h1>
<h2>Please, complete the following form to proceed:</h2>
<form action="signup" method="post" id="signup">
    First name:<br>
    <input type="text" name="first_name"><br>
    Last name:<br>
    <input type="text" name="last_name"><br>
    Birthday:<br>
    <input type="date" name="birth_date"><br>
    Username:<br>
    <input type="text" name="j_username" required><br>
    Password: <br>
    <input type="password" name="j_password" required><br>
    Repeat password:<br>
    <input type="password" name="j_password_test" required><br>
    <br>
    <textarea form ="signup" id="bio" name="bio" rows="10" cols="60" wrap="soft" placeholder="Describe yourself here..."></textarea>
    <br><br>
    <input type="submit" name="Submit">
</form>
</body>
</html>
