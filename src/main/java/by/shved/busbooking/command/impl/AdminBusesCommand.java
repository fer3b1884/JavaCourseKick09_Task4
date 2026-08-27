package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.dao.BusDriverDao;
import by.shved.busbooking.dao.impl.BusDriverDaoImpl;
import by.shved.busbooking.entity.BusDriver;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusService;
import by.shved.busbooking.service.impl.BusServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AdminBusesCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AdminBusesCommand.class);
    private final BusService busService = BusServiceImpl.getInstance();
    private final BusDriverDao driverDao = BusDriverDaoImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        CommandUtil.transferFlashMessages(request);
        try {
            request.setAttribute("buses", busService.findAll());
            List<BusDriver> drivers = driverDao.findAll();
            request.setAttribute("drivers", drivers);
            return CommandResult.forward("/WEB-INF/jsp/admin/buses.jsp");
        } catch (ServiceException | DaoException e) {
            logger.error("Failed to load admin buses page", e);
            CommandUtil.setFlashError(request, "Failed to load bus management page");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }
    }
}