package by.shved.busbooking.service;

import by.shved.busbooking.entity.BusRoute;
import by.shved.busbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BusRouteService {
    List<BusRoute> findAll() throws ServiceException;
    Optional<BusRoute> findById(Integer id) throws ServiceException;
    BusRoute create(BusRoute route) throws ServiceException;
    boolean update(BusRoute route) throws ServiceException;
    boolean delete(Integer id) throws ServiceException;
}
