package by.shved.busbooking.validator;

import by.shved.busbooking.validator.ValidationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TripValidator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final List<String> errors = new ArrayList<>();

    public boolean validate(String routeId, String busId, String departureTime,
                            String arrivalTime, String price, String availableSeats) {
        errors.clear();
        if (!ValidationUtil.isPositiveInteger(routeId)) {
            errors.add("Route is required");
        }
        if (!ValidationUtil.isPositiveInteger(busId)) {
            errors.add("Bus is required");
        }
        if (ValidationUtil.isBlank(departureTime)) {
            errors.add("Departure time is required");
        } else {
            try {
                LocalDateTime.parse(departureTime, FORMATTER);
            } catch (DateTimeParseException e) {
                errors.add("Invalid departure time format");
            }
        }
        if (ValidationUtil.isBlank(arrivalTime)) {
            errors.add("Arrival time is required");
        } else {
            try {
                LocalDateTime.parse(arrivalTime, FORMATTER);
            } catch (DateTimeParseException e) {
                errors.add("Invalid arrival time format");
            }
        }
        if (ValidationUtil.isBlank(price)) {
            errors.add("Price is required");
        } else {
            try {
                new java.math.BigDecimal(price);
            } catch (NumberFormatException e) {
                errors.add("Invalid price");
            }
        }
        if (!ValidationUtil.isPositiveInteger(availableSeats)) {
            errors.add("Available seats must be positive");
        }
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}