package mainpackage;

/**
 * Represents a Course offered in the grade management system.
 * A course may or may not have a professor assigned to teach it yet.
 *
 * @author Petros Travlos P21173
 */
public class Courses {

    private String courseId;
    private String courseName;

    // Composition: a Course "has a" Professor (can be null if unassigned)
    private Professors assignedProfessor;

    /**
     * Constructs a new Courses object with no professor assigned yet.
     *
     * @param courseId   a unique identifier for the course (e.g. "CS101")
     * @param courseName the human-readable name of the course
     */
    public Courses(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.assignedProfessor = null; // not assigned yet
    }

    /** @return the course's unique identifier */
    public String getCourseId() {
        return courseId;
    }

    /** @param courseId the new course identifier */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /** @return the course's human-readable name */
    public String getCourseName() {
        return courseName;
    }

    /** @param courseName the new course name */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /** @return the professor currently assigned to this course, or null if none */
    public Professors getAssignedProfessor() {
        return assignedProfessor;
    }

    /** @param assignedProfessor the professor to assign to this course */
    public void setAssignedProfessor(Professors assignedProfessor) {
        this.assignedProfessor = assignedProfessor;
    }

    /**
     * Returns a human-readable summary of this course, including the
     * assigned professor's name if one exists.
     *
     * @return a formatted string describing the course
     */
    @Override
    public String toString() {
        String professorName = (assignedProfessor == null)
                ? "Unassigned"
                : assignedProfessor.getName() + " " + assignedProfessor.getSurname();
        return "Course ID: " + courseId + ", Name: " + courseName + ", Professor: " + professorName;
    }
}