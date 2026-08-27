<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>My Bookings</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <c:choose>
        <c:when test="${empty bookings}">
            <p>You have no bookings.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Route</th>
                    <th>Direction</th>
                    <th>Departure</th>
                    <th>Seat</th>
                    <th>Status</th>
                    <th>Booking Date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="booking" items="${bookings}">
                    <tr>
                        <td><c:out value="${booking.trip.route.routeNumber}"/></td>
                        <td><c:out value="${booking.trip.route.departureCity}"/> — <c:out value="${booking.trip.route.arrivalCity}"/></td>
                        <td>${booking.trip.departureTime.dayOfMonth}.${booking.trip.departureTime.monthValue}.${booking.trip.departureTime.year} ${booking.trip.departureTime.hour}:${booking.trip.departureTime.minute < 10 ? '0' : ''}${booking.trip.departureTime.minute}</td>
                        <td><c:out value="${booking.seatNumber}"/></td>
                        <td><c:out value="${booking.status}"/></td>
                        <td>${booking.bookingDate.dayOfMonth}.${booking.bookingDate.monthValue}.${booking.bookingDate.year}</td>
                        <td>
                            <c:if test="${booking.status eq 'ACTIVE'}">
                                <form method="post" action="${pageContext.request.contextPath}/controller" style="display:inline">
                                    <input type="hidden" name="command" value="cancel_booking">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <button type="submit" class="danger">Cancel</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>