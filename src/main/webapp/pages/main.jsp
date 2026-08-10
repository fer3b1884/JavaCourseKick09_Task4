<%--
  Created by IntelliJ IDEA.
  User: Professional
  Date: 29.07.2026
  Time: 20:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
Hello {forward} ${user}!
<hr/>
Hi {redirect/forward} = ${user_name}
<hr/>
<form action="controller">
    <input type="hidden" name="command" value="logout"/>
    <input type="submit" value="logout"/>
</form>
</body>
</html>
