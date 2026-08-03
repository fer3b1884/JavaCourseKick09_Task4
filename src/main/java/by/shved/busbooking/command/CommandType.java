package by.shved.busbooking.command;

import by.shved.busbooking.command.impl.AddUserCommand;
import by.shved.busbooking.command.impl.DefaultCommand;
import by.shved.busbooking.command.impl.LoginCommand;
import by.shved.busbooking.command.impl.LogoutCommand;

public enum CommandType {
    ADD_USER(new AddUserCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand());
    Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command define(String commandStr) {
        CommandType current = CommandType.valueOf(commandStr.toUpperCase());
        return current.command;
    }
}
