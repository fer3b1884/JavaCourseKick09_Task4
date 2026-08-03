package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.service.UserService;
import by.shved.busbooking.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("pass");
        UserService userService = UserServiceImpl.getInstance();
        String page;
        if (userService.authentication(login, password)) {
            request.setAttribute("user", login);
            page = "pages/main.jsp";
        } else {
            request.setAttribute("login_msg", "incorrect login or pass");
            page = "index.jsp";
        }
        return page;
    }
}
