package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.Bus;
import by.shved.busbooking.entity.BusDriver;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRoleType;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.BusService;
import by.shved.busbooking.service.impl.BusServiceImpl;
import by.shved.busbooking.validator.BusValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveBusCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SaveBusCommand.class);
    private final BusService busService = BusServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        User user = CommandUtil.getCurrentUser(request);
        if (user == null || user.getRole() != UserRoleType.ADMIN) {
            CommandUtil.setFlashError(request, "Access denied");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
        }
        String idStr = CommandUtil.param(request, "id");
        String busNumber = CommandUtil.param(request, "busNumber");
        String brand = CommandUtil.param(request, "brand");
        String driverId = CommandUtil.param(request, "driverId");
        String startYear = CommandUtil.param(request, "startOperationYear");
        String mileage = CommandUtil.param(request, "mileage");
        String seatCount = CommandUtil.param(request, "seatCount");
        String status = CommandUtil.param(request, "status");
        if (status.isEmpty()) status = "ACTIVE";
        BusValidator validator = new BusValidator();
        if (!validator.validate(busNumber, brand, driverId, startYear, mileage, seatCount)) {
            CommandUtil.setFlashError(request, validator.getErrors().get(0));
            return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_buses");
        }
        try {
            Bus bus = new Bus();
            if (!idStr.isEmpty()) {
                bus.setId(Integer.parseInt(idStr));
            }
            bus.setBusNumber(busNumber);
            bus.setBrand(brand);
            bus.setDriver(new BusDriver(Integer.parseInt(driverId), null, null, null, null, null, null));
            bus.setStartOperationYear(Integer.parseInt(startYear));
            bus.setMileage(Integer.parseInt(mileage));
            bus.setSeatCount(Integer.parseInt(seatCount));
            bus.setStatus(status);
            if (bus.getId() == null) {
                Bus created = busService.create(bus);
                CommandUtil.setFlashMessage(request, "Bus added successfully");
            } else {
                boolean updated = busService.update(bus);
                if (updated) {
                    CommandUtil.setFlashMessage(request, "Bus updated successfully");
                } else {
                    CommandUtil.setFlashError(request, "Bus not found");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid number format in SaveBusCommand", e);
            CommandUtil.setFlashError(request, "Invalid input data");
        } catch (ServiceException e) {
            logger.error("Failed to save bus", e);
            CommandUtil.setFlashError(request, "Failed to save bus: " + e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=admin_buses");
    }
}