package by.shved.busbooking.service;

import by.shved.busbooking.entity.User;
import by.shved.busbooking.exception.ServiceException;

import java.util.Optional;

public interface UserService {
    Optional<User> authenticate(String login, String password) throws ServiceException;
    User register(User user) throws ServiceException;
    boolean updateProfile(User user) throws ServiceException;
}
