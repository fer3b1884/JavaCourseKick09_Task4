package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.BusRouteDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.BusRoute;
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

public class BusRouteDaoImpl implements BusRouteDao {
    private static final Logger logger = LogManager.getLogger(BusRouteDaoImpl.class);
    private static final String FIND_ALL = "SELECT * FROM routes";
    private static final String FIND_BY_ID = "SELECT * FROM routes WHERE id = ?";
    private static final String INSERT = "INSERT INTO routes (route_number, departure_city, arrival_city) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE routes SET route_number = ?, departure_city = ?, arrival_city = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM routes WHERE id = ?";

    @Override
    public boolean create(BusRoute route) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, route.getRouteNumber());
            statement.setString(2, route.getDepartureCity());
            statement.setString(3, route.getArrivalCity());
            int affected = statement.executeUpdate();
            if (affected == 0) {
                return false;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    route.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to create route: {}", route, e);
            throw new DaoException("Failed to create route", e);
        }
    }

    @Override
    public List<BusRoute> findAll() throws DaoException {
        List<BusRoute> routes = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                routes.add(EntityMapper.mapBusRoute(resultSet));
            }
            return routes;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find all routes", e);
            throw new DaoException("Failed to find all routes", e);
        }
    }

    @Override
    public Optional<BusRoute> findEntityById(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapBusRoute(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find route by id: {}", id, e);
            throw new DaoException("Failed to find route by id", e);
        }
    }

    @Override
    public BusRoute update(BusRoute route) throws DaoException {
        Optional<BusRoute> old = findEntityById(route.getId());
        if (old.isEmpty()) {
            return null;
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, route.getRouteNumber());
            statement.setString(2, route.getDepartureCity());
            statement.setString(3, route.getArrivalCity());
            statement.setInt(4, route.getId());
            statement.executeUpdate();
            return old.get();
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update route: {}", route.getId(), e);
            throw new DaoException("Failed to update route", e);
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
            logger.error("Failed to delete route by id: {}", id, e);
            throw new DaoException("Failed to delete route", e);
        }
    }
}
