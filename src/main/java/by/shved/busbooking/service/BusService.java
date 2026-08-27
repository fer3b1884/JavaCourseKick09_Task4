package by.shved.busbooking.service;

import by.shved.busbooking.entity.Bus;
import by.shved.busbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BusService {
    List<Bus> findAll() throws ServiceException;
    Optional<Bus> findById(Integer id) throws ServiceException;
    Bus create(Bus bus) throws ServiceException;
    boolean update(Bus bus) throws ServiceException;
    boolean delete(Integer id) throws ServiceException;
    List<Bus> findByRouteNumber(String routeNumber) throws ServiceException;
    List<Bus> findOlderThanTenYears() throws ServiceException;
    List<Bus> findWithHighMileage() throws ServiceException;
}
