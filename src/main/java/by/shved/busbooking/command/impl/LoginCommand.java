package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.UserService;
import by.shved.busbooking.service.impl.UserServiceImpl;
import by.shved.busbooking.util.SessionKeys;
import by.shved.busbooking.validator.LoginValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String login = CommandUtil.param(request, "login");
        String password = CommandUtil.param(request, "password");
        LoginValidator validator = new LoginValidator();
        if (!validator.validate(login, password)) {
            request.setAttribute("errors", validator.getErrors());
            request.setAttribute("login", login);
            return CommandResult.forward("/index.jsp");
        }
        try {
            Optional<User> userOpt = userService.authenticate(login, password);
            if (userOpt.isPresent()) {
                request.getSession().setAttribute(SessionKeys.USER, userOpt.get());
                CommandUtil.setFlashMessage(request, "Welcome, " + userOpt.get().getFirstName() + "!");
                return CommandResult.redirect(request.getContextPath() + "/controller?command=main");
            } else {
                request.setAttribute("login_msg", "Invalid login or password");
                request.setAttribute("login", login);
                return CommandResult.forward("/index.jsp");
            }
        } catch (ServiceException e) {
            logger.error("Login error for login: {}", login, e);
            request.setAttribute("login_msg", "System error, please try again later");
            request.setAttribute("login", login);
            return CommandResult.forward("/index.jsp");
        }
    }
}