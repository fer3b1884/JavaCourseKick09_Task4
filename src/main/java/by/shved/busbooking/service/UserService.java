package by.shved.busbooking.service;

import by.shved.busbooking.exception.ServiceException;

public interface UserService {
    boolean authentication(String login, String password) throws ServiceException;
}
