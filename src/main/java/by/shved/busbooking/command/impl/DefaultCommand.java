package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        return CommandResult.forward("/index.jsp");
    }
}