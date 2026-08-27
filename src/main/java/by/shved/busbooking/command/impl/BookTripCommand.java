package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BookingService;
import by.shved.busbooking.service.impl.BookingServiceImpl;
import by.shved.busbooking.validator.BookingValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BookTripCommand implements Command {
    private static final Logger logger = LogManager.getLogger(BookTripCommand.class);
    private final BookingService bookingService = BookingServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtil.getCurrentUser(request);
        if (user == null) {
            CommandUtil.setFlashError(request, "Please login to book a trip");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=login");
        }
        String tripIdStr = CommandUtil.param(request, "tripId");
        String seatNumberStr = CommandUtil.param(request, "seatNumber");
        BookingValidator validator = new BookingValidator();
        if (!validator.validate(tripIdStr, seatNumberStr)) {
            CommandUtil.setFlashError(request, validator.getErrors().get(0));
            return CommandResult.redirect(request.getContextPath() + "/controller?command=trips");
        }
        try {
            int tripId = Integer.parseInt(tripIdStr);
            int seatNumber = Integer.parseInt(seatNumberStr);
            bookingService.createBooking(user.getId(), tripId, seatNumber);
            CommandUtil.setFlashMessage(request, "Booking successful!");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=bookings");
        } catch (NumberFormatException e) {
            logger.error("Invalid number format: tripId={}, seatNumber={}", tripIdStr, seatNumberStr);
            CommandUtil.setFlashError(request, "Invalid input data");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=trips");
        } catch (ServiceException e) {
            logger.error("Booking failed for user: {} trip: {} seat: {}", user.getId(), tripIdStr, seatNumberStr, e);
            CommandUtil.setFlashError(request, e.getMessage());
            return CommandResult.redirect(request.getContextPath() + "/controller?command=trips");
        }
    }
}