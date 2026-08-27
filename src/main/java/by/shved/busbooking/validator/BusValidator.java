package by.shved.busbooking.validator;

import java.util.ArrayList;
import java.util.List;

public class BusValidator {
    private final List<String> errors = new ArrayList<>();

    public boolean validate(String busNumber, String brand, String driverId, String startYear, String mileage, String seatCount) {
        errors.clear();
        if (ValidationUtil.isBlank(busNumber)) {
            errors.add("Bus number is required");
        }
        if (ValidationUtil.isBlank(brand)) {
            errors.add("Brand is required");
        }
        if (!ValidationUtil.isPositiveInteger(driverId)) {
            errors.add("Please select a driver");
        }
        if (!ValidationUtil.isPositiveInteger(startYear) || Integer.parseInt(startYear) < 1900) {
            errors.add("Invalid start year");
        }
        if (!ValidationUtil.isNonNegativeInteger(mileage)) {
            errors.add("Invalid mileage (must be non-negative)");
        }
        if (!ValidationUtil.isPositiveInteger(seatCount)) {
            errors.add("Invalid seat count (must be positive)");
        }
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
