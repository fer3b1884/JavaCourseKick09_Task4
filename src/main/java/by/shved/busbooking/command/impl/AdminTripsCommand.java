package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusRouteService;
import by.shved.busbooking.service.BusService;
import by.shved.busbooking.service.TripService;
import by.shved.busbooking.service.impl.BusRouteServiceImpl;
import by.shved.busbooking.service.impl.BusServiceImpl;
import by.shved.busbooking.service.impl.TripServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminTripsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AdminTripsCommand.class);
    private final TripService tripService = TripServiceImpl.getInstance();
    private final BusRouteService routeService = BusRouteServiceImpl.getInstance();
    private final BusService busService = BusServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        CommandUtil.transferFlashMessages(request);
        try {
            request.setAttribute("trips", tripService.findAll());
            request.setAttribute("routes", routeService.findAll());
            request.setAttribute("buses", busService.findAll());
            return CommandResult.forward("/WEB-INF/jsp/admin/trips.jsp");
        } catch (ServiceException e) {
            logger.error("Failed to load admin trips page", e);
            CommandUtil.setFlashError(request, "Failed to load trips");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }
    }
}