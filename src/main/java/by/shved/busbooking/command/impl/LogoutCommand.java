package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "";
    }
}
