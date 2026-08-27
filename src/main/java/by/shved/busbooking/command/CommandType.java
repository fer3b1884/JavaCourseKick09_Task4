package by.shved.busbooking.command;

import by.shved.busbooking.command.impl.*;

public enum CommandType {
    DEFAULT(new DefaultCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    MAIN(new MainCommand()),
    REGISTER(new RegisterPageCommand()),
    ADD_USER(new AddUserCommand()),
    TRIPS(new TripsCommand()),
    BOOKINGS(new BookingsCommand()),
    BOOK_TRIP(new BookTripCommand()),
    CANCEL_BOOKING(new CancelBookingCommand()),
    PROFILE(new ProfileCommand()),
    UPDATE_PROFILE(new UpdateProfileCommand()),
    ADMIN_BUSES(new AdminBusesCommand()),
    SAVE_BUS(new SaveBusCommand()),
    DELETE_BUS(new DeleteBusCommand()),
    ADMIN_ROUTES(new AdminRoutesCommand()),
    SAVE_ROUTE(new SaveRouteCommand()),
    DELETE_ROUTE(new DeleteRouteCommand()),
    ADMIN_TRIPS(new AdminTripsCommand()),
    SAVE_TRIP(new SaveTripCommand()),
    DELETE_TRIP(new DeleteTripCommand()),
    ADMIN_REPORTS(new AdminReportsCommand());

    private final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command define(String commandStr) {
        if (commandStr == null) {
            return DEFAULT.command;
        }
        try {
            return CommandType.valueOf(commandStr.strip().toUpperCase()).command;
        } catch (IllegalArgumentException e) {
            return DEFAULT.command;
        }
    }
}
