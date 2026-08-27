package by.shved.busbooking.dao;

import by.shved.busbooking.entity.Trip;
import by.shved.busbooking.exception.DaoException;

import java.time.LocalDate;
import java.util.List;

public interface TripDao extends BaseDao<Integer, Trip> {
    List<Trip> findByRoute(Integer routeId) throws DaoException;
    List<Trip> findByDepartureDate(LocalDate date) throws DaoException;
    List<Trip> findAvailableTrips() throws DaoException;
    boolean updateAvailableSeats(Integer tripId, Integer availableSeats) throws DaoException;
}
