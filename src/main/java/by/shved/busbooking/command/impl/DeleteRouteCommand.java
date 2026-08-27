package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusRouteService;
import by.shved.busbooking.service.impl.BusRouteServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteRouteCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteRouteCommand.class);
    private final BusRouteService routeService = BusRouteServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String idParam = CommandUtil.param(request, "id");
        if (idParam.isEmpty()) {
            CommandUtil.setFlashError(request, "Route ID is required");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_routes");
        }
        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = routeService.delete(id);
            if (deleted) {
                CommandUtil.setFlashMessage(request, "Route deleted successfully");
            } else {
                CommandUtil.setFlashError(request, "Route not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid route id: {}", idParam);
            CommandUtil.setFlashError(request, "Invalid route ID");
        } catch (ServiceException e) {
            logger.error("Failed to delete route id: {}", idParam, e);
            CommandUtil.setFlashError(request, "Failed to delete route: " + e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_routes");
    }
}