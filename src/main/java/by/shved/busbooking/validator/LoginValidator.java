package by.shved.busbooking.validator;

import java.util.ArrayList;
import java.util.List;

public class LoginValidator {
    private final List<String> errors = new ArrayList<>();

    public boolean validate(String login, String password) {
        errors.clear();
        if (ValidationUtil.isBlank(login)) {
            errors.add("Login is required");
        }
        if (ValidationUtil.isBlank(password)) {
            errors.add("Password is required");
        }
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
