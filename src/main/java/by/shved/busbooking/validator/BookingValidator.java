package by.shved.busbooking.validator;

import java.util.ArrayList;
import java.util.List;

public class BookingValidator {
    private final List<String> errors = new ArrayList<>();

    public boolean validate(String tripId, String seatNumber) {
        errors.clear();
        if (!ValidationUtil.isPositiveInteger(tripId)) {
            errors.add("Please select a trip");
        }
        if (!ValidationUtil.isPositiveInteger(seatNumber)) {
            errors.add("Invalid seat number");
        }
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
