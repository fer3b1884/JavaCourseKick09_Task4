package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.UserDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.User;
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

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private static final String FIND_ALL = """
            SELECT u.id,
                   u.login,
                   u.password_hash,
                   u.email,
                   u.last_name,
                   u.first_name,
                   u.patronymic,
                   r.id AS role_id,
                   r.name AS role_name
            FROM users u
            JOIN roles r ON u.role_id = r.id
            """;
    private static final String FIND_BY_ID = """
            SELECT u.id,
                   u.login,
                   u.password_hash,
                   u.email,
                   u.last_name,
                   u.first_name,
                   u.patronymic,
                   r.id AS role_id,
                   r.name AS role_name
            FROM users u
            JOIN roles r ON u.role_id = r.id
            WHERE u.id = ?
            """;
    private static final String FIND_BY_LOGIN = """
            SELECT u.id,
                   u.login,
                   u.password_hash,
                   u.email,
                   u.last_name,
                   u.first_name,
                   u.patronymic,
                   r.id AS role_id,
                   r.name AS role_name
            FROM users u
            JOIN roles r ON u.role_id = r.id
            WHERE u.login = ?          
            """;
    private static final String FIND_BY_EMAIL = """
            SELECT u.id,
                   u.login,
                   u.password_hash,
                   u.email,
                   u.last_name,
                   u.first_name,
                   u.patronymic,
                   r.id AS role_id,
                   r.name AS role_name
            FROM users u
            JOIN roles r ON u.role_id = r.id
            WHERE u.email = ?
            """;
    private static final String INSERT = """
            INSERT INTO users
                (login, password_hash, email, last_name,
                 first_name, patronymic, role_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE = """
            UPDATE users
            SET login = ?,
                password_hash = ?,
                email = ?,
                last_name = ?,
                first_name = ?,
                patronymic = ?,
                role_id = ?
            WHERE id = ?
            """;
    private static final String DELETE_BY_ID = """
            DELETE FROM users
            WHERE id = ?
            """;
    private static final String EXISTS_BY_LOGIN = """
            SELECT 1
            FROM users
            WHERE login = ?
            """;
    private static final String EXISTS_BY_EMAIL = """
            SELECT 1
            FROM users
            WHERE email = ?
            """;

    @Override
    public boolean create(User user) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setUserParameters(statement, user);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to insert user: {}", user.getLogin(), e);
            throw new DaoException("Failed to insert user", e);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(EntityMapper.mapUser(resultSet));
            }
            return users;
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find all users", e);
            throw new DaoException("Failed to find all users", e);
        }
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapUser(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find user by id: {}", id, e);
            throw new DaoException("Failed to find user by id", e);
        }
    }

    @Override
    public User update(User user) throws DaoException {
        Optional<User> oldUser = findEntityById(user.getId());
        if (oldUser.isEmpty()) {
            return null;
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setUserParameters(statement, user);
            statement.setInt(8, user.getId());
            statement.executeUpdate();
            return oldUser.get();
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to update user: {}", user.getId(), e);
            throw new DaoException("Failed to update user", e);
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
            logger.error("Failed to delete user by id: {}", id, e);
            throw new DaoException("Failed to delete user", e);
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapUser(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find user by login: {}", login, e);
            throw new DaoException("Failed to find user by login", e);
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(EntityMapper.mapUser(resultSet));
                }
                return Optional.empty();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to find user by email: {}", email, e);
            throw new DaoException("Failed to find user by email", e);
        }
    }

    @Override
    public boolean existsByLogin(String login) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_LOGIN)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to check user login: {}", login, e);
            throw new DaoException("Failed to check user login", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws DaoException {
        try (ProxyConnection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (ConnectionPoolException e) {
            logger.error("Failed to obtain database connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to check user email: {}", email, e);
            throw new DaoException("Failed to check user email", e);
        }
    }

    private void setUserParameters(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPasswordHash());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getFirstName());
        statement.setString(6, user.getPatronymic());
        statement.setInt(7, user.getRole().getId());
    }
}