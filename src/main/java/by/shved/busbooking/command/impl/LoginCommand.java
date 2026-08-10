package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.UserService;
import by.shved.busbooking.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter("login");
        String password = request.getParameter("pass");
        UserService userService = UserServiceImpl.getInstance();
        String page;
        HttpSession session = request.getSession();
        try {
            if (userService.authentication(login, password)) {
                request.setAttribute("user", login);
                session.setAttribute("user_name", login);
                page = "pages/main.jsp";
            } else {
                request.setAttribute("login_msg", "incorrect login or pass");
                page = "index.jsp";
            }
            session.setAttribute("current_page", page);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
