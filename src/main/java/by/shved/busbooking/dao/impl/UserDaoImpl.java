package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.BaseDao;
import by.shved.busbooking.dao.UserDao;
import by.shved.busbooking.dao.mapper.EntityMapper;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRole;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.pool.ConnectionPool;
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
    private final ConnectionPool connectionPool;

    public UserDaoImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public boolean create(User user) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS)
            ) {
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
            }
        } catch (SQLException e) {
            logger.error("Failed to insert user: {}", user.getLogin(), e);
            throw new DaoException("Failed to insert user", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        Connection connection = null;
        List<User> users = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = EntityMapper.mapUser(resultSet);
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException e) {
            logger.error("Failed to find all users", e);
            throw new DaoException("Failed to find all users", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = EntityMapper.mapUser(resultSet);
                        return Optional.of(user);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find user by id: {}", id, e);
            throw new DaoException("Failed to find user by id", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public User update(User user) throws DaoException {
        Connection connection = null;
        try {
            Optional<User> oldUser = findEntityById(user.getId());
            if (oldUser.isEmpty()) {
                return null;
            }
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
                setUserParameters(statement, user);
                statement.setInt(8, user.getId());
                statement.executeUpdate();
            }
            return oldUser.get();
        } catch (SQLException e) {
            logger.error("Failed to update user: {}", user.getId(), e);
            throw new DaoException("Failed to update user", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
                statement.setInt(1, id);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.error("Failed to delete user by id: {}", id, e);
            throw new DaoException("Failed to delete user", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN)) {
                statement.setString(1, login);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = EntityMapper.mapUser(resultSet);
                        return Optional.of(user);
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find user by login: {}", login, e);
            throw new DaoException("Failed to find user by login", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
                statement.setString(1, email);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = EntityMapper.mapUser(resultSet);
                        return Optional.of(user);
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Failed to find user by email: {}", email, e);
            throw new DaoException("Failed to find user by email", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean existsByLogin(String login) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_LOGIN)) {
                statement.setString(1, login);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to check user login: {}", login, e);
            throw new DaoException("Failed to check user login", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_EMAIL)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to check user email: {}", email, e);
            throw new DaoException("Failed to check user email", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while obtaining connection", e);
            throw new DaoException("Failed to obtain database connection", e);
        } finally {
            connectionPool.releaseConnection(connection);
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
