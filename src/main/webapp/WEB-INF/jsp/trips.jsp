<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Available Trips</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <c:choose>
        <c:when test="${empty trips}">
            <p>No trips available.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Route</th>
                    <th>Direction</th>
                    <th>Bus</th>
                    <th>Driver</th>
                    <th>Departure</th>
                    <th>Arrival</th>
                    <th>Price</th>
                    <th>Seats</th>
                    <th>Book</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="trip" items="${trips}">
                    <tr>
                        <td><c:out value="${trip.route.routeNumber}"/></td>
                        <td><c:out value="${trip.route.departureCity}"/> — <c:out value="${trip.route.arrivalCity}"/></td>
                        <td><c:out value="${trip.bus.busNumber}"/> (<c:out value="${trip.bus.brand}"/>)</td>
                        <td><c:out value="${trip.bus.driver.shortName}"/></td>
                        <td>${trip.departureTime.dayOfMonth}.${trip.departureTime.monthValue}.${trip.departureTime.year} ${trip.departureTime.hour}:${trip.departureTime.minute < 10 ? '0' : ''}${trip.departureTime.minute}</td>
                        <td>${trip.arrivalTime.dayOfMonth}.${trip.arrivalTime.monthValue}.${trip.arrivalTime.year} ${trip.arrivalTime.hour}:${trip.arrivalTime.minute < 10 ? '0' : ''}${trip.arrivalTime.minute}</td>
                        <td><c:out value="${trip.price}"/> BYN</td>
                        <td><c:out value="${trip.availableSeats}"/></td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/controller">
                                <input type="hidden" name="command" value="book_trip">
                                <input type="hidden" name="tripId" value="${trip.id}">
                                <input type="number" name="seatNumber" min="1"
                                       max="${trip.bus.seatCount}" required style="width:70px">
                                <button type="submit">Book</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>