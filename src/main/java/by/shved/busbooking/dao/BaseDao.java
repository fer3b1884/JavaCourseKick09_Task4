package by.shved.busbooking.dao;

import by.shved.busbooking.entity.AbstractEntity;
import by.shved.busbooking.exception.DaoException;

import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    boolean insert(T t) throws DaoException;
    boolean delete(T t) throws DaoException;
    boolean deleteById(T t) throws DaoException;
    Optional<T> findAll() throws DaoException;
    T update(T t) throws DaoException;  // returns old one
}
