package mainpackage;

/**
 * Utility class containing static validation methods used to check
 * user input before Users/Students/Professors/Secretaries objects
 * are constructed. Throws InvalidDataException on any failure.
 *
 * @author Petros Travlos P21173
 */
public class DataValidator {

    /**
     * Validates that a String field is non-null and non-blank.
     *
     * @param value     the value to check
     * @param fieldName the name of the field, used in the error message
     * @throws InvalidDataException if the value is null or blank
     */
    public static void validateStringField(String value, String fieldName) throws InvalidDataException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " must not be empty.");
        }
    }

    /**
     * Validates that a given String represents a valid integer, and
     * returns the parsed integer if so.
     *
     * @param value     the raw String value (e.g. read from a file or terminal)
     * @param fieldName the name of the field, used in the error message
     * @return the parsed integer value
     * @throws InvalidDataException if the value cannot be parsed as an integer
     */
    public static int validateRegistrationNumber(String value, String fieldName) throws InvalidDataException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new InvalidDataException(fieldName + " must be a valid integer, but got: \"" + value + "\"");
        }
    }
}