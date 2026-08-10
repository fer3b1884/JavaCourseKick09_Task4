<%--
  Created by IntelliJ IDEA.
  User: Professional
  Date: 03.08.2026
  Time: 18:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>500</title>
</head>
<body>
Request from: ${pageContext.errorData.requestURI} is failed <br/>
Servlet name: ${pageContext.errorData.servletName} <br/>
Status code: ${pageContext.errorData.statusCode} <br/>
Exception: ${pageContext.exception} <br/>
Exception message: ${pageContext.exception.message} <br/>
<br/><br/><br/>
Message from exception: ${error_msg}
</body>
</html>
