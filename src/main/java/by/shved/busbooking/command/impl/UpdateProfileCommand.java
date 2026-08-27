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
import by.shved.busbooking.validator.RegistrationValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UpdateProfileCommand implements Command {
    private static final Logger logger = LogManager.getLogger(UpdateProfileCommand.class);
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        User currentUser = CommandUtil.getCurrentUser(request);
        if (currentUser == null) {
            CommandUtil.setFlashError(request, "Please login to update profile");
            return CommandResult.redirect(request.getContextPath() + "/controller?command=login");
        }

        String email = CommandUtil.param(request, "email");
        String lastName = CommandUtil.param(request, "lastName");
        String firstName = CommandUtil.param(request, "firstName");
        String patronymic = CommandUtil.param(request, "patronymic");
        String password = CommandUtil.param(request, "password");

        RegistrationValidator validator = new RegistrationValidator();
        if (!validator.validate(currentUser.getLogin(), password.isEmpty() ? "dummy" : password,
                email, lastName, firstName)) {
            List<String> errors = validator.getErrors();
            CommandUtil.setFlashError(request, errors.get(0));
            return CommandResult.redirect(request.getContextPath() + "/controller?command=profile");
        }

        try {
            User updatedUser = new User();
            updatedUser.setId(currentUser.getId());
            updatedUser.setLogin(currentUser.getLogin());
            updatedUser.setPasswordHash(password.isEmpty() ? currentUser.getPasswordHash() : password);
            updatedUser.setEmail(email);
            updatedUser.setLastName(lastName);
            updatedUser.setFirstName(firstName);
            updatedUser.setPatronymic(patronymic);
            updatedUser.setRole(currentUser.getRole());
            boolean success = userService.updateProfile(updatedUser);
            if (success) {
                currentUser.setEmail(email);
                currentUser.setLastName(lastName);
                currentUser.setFirstName(firstName);
                currentUser.setPatronymic(patronymic);
                if (!password.isEmpty()) {
                    //todo update user on client after password changing
                }
                request.getSession().setAttribute(SessionKeys.USER, currentUser);
                CommandUtil.setFlashMessage(request, "Profile updated successfully");
            } else {
                CommandUtil.setFlashError(request, "Failed to update profile");
            }
        } catch (ServiceException e) {
            logger.error("Update profile failed for user: {}", currentUser.getId(), e);
            CommandUtil.setFlashError(request, "Failed to update profile: " + e.getMessage());
        }
        return CommandResult.redirect(request.getContextPath() + "/controller?command=profile");
    }
}