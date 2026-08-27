<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Dashboard</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>
    <p>Welcome, <strong><c:out value="${sessionScope.user.firstName}"/> <c:out value="${sessionScope.user.lastName}"/></strong>!</p>
    <p>Role: <c:out value="${sessionScope.user.role}"/></p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/controller?command=trips">View available trips & book</a></li>
        <li><a href="${pageContext.request.contextPath}/controller?command=bookings">My bookings</a></li>
        <li><a href="${pageContext.request.contextPath}/controller?command=profile">Edit profile</a></li>
        <c:if test="${sessionScope.user.role eq 'ADMIN'}">
            <li><a href="${pageContext.request.contextPath}/controller?command=admin_buses">Manage buses</a></li>
            <li><a href="${pageContext.request.contextPath}/controller?command=admin_routes">Manage routes</a></li>
            <li><a href="${pageContext.request.contextPath}/controller?command=admin_trips">Manage trips</a></li>
            <li><a href="${pageContext.request.contextPath}/controller?command=admin_reports">Bus reports</a></li>
        </c:if>
    </ul>
</div>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>