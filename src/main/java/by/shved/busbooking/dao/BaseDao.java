package by.shved.busbooking.dao;

import by.shved.busbooking.entity.AbstractEntity;
import by.shved.busbooking.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseDao<K, T extends AbstractEntity> {
    boolean create(T entity) throws DaoException;
    List<T> findAll() throws DaoException;
    Optional<T> findEntityById(K id) throws DaoException;
    boolean update(T entity) throws DaoException;
    boolean delete(K id) throws DaoException;
//    boolean delete(T entity) throws DaoException;
}
