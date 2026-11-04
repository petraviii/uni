package mainpackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Professor user in the grade management system.
 * Extends Users, inheriting username, name, surname, and department,
 * and adds a list of courses currently assigned to this professor.
 *
 * @author Petros Travlos P21173
 */
public class Professors extends Users {

    private List<String> assignedCourses;

    /**
     * Constructs a new Professors object with no courses assigned yet.
     * Calls the Users constructor first, then initializes an empty course list.
     *
     * @param username   the professor's login/username
     * @param name       the professor's first name
     * @param surname    the professor's last name
     * @param department the department the professor belongs to
     */
    public Professors(String username, String name, String surname, String department) {
        super(username, name, surname, department);
        this.assignedCourses = new ArrayList<>();
    }

    /**
     * Returns the list of course names currently assigned to this professor.
     *
     * @return a list of course names
     */
    public List<String> getAssignedCourses() {
        return assignedCourses;
    }

    /**
     * Replaces this professor's entire list of assigned courses.
     *
     * @param assignedCourses the new list of course names
     */
    public void setAssignedCourses(List<String> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }

    /**
     * Assigns a single new course to this professor by adding it to their list.
     * (This will later be called by the Secretaries class, which manages
     * course assignments.)
     *
     * @param courseName the name of the course to add
     */
    public void addCourse(String courseName) {
        assignedCourses.add(courseName);
        System.out.println("Course \"" + courseName + "\" assigned to professor " + getName() + " " + getSurname());
    }

    /**
     * Placeholder for a Professor setting a grade for a student.
     * Full implementation will come once Students, Courses, and Grades
     * are connected (Phase 2/3).
     *
     * @param studentUsername the username of the student being graded
     * @param courseName      the course the grade applies to
     * @param grade           the grade value
     */
    public void setGrade(String studentUsername, String courseName, double grade) {
        System.out.println("Setting grade " + grade + " for student " + studentUsername + " in course " + courseName);
    }

    /**
     * Placeholder for a Professor viewing all grades, but only for
     * the courses they are assigned to.
     */
    public void viewGradesForOwnCourses() {
        System.out.println("Displaying grades for all courses taught by " + getName() + " " + getSurname());
    }

    /**
     * Returns a human-readable summary of this professor, including
     * inherited fields plus their assigned courses.
     *
     * @return a formatted string describing the professor
     */
    @Override
    public String toString() {
        return super.toString() + ", Assigned Courses: " + assignedCourses;
    }
}