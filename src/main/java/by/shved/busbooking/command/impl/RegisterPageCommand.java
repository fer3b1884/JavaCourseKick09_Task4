package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import jakarta.servlet.http.HttpServletRequest;

public class RegisterPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandUtil.transferFlashMessages(request);
        return CommandResult.forward("/WEB-INF/jsp/register.jsp");
    }
}