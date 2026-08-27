<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Profile</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/controller">
        <input type="hidden" name="command" value="update_profile">

        <label>Login</label>
        <input type="text" value="<c:out value='${profile.login}'/>" disabled>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required value="<c:out value='${profile.email}'/>">

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" name="lastName" required value="<c:out value='${profile.lastName}'/>">

        <label for="firstName">First Name</label>
        <input type="text" id="firstName" name="firstName" required value="<c:out value='${profile.firstName}'/>">

        <label for="patronymic">Patronymic</label>
        <input type="text" id="patronymic" name="patronymic" value="<c:out value='${profile.patronymic}'/>">

        <label for="password">New Password (leave blank to keep current)</label>
        <input type="password" id="password" name="password" minlength="4">

        <p style="margin-top: 16px;">
            <button type="submit">Save</button>
        </p>
    </form>
</div>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>