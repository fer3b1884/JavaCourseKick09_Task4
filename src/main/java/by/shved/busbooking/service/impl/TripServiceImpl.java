package by.shved.busbooking.service.impl;

import by.shved.busbooking.dao.TripDao;
import by.shved.busbooking.dao.impl.TripDaoImpl;
import by.shved.busbooking.entity.Trip;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.TripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TripServiceImpl implements TripService {
    private static final Logger logger = LogManager.getLogger(TripServiceImpl.class);
    private static final TripServiceImpl INSTANCE = new TripServiceImpl();
    private final TripDao tripDao = TripDaoImpl.getInstance();

    private TripServiceImpl() {
    }

    public static TripServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Trip> findAll() throws ServiceException {
        try {
            return tripDao.findAll();
        } catch (DaoException e) {
            logger.error("Failed to find all trips", e);
            throw new ServiceException("Failed to get all trips", e);
        }
    }

    @Override
    public List<Trip> findAvailable() throws ServiceException {
        try {
            return tripDao.findAvailableTrips();
        } catch (DaoException e) {
            logger.error("Failed to find available trips", e);
            throw new ServiceException("Failed to get available trips", e);
        }
    }

    @Override
    public Optional<Trip> findById(Integer id) throws ServiceException {
        try {
            return tripDao.findEntityById(id);
        } catch (DaoException e) {
            logger.error("Failed to find trip by id: {}", id, e);
            throw new ServiceException("Failed to get trip", e);
        }
    }

    @Override
    public Trip create(Trip trip) throws ServiceException {
        try {
            boolean created = tripDao.create(trip);
            if (!created) {
                throw new ServiceException("Failed to create trip");
            }
            return trip;
        } catch (DaoException e) {
            logger.error("Failed to create trip: {}", trip, e);
            throw new ServiceException("Failed to create trip", e);
        }
    }

    @Override
    public boolean update(Trip trip) throws ServiceException {
        try {
            Optional<Trip> existing = tripDao.findEntityById(trip.getId());
            if (existing.isEmpty()) {
                throw new ServiceException("Trip not found for update");
            }
            return tripDao.update(trip);
        } catch (DaoException e) {
            logger.error("Failed to update trip: {}", trip, e);
            throw new ServiceException("Failed to update trip", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            return tripDao.delete(id);
        } catch (DaoException e) {
            logger.error("Failed to delete trip id: {}", id, e);
            throw new ServiceException("Failed to delete trip", e);
        }
    }
}
