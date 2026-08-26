package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.BookingDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.Booking;
import by.shved.busbooking.exception.ConnectionPoolException;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.pool.ConnectionPool;
import by.shved.busbooking.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDaoImpl implements BookingDao {
    private static final Logger logger = LogManager.getLogger(BookingDaoImpl.class);
    private static final BookingDaoImpl INSTANCE = new BookingDaoImpl();
    private static final String FIND_ALL = "SELECT * FROM bookings";
    private static final String FIND_BY_ID = "SELECT * FROM bookings WHERE id = ?";
    private static final String INSERT = "INSERT INTO bookings (user_id, trip_id, seat_number, status) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE bookings SET user_id = ?, trip_id = ?, seat_number = ?, status = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM bookings WHERE id = ?";
    private static final String FIND_BY_USER = "SELECT * FROM bookings WHERE user_id = ?";
    private static final String FIND_BY_TRIP = "SELECT * FROM bookings WHERE trip_id = ?";
    private static final String IS_SEAT_TAKEN = "SELECT 1 FROM bookings WHERE trip_id = ? AND seat_number = ? AND status != 'CANCELLED'";

    private BookingDaoImpl() { }

    public static BookingDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean create(Booking booking) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getTrip().getId());
            statement.setInt(3, booking.getSeatNumber());
            statement.setString(4, booking.getStatus());
            int affected = statement.executeUpdate();
            if (affected == 0) {
                return false;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to create booking: {}", booking, e);
            throw new DaoException("Failed to create booking", e);
        }
    }

    @Override
    public List<Booking> findAll() throws DaoException {
        List<Booking> bookings = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookings.add(EntityMapper.mapBooking(resultSet));
            }
            return bookings;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find all bookings", e);
            throw new DaoException("Failed to find all bookings", e);
        }
    }

    @Override
    public Optional<Booking> findEntityById(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapBooking(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find booking by id: {}", id, e);
            throw new DaoException("Failed to find booking by id", e);
        }
    }

    @Override
    public boolean update(Booking booking) throws DaoException {
        Optional<Booking> old = findEntityById(booking.getId());
        if (old.isEmpty()) {
            return false;
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getTrip().getId());
            statement.setInt(3, booking.getSeatNumber());
            statement.setString(4, booking.getStatus());
            statement.setInt(5, booking.getId());
            statement.executeUpdate();
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update booking: {}", booking.getId(), e);
            throw new DaoException("Failed to update booking", e);
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
            logger.error("Failed to delete booking by id: {}", id, e);
            throw new DaoException("Failed to delete booking", e);
        }
    }

    @Override
    public List<Booking> findByTrip(Integer tripId) throws DaoException {
        List<Booking> bookings = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_TRIP)) {
            statement.setInt(1, tripId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(EntityMapper.mapBooking(resultSet));
                }
            }
            return bookings;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find bookings by trip id: {}", tripId, e);
            throw new DaoException("Failed to find bookings by trip", e);
        }
    }

    @Override
    public List<Booking> findByUserId(Integer userId) throws DaoException {
        List<Booking> bookings = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USER)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(EntityMapper.mapBooking(resultSet));
                }
            }
            return bookings;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find bookings by user id: {}", userId, e);
            throw new DaoException("Failed to find bookings by user", e);
        }
    }

    @Override
    public boolean isSeatTaken(Integer tripId, Integer seatNumber) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(IS_SEAT_TAKEN)) {
            statement.setInt(1, tripId);
            statement.setInt(2, seatNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to check if seat is taken: trip={}, seat={}", tripId, seatNumber, e);
            throw new DaoException("Failed to check seat availability", e);
        }
    }
}
