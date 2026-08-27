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

public class DeleteBusCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteBusCommand.class);
    private final BusService busService = BusServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String idParam = CommandUtil.param(request, "id");
        if (idParam.isEmpty()) {
            CommandUtil.setFlashError(request, "Bus ID is required");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_buses");
        }
        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = busService.delete(id);
            if (deleted) {
                CommandUtil.setFlashMessage(request, "Bus deleted successfully");
            } else {
                CommandUtil.setFlashError(request, "Bus not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid bus id: {}", idParam);
            CommandUtil.setFlashError(request, "Invalid bus ID");
        } catch (ServiceException e) {
            logger.error("Failed to delete bus id: {}", idParam, e);
            CommandUtil.setFlashError(request, "Failed to delete bus: " + e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_buses");
    }
}