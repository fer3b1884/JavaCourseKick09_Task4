package by.shved.busbooking.dao;

import by.shved.busbooking.entity.User;
import by.shved.busbooking.exception.DaoException;

import java.util.Optional;

public interface UserDao extends BaseDao<Integer, User> {
    Optional<User> findUserByLogin(String login) throws DaoException;
    Optional<User> findUserByEmail(String email) throws DaoException;
    boolean existsByLogin(String login) throws DaoException;
    boolean existsByEmail(String email) throws DaoException;
}
