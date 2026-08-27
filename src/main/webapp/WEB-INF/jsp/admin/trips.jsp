<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Trip Management</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/controller" id="tripForm">
        <input type="hidden" name="command" value="save_trip">
        <input type="hidden" name="id" id="tripId">

        <label for="routeId">Route</label>
        <select id="routeId" name="routeId" required>
            <c:forEach var="route" items="${routes}">
                <option value="${route.id}"><c:out value="${route.routeNumber}"/> — <c:out value="${route.departureCity}"/>-<c:out value="${route.arrivalCity}"/></option>
            </c:forEach>
        </select>

        <label for="busId">Bus</label>
        <select id="busId" name="busId" required>
            <c:forEach var="bus" items="${buses}">
                <option value="${bus.id}"><c:out value="${bus.busNumber}"/> (<c:out value="${bus.brand}"/>)</option>
            </c:forEach>
        </select>

        <label for="departureTime">Departure</label>
        <input type="datetime-local" id="departureTime" name="departureTime" required>

        <label for="arrivalTime">Arrival</label>
        <input type="datetime-local" id="arrivalTime" name="arrivalTime" required>

        <label for="price">Price (BYN)</label>
        <input type="number" id="price" name="price" step="0.01" min="0" required>

        <label for="availableSeats">Available Seats</label>
        <input type="number" id="availableSeats" name="availableSeats" min="1" required>

        <p style="margin-top: 16px;">
            <button type="submit">Save</button>
            <button type="button" onclick="clearTripForm()">Clear</button>
        </p>
    </form>
</div>

<div class="card">
    <table>
        <thead>
        <tr>
            <th>Route</th>
            <th>Bus</th>
            <th>Departure</th>
            <th>Arrival</th>
            <th>Price</th>
            <th>Seats</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="trip" items="${trips}">
            <tr>
                <td><c:out value="${trip.route.routeNumber}"/></td>
                <td><c:out value="${trip.bus.busNumber}"/></td>
                <td>${trip.departureTime.year}-${trip.departureTime.monthValue < 10 ? '0' : ''}${trip.departureTime.monthValue}-${trip.departureTime.dayOfMonth < 10 ? '0' : ''}${trip.departureTime.dayOfMonth}T${trip.departureTime.hour < 10 ? '0' : ''}${trip.departureTime.hour}:${trip.departureTime.minute < 10 ? '0' : ''}${trip.departureTime.minute}</td>
                <td>${trip.arrivalTime.year}-${trip.arrivalTime.monthValue < 10 ? '0' : ''}${trip.arrivalTime.monthValue}-${trip.arrivalTime.dayOfMonth < 10 ? '0' : ''}${trip.arrivalTime.dayOfMonth}T${trip.arrivalTime.hour < 10 ? '0' : ''}${trip.arrivalTime.hour}:${trip.arrivalTime.minute < 10 ? '0' : ''}${trip.arrivalTime.minute}</td>
                <td><c:out value="${trip.price}"/></td>
                <td><c:out value="${trip.availableSeats}"/></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/controller" style="display:inline">
                        <input type="hidden" name="command" value="delete_trip">
                        <input type="hidden" name="id" value="${trip.id}">
                        <button type="submit" class="danger">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function clearTripForm() {
        document.getElementById('tripForm').reset();
        document.getElementById('tripId').value = '';
    }
</script>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>