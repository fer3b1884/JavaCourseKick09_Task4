package by.shved.busbooking.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RouteValidator {
    private static final Pattern ROUTE_NUMBER_PATTERN = Pattern.compile("^[\\w\\-]{1,20}$");
    private final List<String> errors = new ArrayList<>();

    public boolean validate(String routeNumber, String departureCity, String arrivalCity) {
        errors.clear();
        if (ValidationUtil.isBlank(routeNumber) || !ROUTE_NUMBER_PATTERN.matcher(routeNumber).matches()) {
            errors.add("Invalid route number");
        }
        if (ValidationUtil.isBlank(departureCity)) {
            errors.add("Departure city is required");
        }
        if (ValidationUtil.isBlank(arrivalCity)) {
            errors.add("Arrival city is required");
        }
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
