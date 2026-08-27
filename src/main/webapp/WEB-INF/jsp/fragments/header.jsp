<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bus Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <div class="container">
        <h1>BusBooking</h1>
        <c:if test="${not empty sessionScope.user}">
            <nav>
                <a href="${pageContext.request.contextPath}/controller?command=main">Home</a>
                <a href="${pageContext.request.contextPath}/controller?command=trips">Trips</a>
                <a href="${pageContext.request.contextPath}/controller?command=bookings">My Bookings</a>
                <a href="${pageContext.request.contextPath}/controller?command=profile">Profile</a>
                <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/controller?command=admin_buses">Buses</a>
                    <a href="${pageContext.request.contextPath}/controller?command=admin_routes">Routes</a>
                    <a href="${pageContext.request.contextPath}/controller?command=admin_trips">Trips (Admin)</a>
                    <a href="${pageContext.request.contextPath}/controller?command=admin_reports">Reports</a>
                </c:if>
            </nav>
            <span class="user-info"><c:out value="${sessionScope.user.login}"/></span>
            <form method="post" action="${pageContext.request.contextPath}/controller" class="logout-form">
                <input type="hidden" name="command" value="logout">
                <button type="submit">Logout</button>
            </form>
        </c:if>
    </div>
</header>
<main class="container">