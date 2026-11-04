package mainpackage;

/**
 * Represents a single grade record: one student's score in one course,
 * for a specific semester.
 *
 * @author Petros Travlos P21173
 */
public class Grades {

    // Composition: a Grade "has a" Student and "has a" Course
    private Students student;
    private Courses course;
    private String semester;
    private double score;

    /**
     * Constructs a new Grades record.
     *
     * @param student  the student receiving the grade
     * @param course   the course the grade applies to
     * @param semester the semester this grade was awarded in (e.g. "2026-Spring")
     * @param score    the numeric grade value
     */
    public Grades(Students student, Courses course, String semester, double score) {
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.score = score;
    }

    /** @return the student this grade belongs to */
    public Students getStudent() {
        return student;
    }

    /** @param student the student to associate with this grade */
    public void setStudent(Students student) {
        this.student = student;
    }

    /** @return the course this grade applies to */
    public Courses getCourse() {
        return course;
    }

    /** @param course the course to associate with this grade */
    public void setCourse(Courses course) {
        this.course = course;
    }

    /** @return the semester this grade was awarded in */
    public String getSemester() {
        return semester;
    }

    /** @param semester the semester to set */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /** @return the numeric score of this grade */
    public double getScore() {
        return score;
    }

    /** @param score the new score value */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Returns a human-readable summary of this grade record.
     *
     * @return a formatted string describing the grade
     */
    @Override
    public String toString() {
        return "Student: " + student.getName() + " " + student.getSurname() +
                ", Course: " + course.getCourseName() +
                ", Semester: " + semester +
                ", Score: " + score;
    }
}