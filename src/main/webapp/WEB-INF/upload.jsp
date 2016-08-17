<%--
  Created by IntelliJ IDEA.
  User: artemypestretsov
  Date: 8/17/16
  Time: 1:47 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>File Upload</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<form method="POST" action="upload" enctype="multipart/form-data" >
    File:
    <input type="file" name="file" id="file" /> <br/>
    Destination:
    <input type="text" value="/tmp" name="destination"/>
    <br>
    <input type="submit" value="Upload" name="upload" id="upload" />
</form>
</body>
</html>