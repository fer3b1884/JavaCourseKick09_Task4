package by.shved.busbooking.dao;

import by.shved.busbooking.entity.AbstractEntity;

import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    boolean insert(T t);
    boolean delete(T t);
    boolean deleteById(T t);
    Optional<T> findAll();
    T update(T t);  // returns old one
}
