package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusService;
import by.shved.busbooking.service.impl.BusServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminReportsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AdminReportsCommand.class);
    private final BusService busService = BusServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        CommandUtil.transferFlashMessages(request);
        String routeNumber = CommandUtil.param(request, "routeNumber");
        try {
            if (!routeNumber.isEmpty()) {
                request.setAttribute("busesByRoute", busService.findByRouteNumber(routeNumber));
                request.setAttribute("routeNumber", routeNumber);
            }
            request.setAttribute("oldBuses", busService.findOlderThanTenYears());
            request.setAttribute("highMileageBuses", busService.findWithHighMileage());
            return CommandResult.forward("/WEB-INF/jsp/admin/reports.jsp");
        } catch (ServiceException e) {
            logger.error("Failed to load reports", e);
            CommandUtil.setFlashError(request, "Failed to load reports: " + e.getMessage());
            return CommandResult.forward("/WEB-INF/jsp/admin/reports.jsp");
        }
    }
}