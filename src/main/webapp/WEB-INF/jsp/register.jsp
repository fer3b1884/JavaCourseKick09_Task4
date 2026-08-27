<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Registration</h2>
    <c:if test="${not empty errors}">
        <div class="msg msg-error">
            <ul>
                <c:forEach var="err" items="${errors}">
                    <li><c:out value="${err}"/></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/controller" id="registerForm">
        <input type="hidden" name="command" value="add_user">
        <label for="login">Login</label>
        <input type="text" id="login" name="login" required minlength="3" maxlength="30"
               pattern="[a-zA-Z0-9_]{3,30}" value="<c:out value='${login}'/>">

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required minlength="4">

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required value="<c:out value='${email}'/>">

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" name="lastName" required value="<c:out value='${lastName}'/>">

        <label for="firstName">First Name</label>
        <input type="text" id="firstName" name="firstName" required value="<c:out value='${firstName}'/>">

        <label for="patronymic">Patronymic</label>
        <input type="text" id="patronymic" name="patronymic" value="<c:out value='${patronymic}'/>">

        <p style="margin-top: 16px;">
            <button type="submit">Register</button>
            <a href="${pageContext.request.contextPath}/index.jsp">Back</a>
        </p>
    </form>
</div>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>