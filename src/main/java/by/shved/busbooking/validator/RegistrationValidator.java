package by.shved.busbooking.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegistrationValidator {
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,30}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private final List<String> errors = new ArrayList<>();

    public boolean validate(String login, String password, String email, String lastName, String firstName) {
        errors.clear();
        if (ValidationUtil.isBlank(login) || !LOGIN_PATTERN.matcher(login).matches()) {
            errors.add("Login: 3-30 characters, letters, digits and underscore");
        }
        if (ValidationUtil.isBlank(password) || password.length() < 4) {
            errors.add("Password: at least 4 characters");
        }
        if (ValidationUtil.isBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Invalid email format");
        }
        if (ValidationUtil.isBlank(lastName)) {
            errors.add("Last name is required");
        }
        if (ValidationUtil.isBlank(firstName)) {
            errors.add("First name is required");
        }
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
