package by.shved.busbooking.service.impl;

import by.shved.busbooking.dao.BusRouteDao;
import by.shved.busbooking.dao.impl.BusRouteDaoImpl;
import by.shved.busbooking.entity.BusRoute;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusRouteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BusRouteServiceImpl implements BusRouteService {
    private static final Logger logger = LogManager.getLogger(BusRouteServiceImpl.class);
    private static final BusRouteServiceImpl INSTANCE = new BusRouteServiceImpl();
    private final BusRouteDao routeDao = BusRouteDaoImpl.getInstance();

    private BusRouteServiceImpl() {}

    public static BusRouteServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<BusRoute> findAll() throws ServiceException {
        try {
            return routeDao.findAll();
        } catch (DaoException e) {
            logger.error("Failed to find all routes", e);
            throw new ServiceException("Failed to get route list", e);
        }
    }

    @Override
    public Optional<BusRoute> findById(Integer id) throws ServiceException {
        try {
            return routeDao.findEntityById(id);
        } catch (DaoException e) {
            logger.error("Failed to find route by id: {}", id, e);
            throw new ServiceException("Failed to get route", e);
        }
    }

    @Override
    public BusRoute create(BusRoute route) throws ServiceException {
        try {
            boolean created = routeDao.create(route);
            if (!created) {
                throw new ServiceException("Failed to create route");
            }
            return route;
        } catch (DaoException e) {
            logger.error("Failed to create route: {}", route, e);
            throw new ServiceException("Failed to create route", e);
        }
    }

    @Override
    public boolean update(BusRoute route) throws ServiceException {
        try {
            Optional<BusRoute> existing = routeDao.findEntityById(route.getId());
            if (existing.isEmpty()) {
                throw new ServiceException("Route not found for update");
            }
            return routeDao.update(route);
        } catch (DaoException e) {
            logger.error("Failed to update route: {}", route, e);
            throw new ServiceException("Failed to update route", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            return routeDao.delete(id);
        } catch (DaoException e) {
            logger.error("Failed to delete route id: {}", id, e);
            throw new ServiceException("Failed to delete route", e);
        }
    }
}
