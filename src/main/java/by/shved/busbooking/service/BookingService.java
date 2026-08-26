package by.shved.busbooking.service;

import by.shved.busbooking.entity.Booking;
import by.shved.busbooking.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<Booking> findUserBookings(Integer userId) throws ServiceException;
    Optional<Booking> findById(Integer bookingId) throws ServiceException;
    Booking createBooking(Integer userId, Integer tripId, Integer seatNumber) throws ServiceException;
    boolean cancelBooking(Integer bookingId, Integer userId) throws ServiceException;
}
