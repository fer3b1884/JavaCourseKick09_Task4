package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BookingService;
import by.shved.busbooking.service.impl.BookingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookingsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(BookingsCommand.class);
    private final BookingService bookingService = BookingServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        CommandUtil.transferFlashMessages(request);
        User user = CommandUtil.getCurrentUser(request);
        if (user == null) {
            CommandUtil.setFlashError(request, "Please login to view your bookings");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=login");
        }
        try {
            request.setAttribute("bookings", bookingService.findUserBookings(user.getId()));
            return CommandResult.forward("/WEB-INF/jsp/bookings.jsp");
        } catch (ServiceException e) {
            logger.error("Failed to load bookings for user: {}", user.getId(), e);
            CommandUtil.setFlashError(request, "Failed to load bookings");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }
    }
}