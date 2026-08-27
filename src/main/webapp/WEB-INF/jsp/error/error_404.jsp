<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>404 - Not Found</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="card">
        <h2>404 - Page Not Found</h2>
        <p>The requested page could not be found.</p>
        <a href="${pageContext.request.contextPath}/index.jsp">Go to Home</a>
    </div>
</div>
</body>
</html>