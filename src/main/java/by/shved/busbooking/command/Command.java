package by.shved.busbooking.command;

import by.shved.busbooking.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface Command {
    CommandResult execute(HttpServletRequest request) throws CommandException;
}
