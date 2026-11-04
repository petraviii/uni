package mainpackage;

/**
 * Custom checked exception thrown when user input data fails validation —
 * for example, when a registration number is not a valid integer, or a
 * required String field (name, surname, department) is null or blank.
 *
 * Being a checked exception (extends Exception, not RuntimeException),
 * callers are required to either catch it or declare it, ensuring
 * invalid data is never silently ignored.
 *
 * @author Petros Travlos P21173
 */
public class InvalidDataException extends Exception {

    /**
     * Constructs a new InvalidDataException with a descriptive message
     * explaining what validation rule was violated.
     *
     * @param message a human-readable explanation of the invalid data
     */
    public InvalidDataException(String message) {
        super(message);
    }
}