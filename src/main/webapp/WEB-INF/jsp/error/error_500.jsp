<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>500 - Internal Server Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="card">
        <h2>500 - Internal Server Error</h2>
        <p>Request from: ${pageContext.errorData.requestURI}</p>
        <p>Servlet name: ${pageContext.errorData.servletName}</p>
        <p>Status code: ${pageContext.errorData.statusCode}</p>
        <p>Exception: ${pageContext.exception}</p>
        <p>Exception message: ${pageContext.exception.message}</p>
        <p><strong>Error message:</strong> ${error_msg}</p>
        <a href="${pageContext.request.contextPath}/index.jsp">Go to Home</a>
    </div>
</div>
</body>
</html>