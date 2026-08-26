package by.shved.busbooking.service.impl;

import by.shved.busbooking.dao.BusDao;
import by.shved.busbooking.dao.impl.BusDaoImpl;
import by.shved.busbooking.entity.Bus;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BusServiceImpl implements BusService {
    private static final Logger logger = LogManager.getLogger(BusServiceImpl.class);
    private static final BusServiceImpl INSTANCE = new BusServiceImpl();
    private final BusDao busDao = BusDaoImpl.getInstance();

    private BusServiceImpl() {}

    public static BusServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Bus> findAll() throws ServiceException {
        try {
            return busDao.findAll();
        } catch (DaoException e) {
            logger.error("Failed to find all buses", e);
            throw new ServiceException("Failed to get bus list", e);
        }
    }

    @Override
    public Optional<Bus> findById(Integer id) throws ServiceException {
        try {
            return busDao.findEntityById(id);
        } catch (DaoException e) {
            logger.error("Failed to find bus by id: {}", id, e);
            throw new ServiceException("Failed to get bus", e);
        }
    }

    @Override
    public Bus create(Bus bus) throws ServiceException {
        try {
            boolean created = busDao.create(bus);
            if (!created) {
                throw new ServiceException("Failed to create bus");
            }
            return bus;
        } catch (DaoException e) {
            logger.error("Failed to create bus: {}", bus, e);
            throw new ServiceException("Failed to create bus", e);
        }
    }

    @Override
    public boolean update(Bus bus) throws ServiceException {
        try {
            Optional<Bus> existing = busDao.findEntityById(bus.getId());
            if (existing.isEmpty()) {
                throw new ServiceException("Bus not found for update");
            }
            return busDao.update(bus);
        } catch (DaoException e) {
            logger.error("Failed to update bus: {}", bus, e);
            throw new ServiceException("Failed to update bus", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            return busDao.delete(id);
        } catch (DaoException e) {
            logger.error("Failed to delete bus id: {}", id, e);
            throw new ServiceException("Failed to delete bus", e);
        }
    }

    @Override
    public List<Bus> findByRouteNumber(String routeNumber) throws ServiceException {
        try {
            return busDao.findByRouteNumber(routeNumber);
        } catch (DaoException e) {
            logger.error("Failed to find buses by route number: {}", routeNumber, e);
            throw new ServiceException("Failed to get buses by route", e);
        }
    }

    @Override
    public List<Bus> findOlderThanTenYears() throws ServiceException {
        try {
            return busDao.findOlderThanYears(10);
        } catch (DaoException e) {
            logger.error("Failed to find buses older than 10 years", e);
            throw new ServiceException("Failed to get old buses", e);
        }
    }

    @Override
    public List<Bus> findWithHighMileage() throws ServiceException {
        try {
            return busDao.findByMileageGreaterThan(100_000);
        } catch (DaoException e) {
            logger.error("Failed to find buses with high mileage", e);
            throw new ServiceException("Failed to get high-mileage buses", e);
        }
    }
}
