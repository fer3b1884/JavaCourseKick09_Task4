package by.shved.busbooking.command.impl;

import by.shved.busbooking.command.Command;
import by.shved.busbooking.command.CommandResult;
import by.shved.busbooking.command.CommandUtil;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRoleType;
import by.shved.busbooking.exception.CommandException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.UserService;
import by.shved.busbooking.service.impl.UserServiceImpl;
import by.shved.busbooking.validator.RegistrationValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class AddUserCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String login = CommandUtil.param(request, "login");
        String password = CommandUtil.param(request, "password");
        String email = CommandUtil.param(request, "email");
        String lastName = CommandUtil.param(request, "lastName");
        String firstName = CommandUtil.param(request, "firstName");
        String patronymic = CommandUtil.param(request, "patronymic");
        RegistrationValidator validator = new RegistrationValidator();
        if (!validator.validate(login, password, email, lastName, firstName)) {
            request.setAttribute("errors", validator.getErrors());
            preserveForm(request, login, email, lastName, firstName, patronymic);
            return CommandResult.forward("/WEB-INF/jsp/register.jsp");
        }
        User user = new User();
        user.setLogin(login);
        user.setPasswordHash(password);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPatronymic(patronymic);
        user.setRole(UserRoleType.USER);
        try {
            userService.register(user);
            CommandUtil.setFlashMessage(request, "Registration successful. Please log in.");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=login");
        } catch (ServiceException e) {
            request.setAttribute("errors", List.of(e.getMessage()));
            preserveForm(request, login, email, lastName, firstName, patronymic);
            return CommandResult.forward("/WEB-INF/jsp/register.jsp");
        }
    }

    private void preserveForm(HttpServletRequest request, String login, String email,
                              String lastName, String firstName, String patronymic) {
        request.setAttribute("login", login);
        request.setAttribute("email", email);
        request.setAttribute("lastName", lastName);
        request.setAttribute("firstName", firstName);
        request.setAttribute("patronymic", patronymic);
    }
}
