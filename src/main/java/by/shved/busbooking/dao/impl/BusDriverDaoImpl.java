package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.BusDriverDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.BusDriver;
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

public class BusDriverDaoImpl implements BusDriverDao {
    private static final Logger logger = LogManager.getLogger(BusDriverDaoImpl.class);
    private static final String FIND_ALL = "SELECT * FROM drivers";
    private static final String FIND_BY_ID = "SELECT * FROM drivers WHERE id = ?";
    private static final String INSERT = """
            INSERT INTO drivers (last_name, first_name, patronymic, experience_years, phone_number, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE = """
            UPDATE drivers SET last_name = ?, first_name = ?, patronymic = ?, experience_years = ?,
            phone_number = ?, status = ? WHERE id = ?
            """;
    private static final String DELETE_BY_ID = "DELETE FROM drivers WHERE id = ?";

    @Override
    public boolean create(BusDriver driver) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setDriverParameters(statement, driver);
            int affected = statement.executeUpdate();
            if (affected == 0) {
                return false;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    driver.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to create driver: {}", driver, e);
            throw new DaoException("Failed to create driver", e);
        }
    }

    @Override
    public List<BusDriver> findAll() throws DaoException {
        List<BusDriver> drivers = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                drivers.add(EntityMapper.mapBusDriver(resultSet));
            }
            return drivers;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find all drivers", e);
            throw new DaoException("Failed to find all drivers", e);
        }
    }

    @Override
    public Optional<BusDriver> findEntityById(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapBusDriver(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find driver by id: {}", id, e);
            throw new DaoException("Failed to find driver by id", e);
        }
    }

    @Override
    public BusDriver update(BusDriver driver) throws DaoException {
        Optional<BusDriver> old = findEntityById(driver.getId());
        if (old.isEmpty()) {
            return null;
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setDriverParameters(statement, driver);
            statement.setInt(7, driver.getId());
            statement.executeUpdate();
            return old.get();
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update driver: {}", driver.getId(), e);
            throw new DaoException("Failed to update driver", e);
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
            logger.error("Failed to delete driver by id: {}", id, e);
            throw new DaoException("Failed to delete driver", e);
        }
    }

    private void setDriverParameters(PreparedStatement ps, BusDriver driver) throws SQLException {
        ps.setString(1, driver.getLastName());
        ps.setString(2, driver.getFirstName());
        ps.setString(3, driver.getPatronymic());
        ps.setInt(4, driver.getExperienceYears());
        ps.setString(5, driver.getPhoneNumber());
        ps.setString(6, driver.getStatus());
    }
}
