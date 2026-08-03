package by.shved.busbooking.service.impl;

import by.shved.busbooking.dao.impl.UserDaoImpl;
import by.shved.busbooking.service.UserService;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance = new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authentication(String login, String password) {
        // todo validate login, pass, encoding
        UserDaoImpl userDao = UserDaoImpl.getInstance();
        return userDao.authenticate(login, password);
    }
}
