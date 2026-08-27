package by.shved.busbooking.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private ValidationUtil() {}

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isPositiveInteger(String value) {
        if (isBlank(value)) {
            return false;
        }
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeInteger(String value) {
        if (isBlank(value)) {
            return false;
        }
        try {
            return Integer.parseInt(value.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
