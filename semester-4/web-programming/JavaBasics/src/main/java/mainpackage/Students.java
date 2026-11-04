package mainpackage;

/**
 * Represents a Student user in the grade management system.
 * Extends Users, inheriting username, name, surname, and department,
 * and adds a registration number that is assigned once and cannot change.
 *
 * @author Petros Travlos P21173
 */
public class Students extends Users {

    // final => assigned exactly once (in the constructor), then immutable.
    private final int registrationNumber;

    /**
     * Constructs a new Students object.
     * Calls the Users constructor first (via super), then sets the
     * registration number, which becomes permanent for this object.
     *
     * @param username           the student's login/username
     * @param name               the student's first name
     * @param surname            the student's last name
     * @param department         the department the student belongs to
     * @param registrationNumber the student's unique registration number (immutable)
     */
    public Students(String username, String name, String surname, String department, int registrationNumber) {
        // Step 1: let Users handle the shared fields + increment usersCounter
        super(username, name, surname, department);
        // Step 2: handle the Students-specific field
        this.registrationNumber = registrationNumber;
    }

    /**
     * Returns the student's registration number.
     * Note: there is intentionally NO setter for this field — it is final
     * and immutable by design.
     *
     * @return the registration number
     */
    public int getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Placeholder for a Student viewing their own grades.
     * Full implementation will come once we build the Grades class
     * and connect it to real data (Phase 2/3).
     */
    public void viewGrades() {
        System.out.println("Displaying grades for student with registration number: " + registrationNumber);
    }

    /**
     * Returns a human-readable summary of this student, including
     * the fields inherited from Users plus the registration number.
     *
     * @return a formatted string describing the student
     */
    @Override
    public String toString() {
        return super.toString() + ", Registration Number: " + registrationNumber;
    }
}