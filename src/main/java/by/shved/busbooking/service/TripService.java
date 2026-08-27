package by.shved.busbooking.service;

import by.shved.busbooking.entity.Trip;
import by.shved.busbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface TripService {
    List<Trip> findAll() throws ServiceException;
    List<Trip> findAvailable() throws ServiceException;
    Optional<Trip> findById(Integer id) throws ServiceException;
    Trip create(Trip trip) throws ServiceException;
    boolean update(Trip trip) throws ServiceException;
    boolean delete(Integer id) throws ServiceException;
}
