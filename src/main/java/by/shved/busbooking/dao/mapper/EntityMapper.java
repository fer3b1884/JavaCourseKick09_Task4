package by.shved.busbooking.dao.mapper;

import by.shved.busbooking.entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getString("password_hash"),
                rs.getString("email"),
                rs.getString("last_name"),
                rs.getString("first_name"),
                rs.getString("patronymic"),
                UserRoleType.valueOf(rs.getString("role_name"))
        );
    }

    public static BusDriver mapBusDriver(ResultSet rs) throws SQLException {
        return new BusDriver(
                rs.getInt("driver_id"),
                rs.getString("driver_first_name"),
                rs.getString("driver_last_name"),
                rs.getString("driver_patronymic"),
                rs.getObject("driver_experience_years", Integer.class),
                rs.getString("driver_phone"),
                rs.getString("driver_status")
        );
    }

    public static BusDriver mapBusDriverSimple(ResultSet rs) throws SQLException {
        return new BusDriver(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("patronymic"),
                rs.getObject("experience_years", Integer.class),
                rs.getString("phone_number"),
                rs.getString("status")
        );
    }

    public static Bus mapBus(ResultSet rs) throws SQLException {
        BusDriver driver = new BusDriver(
                rs.getInt("driver_id"),
                rs.getString("driver_first_name"),
                rs.getString("driver_last_name"),
                rs.getString("driver_patronymic"),
                rs.getObject("driver_experience_years", Integer.class),
                rs.getString("driver_phone"),
                rs.getString("driver_status")
        );
        return new Bus(
                rs.getInt("id"),
                rs.getString("bus_number"),
                rs.getString("brand"),
                driver,
                rs.getInt("start_operation_year"),
                rs.getInt("mileage"),
                rs.getInt("seat_count"),
                rs.getString("status")
        );
    }

    public static BusRoute mapBusRoute(ResultSet rs) throws SQLException {
        return new BusRoute(
                rs.getInt("id"),
                rs.getString("route_number"),
                rs.getString("departure_city"),
                rs.getString("arrival_city")
        );
    }

    public static Trip mapTrip(ResultSet rs) throws SQLException {
        BusRoute route = new BusRoute(
                rs.getInt("route_id"),
                rs.getString("route_number"),
                rs.getString("departure_city"),
                rs.getString("arrival_city")
        );
        Bus bus = new Bus(
                rs.getInt("bus_id"),
                rs.getString("bus_number"),
                rs.getString("brand"),
                mapBusDriver(rs),
                rs.getInt("start_operation_year"),
                rs.getInt("mileage"),
                rs.getInt("seat_count"),
                rs.getString("bus_status")
        );
        return new Trip(
                rs.getInt("trip_id"),
                route,
                bus,
                toLocalDateTime(rs.getTimestamp("departure_time")),
                toLocalDateTime(rs.getTimestamp("arrival_time")),
                rs.getBigDecimal("price"),
                rs.getInt("available_seats")
        );
    }

    public static Booking mapBooking(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setLogin(rs.getString("user_login"));
        user.setLastName(rs.getString("user_last_name"));
        user.setFirstName(rs.getString("user_first_name"));
        Trip trip = mapTrip(rs);
        return new Booking(
                rs.getInt("booking_id"),
                user,
                trip,
                rs.getInt("seat_number"),
                toLocalDateTime(rs.getTimestamp("booking_date")),
                rs.getString("booking_status")
        );
    }

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
