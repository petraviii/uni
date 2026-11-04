package mainpackage;

/**
 * Represents a generic user of the grade management system.
 * This is the superclass for all user types: Students, Professors, and Secretaries.
 * It holds the fields and behavior common to every user, following the
 * DRY (Don't Repeat Yourself) principle of object-oriented design.
 *
 * @author Petros Travlos P21173
 */
public class Users {

    // ----- Instance fields (private for encapsulation) -----
    private String username;
    private String name;
    private String surname;
    private String department;

    // ----- Static field: shared across ALL Users objects -----
    // Starts at 0, and increases by 1 every time a Users (or subclass) constructor runs.
    private static int usersCounter = 0;

    /**
     * Constructs a new Users object with the given details.
     * Increments the shared usersCounter each time a new user is created.
     *
     * @param username   the user's login/username
     * @param name       the user's first name
     * @param surname    the user's last name
     * @param department the department the user belongs to
     */
    public Users(String username, String name, String surname, String department) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.department = department;
        usersCounter++; // one more user has been created
    }

    // ----- Getters and setters (standard encapsulation pattern) -----

    /** @return the user's username */
    public String getUsername() {
        return username;
    }

    /** @param username the new username to set */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the user's first name */
    public String getName() {
        return name;
    }

    /** @param name the new first name to set */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the user's last name */
    public String getSurname() {
        return surname;
    }

    /** @param surname the new last name to set */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /** @return the department the user belongs to */
    public String getDepartment() {
        return department;
    }

    /** @param department the new department to set */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns the total number of Users objects created so far
     * (including subclass instances, since they call this constructor).
     * Note: there is intentionally no setter for this — it should only
     * ever change via object creation, never be set directly.
     *
     * @return the current value of usersCounter
     */
    public static int getUsersCounter() {
        return usersCounter;
    }

    /**
     * Returns a human-readable summary of this user's details.
     * Useful for printing/debugging.
     *
     * @return a formatted string describing the user
     */
    @Override
    public String toString() {
        return "Username: " + username +
                ", Name: " + name +
                ", Surname: " + surname +
                ", Department: " + department;
    }
}