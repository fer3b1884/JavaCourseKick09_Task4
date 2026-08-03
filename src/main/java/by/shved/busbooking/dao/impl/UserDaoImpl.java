package by.shved.busbooking.dao.impl;

import by.shved.busbooking.dao.BaseDao;
import by.shved.busbooking.dao.UserDao;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.pool.ConnectionPool;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements BaseDao<User>, UserDao {
    private static final String SELECT_PASSWORD_HASH_FROM_USERS_WHERE_LOGIN = "Select password_hash FROM users WHERE login = ?";
    private static UserDaoImpl instance = new UserDaoImpl();

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        return instance;
    }

    @Override
    public boolean insert(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public boolean deleteById(User user) {
        return false;
    }

    @Override
    public Optional<User> findAll() {
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean authenticate(String login, String password) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PASSWORD_HASH_FROM_USERS_WHERE_LOGIN)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            String passFromDb;
            if (resultSet.next()) {
                passFromDb = resultSet.getString(1);
                return password.equals(passFromDb);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
