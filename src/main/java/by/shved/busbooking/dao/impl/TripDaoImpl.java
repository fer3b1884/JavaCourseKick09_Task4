package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.TripDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.Trip;
import by.shved.busbooking.exception.ConnectionPoolException;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.pool.ConnectionPool;
import by.shved.busbooking.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripDaoImpl implements TripDao {
    private static final Logger logger = LogManager.getLogger(TripDaoImpl.class);
    private static final TripDaoImpl INSTANCE = new TripDaoImpl();
    private static final String TRIP_SELECT = """
            SELECT t.id AS trip_id, t.departure_time, t.arrival_time, t.price, t.available_seats,
                   r.id AS route_id, r.route_number, r.departure_city, r.arrival_city,
                   b.id AS bus_id, b.bus_number, b.brand, b.start_operation_year, b.mileage,
                   b.seat_count, b.status AS bus_status,
                   d.id AS driver_id, d.first_name AS driver_first_name, d.last_name AS driver_last_name,
                   d.patronymic AS driver_patronymic, d.experience_years AS driver_experience_years,
                   d.phone_number AS driver_phone, d.status AS driver_status
            FROM trips t
            JOIN routes r ON t.route_id = r.id
            JOIN buses b ON t.bus_id = b.id
            JOIN drivers d ON b.driver_id = d.id
            """;
    private static final String FIND_ALL = TRIP_SELECT + " ORDER BY t.departure_time";
    private static final String FIND_BY_ID = TRIP_SELECT + " WHERE t.id = ?";
    private static final String FIND_BY_ROUTE = TRIP_SELECT + " WHERE t.route_id = ? ORDER BY t.departure_time";
    private static final String FIND_BY_DEPARTURE_DATE = TRIP_SELECT + " WHERE DATE(t.departure_time) = ? ORDER BY t.departure_time";
    private static final String FIND_AVAILABLE = TRIP_SELECT + " WHERE t.available_seats > 0 ORDER BY t.departure_time";
    private static final String INSERT = """
            INSERT INTO trips (route_id, bus_id, departure_time, arrival_time, price, available_seats)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE = """
            UPDATE trips
            SET route_id = ?, bus_id = ?, departure_time = ?, arrival_time = ?, price = ?, available_seats = ?
            WHERE id = ?
            """;
    private static final String DELETE_BY_ID = "DELETE FROM trips WHERE id = ?";
    private static final String UPDATE_AVAILABLE_SEATS = "UPDATE trips SET available_seats = ? WHERE id = ?";

    private TripDaoImpl() { }

    public static TripDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean create(Trip trip) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setTripParameters(statement, trip);
            int affected = statement.executeUpdate();
            if (affected == 0) {
                return false;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trip.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to create trip: {}", trip, e);
            throw new DaoException("Failed to create trip", e);
        }
    }

    @Override
    public List<Trip> findAll() throws DaoException {
        List<Trip> trips = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                trips.add(EntityMapper.mapTrip(resultSet));
            }
            return trips;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find all trips", e);
            throw new DaoException("Failed to find all trips", e);
        }
    }

    @Override
    public Optional<Trip> findEntityById(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapTrip(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find trip by id: {}", id, e);
            throw new DaoException("Failed to find trip by id", e);
        }
    }

    @Override
    public boolean update(Trip trip) throws DaoException {
        Optional<Trip> old = findEntityById(trip.getId());
        if (old.isEmpty()) {
            return false;
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setTripParameters(statement, trip);
            statement.setInt(7, trip.getId());
            statement.executeUpdate();
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update trip: {}", trip.getId(), e);
            throw new DaoException("Failed to update trip", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to delete trip by id: {}", id, e);
            throw new DaoException("Failed to delete trip", e);
        }
    }

    @Override
    public List<Trip> findByRoute(Integer routeId) throws DaoException {
        List<Trip> trips = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ROUTE)) {
            statement.setInt(1, routeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trips.add(EntityMapper.mapTrip(resultSet));
                }
            }
            return trips;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find trips by route id: {}", routeId, e);
            throw new DaoException("Failed to find trips by route", e);
        }
    }

    @Override
    public List<Trip> findByDepartureDate(LocalDate date) throws DaoException {
        List<Trip> trips = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_DEPARTURE_DATE)) {
            statement.setDate(1, Date.valueOf(date));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trips.add(EntityMapper.mapTrip(resultSet));
                }
            }
            return trips;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find trips by departure date: {}", date, e);
            throw new DaoException("Failed to find trips by date", e);
        }
    }

    @Override
    public List<Trip> findAvailableTrips() throws DaoException {
        List<Trip> trips = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_AVAILABLE);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                trips.add(EntityMapper.mapTrip(resultSet));
            }
            return trips;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find available trips", e);
            throw new DaoException("Failed to find available trips", e);
        }
    }

    @Override
    public boolean updateAvailableSeats(Integer tripId, Integer availableSeats) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_AVAILABLE_SEATS)) {
            statement.setInt(1, availableSeats);
            statement.setInt(2, tripId);
            return statement.executeUpdate() > 0;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update available seats for trip {} to {}", tripId, availableSeats, e);
            throw new DaoException("Failed to update available seats", e);
        }
    }

    private void setTripParameters(PreparedStatement statement, Trip trip) throws SQLException {
        statement.setInt(1, trip.getRoute().getId());
        statement.setInt(2, trip.getBus().getId());
        statement.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
        statement.setTimestamp(4, Timestamp.valueOf(trip.getArrivalTime()));
        statement.setBigDecimal(5, trip.getPrice());
        statement.setInt(6, trip.getAvailableSeats());
    }
}
