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
    <title>Login Page</title>
</head>
<h2>Login:</h2>
<body>
    <form action="login" method="post">
        Username: <input type="text" name="j_username" value="" required><br>
        Password: <input type="password" name="j_password" value="" required><br>
        <input type="submit" value="Log In">
    </form>
</body>
</html>
