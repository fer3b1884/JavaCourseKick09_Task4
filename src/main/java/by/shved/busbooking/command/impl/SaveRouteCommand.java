package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.BusRoute;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRoleType;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusRouteService;
import by.shved.busbooking.service.impl.BusRouteServiceImpl;
import by.shved.busbooking.validator.RouteValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveRouteCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SaveRouteCommand.class);
    private final BusRouteService routeService = BusRouteServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtil.getCurrentUser(request);
        if (user == null || user.getRole() != UserRoleType.ADMIN) {
            CommandUtil.setFlashError(request, "Access denied");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }

        String idStr = CommandUtil.param(request, "id");
        String routeNumber = CommandUtil.param(request, "routeNumber");
        String departureCity = CommandUtil.param(request, "departureCity");
        String arrivalCity = CommandUtil.param(request, "arrivalCity");

        RouteValidator validator = new RouteValidator();
        if (!validator.validate(routeNumber, departureCity, arrivalCity)) {
            CommandUtil.setFlashError(request, validator.getErrors().get(0));
            return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_routes");
        }

        try {
            BusRoute route = new BusRoute();
            if (!idStr.isEmpty()) {
                route.setId(Integer.parseInt(idStr));
            }
            route.setRouteNumber(routeNumber);
            route.setDepartureCity(departureCity);
            route.setArrivalCity(arrivalCity);

            if (route.getId() == null) {
                BusRoute created = routeService.create(route);
                CommandUtil.setFlashMessage(request, "Route added successfully");
            } else {
                boolean updated = routeService.update(route);
                if (updated) {
                    CommandUtil.setFlashMessage(request, "Route updated successfully");
                } else {
                    CommandUtil.setFlashError(request, "Route not found");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid number format in SaveRouteCommand", e);
            CommandUtil.setFlashError(request, "Invalid input data");
        } catch (ServiceException e) {
            logger.error("Failed to save route", e);
            CommandUtil.setFlashError(request, "Failed to save route: " + e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_routes");
    }
}