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

public class CancelBookingCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CancelBookingCommand.class);
    private final BookingService bookingService = BookingServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtil.getCurrentUser(request);
        if (user == null) {
            CommandUtil.setFlashError(request, "Please login to cancel booking");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=login");
        }
        String bookingIdStr = CommandUtil.param(request, "bookingId");
        if (bookingIdStr.isEmpty()) {
            CommandUtil.setFlashError(request, "Booking ID is required");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=bookings");
        }
        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            boolean cancelled = bookingService.cancelBooking(bookingId, user.getId());
            if (cancelled) {
                CommandUtil.setFlashMessage(request, "Booking cancelled successfully");
            } else {
                CommandUtil.setFlashError(request, "Failed to cancel booking");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid bookingId: {}", bookingIdStr);
            CommandUtil.setFlashError(request, "Invalid booking ID");
        } catch (ServiceException e) {
            logger.error("Cancel booking failed for user: {}, booking: {}", user.getId(), bookingIdStr, e);
            CommandUtil.setFlashError(request, e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=bookings");
    }
}