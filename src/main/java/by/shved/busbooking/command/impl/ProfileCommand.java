package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public class ProfileCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        CommandUtil.transferFlashMessages(request);
        User user = CommandUtil.getCurrentUser(request);
        if (user == null) {
            CommandUtil.setFlashError(request, "Please login to view profile");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=login");
        }
        request.setAttribute("profile", user);
        return CommandResult.forward("/WEB-INF/jsp/profile.jsp");
    }
}