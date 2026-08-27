<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Route Management</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/controller" id="routeForm">
        <input type="hidden" name="command" value="save_route">
        <input type="hidden" name="id" id="routeId">

        <label for="routeNumber">Route Number</label>
        <input type="text" id="routeNumber" name="routeNumber" required>

        <label for="departureCity">Departure City</label>
        <input type="text" id="departureCity" name="departureCity" required>

        <label for="arrivalCity">Arrival City</label>
        <input type="text" id="arrivalCity" name="arrivalCity" required>

        <p style="margin-top: 16px;">
            <button type="submit">Save</button>
            <button type="button" onclick="clearRouteForm()">Clear</button>
        </p>
    </form>
</div>

<div class="card">
    <table>
        <thead>
        <tr>
            <th>Route #</th>
            <th>From</th>
            <th>To</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="route" items="${routes}">
            <tr>
                <td><c:out value="${route.routeNumber}"/></td>
                <td><c:out value="${route.departureCity}"/></td>
                <td><c:out value="${route.arrivalCity}"/></td>
                <td>
                    <button type="button" onclick="editRoute('${route.id}','${route.routeNumber}','${route.departureCity}','${route.arrivalCity}')">Edit</button>
                    <form method="post" action="${pageContext.request.contextPath}/controller" style="display:inline">
                        <input type="hidden" name="command" value="delete_route">
                        <input type="hidden" name="id" value="${route.id}">
                        <button type="submit" class="danger">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function editRoute(id, routeNumber, departureCity, arrivalCity) {
        document.getElementById('routeId').value = id;
        document.getElementById('routeNumber').value = routeNumber;
        document.getElementById('departureCity').value = departureCity;
        document.getElementById('arrivalCity').value = arrivalCity;
    }
    function clearRouteForm() {
        document.getElementById('routeForm').reset();
        document.getElementById('routeId').value = '';
    }
</script>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>