<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login — BusBooking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container" style="margin-top: 40px;">
    <div class="card" style="max-width: 420px; margin: 0 auto;">
        <h2>Sign In</h2>
        <c:if test="${not empty login_msg}">
            <div class="msg msg-error"><c:out value="${login_msg}"/></div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="msg msg-success"><c:out value="${message}"/></div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="login">
            <label for="login">Login</label>
            <input type="text" id="login" name="login" required minlength="3" maxlength="30"
                   pattern="[a-zA-Z0-9_]{3,30}" value="<c:out value='${login}'/>">

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required minlength="4">

            <p style="margin-top: 16px;">
                <button type="submit">Sign In</button>
            </p>
        </form>
        <p>Don't have an account?
            <a href="${pageContext.request.contextPath}/controller?command=register">Register</a>
        </p>
    </div>
</div>
</body>
</html>