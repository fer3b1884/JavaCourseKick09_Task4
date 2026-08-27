package by.shved.busbooking.service.impl;

import by.shved.busbooking.dao.UserDao;
import by.shved.busbooking.dao.impl.UserDaoImpl;
import by.shved.busbooking.encoder.PasswordEncoder;
import by.shved.busbooking.entity.User;
import by.shved.busbooking.entity.UserRoleType;
import by.shved.busbooking.exception.DaoException;
import by.shved.busbooking.exception.ServiceException;
import by.shved.busbooking.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    private final UserDao userDao = UserDaoImpl.getInstance();

    private UserServiceImpl() {}

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        try {
            Optional<User> userOpt = userDao.findUserByLogin(login);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }
            User user = userOpt.get();
            if (PasswordEncoder.checkPassword(password, user.getPasswordHash())) {
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (DaoException e) {
            logger.error("Authentication failed for login: {}", login, e);
            throw new ServiceException("Authentication error", e);
        }
    }

    @Override
    public User register(User user) throws ServiceException {
        try {
            if (userDao.existsByLogin(user.getLogin())) {
                throw new ServiceException("Login already taken");
            }
            if (userDao.existsByEmail(user.getEmail())) {
                throw new ServiceException("Email already registered");
            }
            String hashed = PasswordEncoder.hashPassword(user.getPasswordHash());
            user.setPasswordHash(hashed);
            user.setRole(UserRoleType.USER);

            boolean created = userDao.create(user);
            if (!created) {
                throw new ServiceException("Failed to create user");
            }
            return user;
        } catch (DaoException e) {
            logger.error("Registration failed for user: {}", user.getLogin(), e);
            throw new ServiceException("Registration failed", e);
        }
    }

    @Override
    public boolean updateProfile(User user) throws ServiceException {
        try {
            Optional<User> existing = userDao.findEntityById(user.getId());
            if (existing.isEmpty()) {
                throw new ServiceException("User not found");
            }
            return userDao.update(user);
        } catch (DaoException e) {
            logger.error("Update profile failed for user id: {}", user.getId(), e);
            throw new ServiceException("Update profile failed", e);
        }
    }
}
