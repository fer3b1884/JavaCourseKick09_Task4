package by.shved.busbooking.dao;

import by.shved.busbooking.exception.DaoException;

public interface UserDao {
    boolean authenticate(String login, String password) throws DaoException;
}
