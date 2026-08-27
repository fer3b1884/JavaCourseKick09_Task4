package by.shved.busbooking.dao;

import by.shved.busbooking.entity.Bus;
import by.shved.busbooking.exception.DaoException;

import java.util.List;

public interface BusDao extends BaseDao<Integer, Bus> {
    List<Bus> findByRouteNumber(String routeNumber) throws DaoException;
    List<Bus> findOlderThanYears(int years) throws DaoException;
    List<Bus> findByMileageGreaterThan(int mileage) throws DaoException;
}
