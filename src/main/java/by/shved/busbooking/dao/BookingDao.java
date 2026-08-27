package by.shved.busbooking.dao;

import by.shved.busbooking.entity.Booking;
import by.shved.busbooking.exception.DaoException;

import java.util.List;

public interface BookingDao extends BaseDao<Integer, Booking> {
    List<Booking> findByTrip(Integer tripId) throws DaoException;
    List<Booking> findByUserId(Integer userId) throws DaoException;
    boolean isSeatTaken(Integer tripId, Integer seatNumber) throws DaoException;
}
