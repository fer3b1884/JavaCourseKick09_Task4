package by.shved.busbooking.service.impl;

import by.shved.busbooking.dao.BookingDao;
import by.shved.busbooking.dao.TripDao;
import by.shved.busbooking.dao.UserDao;
import by.shved.busbooking.dao.impl.BookingDaoImpl;
import by.shved.busbooking.dao.impl.TripDaoImpl;
import by.shved.busbooking.dao.impl.UserDaoImpl;
import by.shved.busbooking.entity.Booking;
import by.shved.busbooking.entity.Trip;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BookingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {
    private static final Logger logger = LogManager.getLogger(BookingServiceImpl.class);
    private static final BookingServiceImpl INSTANCE = new BookingServiceImpl();
    private final BookingDao bookingDao = BookingDaoImpl.getInstance();
    private final TripDao tripDao = TripDaoImpl.getInstance();
    private final UserDao userDao = UserDaoImpl.getInstance();

    private BookingServiceImpl() {}

    public static BookingServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Booking> findUserBookings(Integer userId) throws ServiceException {
        try {
            return bookingDao.findByUserId(userId);
        } catch (DaoException e) {
            logger.error("Failed to find bookings for user: {}", userId, e);
            throw new ServiceException("Failed to get user bookings", e);
        }
    }

    @Override
    public Optional<Booking> findById(Integer bookingId) throws ServiceException {
        try {
            return bookingDao.findEntityById(bookingId);
        } catch (DaoException e) {
            logger.error("Failed to find booking by id: {}", bookingId, e);
            throw new ServiceException("Failed to get booking", e);
        }
    }

    @Override
    public Booking createBooking(Integer userId, Integer tripId, Integer seatNumber) throws ServiceException {
        try {
            User user = userDao.findEntityById(userId)
                    .orElseThrow(() -> new ServiceException("User not found"));
            Trip trip = tripDao.findEntityById(tripId)
                    .orElseThrow(() -> new ServiceException("Trip not found"));
            if (bookingDao.isSeatTaken(tripId, seatNumber)) {
                throw new ServiceException("Seat " + seatNumber + " is already taken");
            }
            if (trip.getAvailableSeats() <= 0) {
                throw new ServiceException("No available seats on this trip");
            }
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setTrip(trip);
            booking.setSeatNumber(seatNumber);
            booking.setStatus("ACTIVE");
            booking.setBookingDate(LocalDateTime.now());
            boolean created = bookingDao.create(booking);
            if (!created) {
                throw new ServiceException("Failed to create booking");
            }
            int newAvailable = trip.getAvailableSeats() - 1;
            boolean updated = tripDao.updateAvailableSeats(tripId, newAvailable);
            if (!updated) {
                logger.error("Failed to update available seats after booking creation, tripId: {}", tripId);
                throw new ServiceException("Failed to update seats");
            }
            return booking;
        } catch (DaoException e) {
            logger.error("Failed to create booking for user {} trip {} seat {}", userId, tripId, seatNumber, e);
            throw new ServiceException("Database error while booking", e);
        }
    }

    @Override
    public boolean cancelBooking(Integer bookingId, Integer userId) throws ServiceException {
        try {
            Booking booking = bookingDao.findEntityById(bookingId)
                    .orElseThrow(() -> new ServiceException("Booking not found"));
            if (!booking.getUser().getId().equals(userId)) {
                throw new ServiceException("You are not authorized to cancel this booking");
            }
            if ("CANCELLED".equals(booking.getStatus())) {
                throw new ServiceException("Booking is already cancelled");
            }
            booking.setStatus("CANCELLED");
            boolean updated = bookingDao.update(booking);
            if (!updated) {
                throw new ServiceException("Failed to cancel booking");
            }
            Trip trip = booking.getTrip();
            int newAvailable = trip.getAvailableSeats() + 1;
            boolean seatsUpdated = tripDao.updateAvailableSeats(trip.getId(), newAvailable);
            if (!seatsUpdated) {
                logger.error("Failed to increase available seats after cancellation, tripId: {}", trip.getId());
                throw new ServiceException("Failed to restore seat availability");
            }
            return true;
        } catch (DaoException e) {
            logger.error("Failed to cancel booking id: {} for user {}", bookingId, userId, e);
            throw new ServiceException("Database error while cancelling booking", e);
        }
    }
}
