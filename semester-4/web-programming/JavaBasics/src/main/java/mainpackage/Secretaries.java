package mainpackage;

/**
 * Represents a Secretary user in the grade management system.
 * Extends Users, inheriting username, name, surname, and department.
 * Secretaries have no additional fields — their role is defined by
 * administrative behavior: creating records and assigning courses.
 *
 * @author Petros Travlos P21173
 */
public class Secretaries extends Users {

    /**
     * Constructs a new Secretaries object.
     * Calls the Users constructor directly, since Secretaries adds
     * no extra fields of its own.
     *
     * @param username   the secretary's login/username
     * @param name       the secretary's first name
     * @param surname    the secretary's last name
     * @param department the department the secretary belongs to
     */
    public Secretaries(String username, String name, String surname, String department) {
        super(username, name, surname, department);
    }

    /**
     * Placeholder for creating a new Professor record.
     * Full implementation (real object creation + storage) comes later
     * once we have persistent storage (Phase 2: database).
     *
     * @param username   the new professor's username
     * @param name       the new professor's first name
     * @param surname    the new professor's last name
     * @param department the new professor's department
     */
    public void createProfessorRecord(String username, String name, String surname, String department) {
        System.out.println("Professor record created for: " + name + " " + surname);
    }

    /**
     * Placeholder for creating a new Student record.
     *
     * @param username           the new student's username
     * @param name               the new student's first name
     * @param surname            the new student's last name
     * @param department         the new student's department
     * @param registrationNumber the new student's registration number
     */
    public void createStudentRecord(String username, String name, String surname, String department, int registrationNumber) {
        System.out.println("Student record created for: " + name + " " + surname + " (Reg #: " + registrationNumber + ")");
    }

    /**
     * Placeholder for creating a new Course record.
     *
     * @param courseName the name of the course to create
     */
    public void createCourseRecord(String courseName) {
        System.out.println("Course record created: " + courseName);
    }

    /**
     * Assigns a professor to teach a specific course.
     * Delegates to the Professors class's own addCourse() method,
     * since the professor is the one who "owns" their course list.
     *
     * @param professor  the professor to assign
     * @param courseName the course to assign them to
     */
    public void assignProfessorToCourse(Professors professor, String courseName) {
        professor.addCourse(courseName);
    }

    /**
     * Placeholder for generating the list of students who need to be
     * graded for a given course.
     *
     * @param courseName the course to generate the student list for
     */
    public void createGradingListForCourse(String courseName) {
        System.out.println("The list of students for course \"" + courseName + "\" has been created.");
    }

    /**
     * Returns a human-readable summary of this secretary.
     * Secretaries have no extra fields, so this just reuses the parent's toString().
     *
     * @return a formatted string describing the secretary
     */
    @Override
    public String toString() {
        return super.toString() + " [Role: Secretary]";
    }
}