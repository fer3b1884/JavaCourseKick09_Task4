package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.Bus;
import by.shved.busbooking.entity.BusRoute;
import by.shved.busbooking.entity.Trip;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRoleType;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.TripService;
import by.shved.busbooking.service.impl.TripServiceImpl;
import by.shved.busbooking.validator.TripValidator; // нужно создать
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SaveTripCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SaveTripCommand.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final TripService tripService = TripServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtil.getCurrentUser(request);
        if (user == null || user.getRole() != UserRoleType.ADMIN) {
            CommandUtil.setFlashError(request, "Access denied");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }

        String idStr = CommandUtil.param(request, "id");
        String routeId = CommandUtil.param(request, "routeId");
        String busId = CommandUtil.param(request, "busId");
        String departureTime = CommandUtil.param(request, "departureTime");
        String arrivalTime = CommandUtil.param(request, "arrivalTime");
        String price = CommandUtil.param(request, "price");
        String availableSeats = CommandUtil.param(request, "availableSeats");

        TripValidator validator = new TripValidator();
        if (!validator.validate(routeId, busId, departureTime, arrivalTime, price, availableSeats)) {
            CommandUtil.setFlashError(request, validator.getErrors().get(0));
            return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_trips");
        }

        try {
            Trip trip = new Trip();
            if (!idStr.isEmpty()) {
                trip.setId(Integer.parseInt(idStr));
            }
            trip.setRoute(new BusRoute(Integer.parseInt(routeId), null, null, null));
            trip.setBus(new Bus(Integer.parseInt(busId), null, null, null, null, null, null, null));
            trip.setDepartureTime(LocalDateTime.parse(departureTime, FORMATTER));
            trip.setArrivalTime(LocalDateTime.parse(arrivalTime, FORMATTER));
            trip.setPrice(new BigDecimal(price));
            trip.setAvailableSeats(Integer.parseInt(availableSeats));

            if (trip.getId() == null) {
                Trip created = tripService.create(trip);
                CommandUtil.setFlashMessage(request, "Trip added successfully");
            } else {
                boolean updated = tripService.update(trip);
                if (updated) {
                    CommandUtil.setFlashMessage(request, "Trip updated successfully");
                } else {
                    CommandUtil.setFlashError(request, "Trip not found");
                }
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error("Invalid input format in SaveTripCommand", e);
            CommandUtil.setFlashError(request, "Invalid input data");
        } catch (ServiceException e) {
            logger.error("Failed to save trip", e);
            CommandUtil.setFlashError(request, "Failed to save trip: " + e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_trips");
    }
}