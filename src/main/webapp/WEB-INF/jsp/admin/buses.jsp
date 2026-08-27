<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="card">
    <h2>Bus Management</h2>
    <c:if test="${not empty message}">
        <div class="msg msg-success"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <h3>Add / Edit Bus</h3>
    <form method="post" action="${pageContext.request.contextPath}/controller" id="busForm">
        <input type="hidden" name="command" value="save_bus">
        <input type="hidden" name="id" id="busId">

        <label for="busNumber">Bus Number</label>
        <input type="text" id="busNumber" name="busNumber" required>

        <label for="brand">Brand</label>
        <input type="text" id="brand" name="brand" required>

        <label for="driverId">Driver</label>
        <select id="driverId" name="driverId" required>
            <c:forEach var="driver" items="${drivers}">
                <option value="${driver.id}"><c:out value="${driver.shortName}"/></option>
            </c:forEach>
        </select>

        <label for="startOperationYear">Year of Operation Start</label>
        <input type="number" id="startOperationYear" name="startOperationYear" min="1950" max="2100" required>

        <label for="mileage">Mileage (km)</label>
        <input type="number" id="mileage" name="mileage" min="0" required>

        <label for="seatCount">Seats</label>
        <input type="number" id="seatCount" name="seatCount" min="1" required>

        <label for="status">Status</label>
        <select id="status" name="status">
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
        </select>

        <p style="margin-top: 16px;">
            <button type="submit">Save</button>
            <button type="button" onclick="clearBusForm()">Clear</button>
        </p>
    </form>
</div>

<div class="card">
    <h3>Bus List</h3>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Brand</th>
            <th>Driver</th>
            <th>Year</th>
            <th>Mileage</th>
            <th>Seats</th>
            <th>Status</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="bus" items="${buses}">
            <tr>
                <td><c:out value="${bus.busNumber}"/></td>
                <td><c:out value="${bus.brand}"/></td>
                <td><c:out value="${bus.driver.shortName}"/></td>
                <td><c:out value="${bus.startOperationYear}"/></td>
                <td><c:out value="${bus.mileage}"/></td>
                <td><c:out value="${bus.seatCount}"/></td>
                <td><c:out value="${bus.status}"/></td>
                <td>
                    <button type="button" onclick="editBus('${bus.id}','${bus.busNumber}','${bus.brand}','${bus.driver.id}','${bus.startOperationYear}','${bus.mileage}','${bus.seatCount}','${bus.status}')">Edit</button>
                    <form method="post" action="${pageContext.request.contextPath}/controller" style="display:inline">
                        <input type="hidden" name="command" value="delete_bus">
                        <input type="hidden" name="id" value="${bus.id}">
                        <button type="submit" class="danger">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function editBus(id, busNumber, brand, driverId, year, mileage, seatCount, status) {
        document.getElementById('busId').value = id;
        document.getElementById('busNumber').value = busNumber;
        document.getElementById('brand').value = brand;
        document.getElementById('driverId').value = driverId;
        document.getElementById('startOperationYear').value = year;
        document.getElementById('mileage').value = mileage;
        document.getElementById('seatCount').value = seatCount;
        document.getElementById('status').value = status;
    }
    function clearBusForm() {
        document.getElementById('busForm').reset();
        document.getElementById('busId').value = '';
    }
</script>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>