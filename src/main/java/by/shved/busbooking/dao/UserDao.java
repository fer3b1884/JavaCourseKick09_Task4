package by.shved.busbooking.dao;

public interface UserDao {
    boolean authenticate(String login, String password);
}
