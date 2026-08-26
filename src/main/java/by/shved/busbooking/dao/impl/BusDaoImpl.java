package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.BusDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.Bus;
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

public class BusDaoImpl implements BusDao {
    private static final Logger logger = LogManager.getLogger(BusDaoImpl.class);
    private static final String FIND_ALL = "SELECT * FROM buses";
    private static final String FIND_BY_ID = "SELECT * FROM buses WHERE id = ?";
    private static final String INSERT = """
            INSERT INTO buses (bus_number, brand, driver_id, start_operation_year, mileage, seat_count, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE = """
            UPDATE buses SET bus_number = ?, brand = ?, driver_id = ?, start_operation_year = ?,
            mileage = ?, seat_count = ?, status = ? WHERE id = ?
            """;
    private static final String DELETE_BY_ID = "DELETE FROM buses WHERE id = ?";
    private static final String FIND_BY_ROUTE_NUMBER =
            "SELECT b.* FROM buses b JOIN trips t ON b.id = t.bus_id " +
                    "JOIN routes r ON t.route_id = r.id WHERE r.route_number = ?";
    private static final String FIND_OLDER_THAN =
            "SELECT * FROM buses WHERE EXTRACT(YEAR FROM CURRENT_DATE) - start_operation_year > ?";
    private static final String FIND_MILEAGE_GREATER =
            "SELECT * FROM buses WHERE mileage > ?";

    public BusDaoImpl() {
    }

    @Override
    public boolean create(Bus bus) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setBusParameters(statement, bus);
            int affected = statement.executeUpdate();
            if (affected == 0) {
                return false;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bus.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to create bus: {}", bus, e);
            throw new DaoException("Failed to create bus", e);
        }
    }

    @Override
    public List<Bus> findAll() throws DaoException {
        List<Bus> buses = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                buses.add(EntityMapper.mapBus(resultSet));
            }
            return buses;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find all buses", e);
            throw new DaoException("Failed to find all buses", e);
        }
    }

    @Override
    public Optional<Bus> findEntityById(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapBus(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find bus by id: {}", id, e);
            throw new DaoException("Failed to find bus by id", e);
        }
    }

    @Override
    public Bus update(Bus bus) throws DaoException {
        Optional<Bus> old = findEntityById(bus.getId());
        if (old.isEmpty()) {
            return null;
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setBusParameters(statement, bus);
            statement.setInt(8, bus.getId());
            statement.executeUpdate();
            return old.get();
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update bus: {}", bus.getId(), e);
            throw new DaoException("Failed to update bus", e);
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
            logger.error("Failed to delete bus by id: {}", id, e);
            throw new DaoException("Failed to delete bus", e);
        }
    }

    @Override
    public List<Bus> findByRouteNumber(String routeNumber) throws DaoException {
        List<Bus> buses = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ROUTE_NUMBER)) {
            statement.setString(1, routeNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    buses.add(EntityMapper.mapBus(resultSet));
                }
            }
            return buses;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find buses by route number: {}", routeNumber, e);
            throw new DaoException("Failed to find buses by route number", e);
        }
    }

    @Override
    public List<Bus> findOlderThanYears(int years) throws DaoException {
        List<Bus> buses = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_OLDER_THAN)) {
            statement.setInt(1, years);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    buses.add(EntityMapper.mapBus(resultSet));
                }
            }
            return buses;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find buses older than {} years", years, e);
            throw new DaoException("Failed to find buses older than years", e);
        }
    }

    @Override
    public List<Bus> findByMileageGreaterThan(int mileage) throws DaoException {
        List<Bus> buses = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MILEAGE_GREATER)) {
            statement.setInt(1, mileage);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    buses.add(EntityMapper.mapBus(resultSet));
                }
            }
            return buses;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find buses with mileage > {}", mileage, e);
            throw new DaoException("Failed to find buses with mileage greater", e);
        }
    }

    private void setBusParameters(PreparedStatement ps, Bus bus) throws SQLException {
        ps.setString(1, bus.getBusNumber());
        ps.setString(2, bus.getBrand());
        ps.setInt(3, bus.getDriver().getId());
        ps.setInt(4, bus.getStartOperationYear());
        ps.setInt(5, bus.getMileage());
        ps.setInt(6, bus.getSeatCount());
        ps.setString(7, bus.getStatus());
    }
}