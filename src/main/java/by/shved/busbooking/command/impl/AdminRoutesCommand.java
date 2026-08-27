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

public class AdminRoutesCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AdminRoutesCommand.class);
    private final BusRouteService routeService = BusRouteServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        CommandUtil.transferFlashMessages(request);
        try {
            request.setAttribute("routes", routeService.findAll());
            return CommandResult.forward("/WEB-INF/jsp/admin/routes.jsp");
        } catch (ServiceException e) {
            logger.error("Failed to load admin routes page", e);
            CommandUtil.setFlashError(request, "Failed to load routes");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }
    }
}