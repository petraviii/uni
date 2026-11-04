package servlets;

import mainpackage.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet handling all Student-role actions: viewing grades per course,
 * per semester, and viewing the overall (GPA) grade.
 *
 * All queries are scoped to the logged-in student's own ID (from the
 * session), so a student can never view another student's grades.
 *
 * @author Petros Travlos P21173
 */
@WebServlet(name = "StudentServlet", urlPatterns = {"/StudentServlet"})
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }
        int studentId = (int) session.getAttribute("userId");

        String action = request.getParameter("action");

        if ("viewGradesByCourse".equals(action)) {
            showCourseSelectionForm(request, response, studentId);
        } else if ("gradesForCourse".equals(action)) {
            showGradesForCourse(request, response, studentId);
        } else if ("viewGradesBySemester".equals(action)) {
            showSemesterSelectionForm(request, response, studentId);
        } else if ("gradesForSemester".equals(action)) {
            showGradesForSemester(request, response, studentId);
        } else if ("viewOverallGrade".equals(action)) {
            showOverallGrade(request, response, studentId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    /**
     * Shows a dropdown of courses this student has at least one grade
     * in, so they can pick which one to view.
     */
    private void showCourseSelectionForm(HttpServletRequest request, HttpServletResponse response, int studentId)
            throws ServletException, IOException {

        String sql = "SELECT DISTINCT c.course_id, c.course_name " +
                "FROM grades g JOIN courses c ON g.course_id = c.course_id " +
                "WHERE g.student_id = ? ORDER BY c.course_name";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>View Grades by Course</h1>");
                    out.println("<form action=\"StudentServlet\" method=\"GET\">");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"gradesForCourse\">");
                    out.println("<select name=\"courseId\">");
                    while (resultSet.next()) {
                        out.println("<option value=\"" + resultSet.getInt("course_id") + "\">" +
                                resultSet.getString("course_name") + "</option>");
                    }
                    out.println("</select>");
                    out.println("<button type=\"submit\">View</button>");
                    out.println("</form>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while listing courses", e);
        }
    }

    /**
     * Displays the grade(s) this student has in one specific course.
     * Satisfies assignment step 2.3.1: "view grades per course."
     */
    private void showGradesForCourse(HttpServletRequest request, HttpServletResponse response, int studentId)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));

        String sql = "SELECT c.course_name, g.semester, g.score " +
                "FROM grades g JOIN courses c ON g.course_id = c.course_id " +
                "WHERE g.student_id = ? AND g.course_id = ? ORDER BY g.semester";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>Grades for This Course</h1>");
                    out.println("<table border=\"1\"><tr><th>Course</th><th>Semester</th><th>Score</th></tr>");
                    while (resultSet.next()) {
                        out.println("<tr><td>" + resultSet.getString("course_name") + "</td><td>" +
                                resultSet.getString("semester") + "</td><td>" +
                                resultSet.getDouble("score") + "</td></tr>");
                    }
                    out.println("</table>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while fetching course grades", e);
        }
    }

    /**
     * Shows a dropdown of semesters this student has grades in.
     */
    private void showSemesterSelectionForm(HttpServletRequest request, HttpServletResponse response, int studentId)
            throws ServletException, IOException {

        String sql = "SELECT DISTINCT semester FROM grades WHERE student_id = ? ORDER BY semester";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>View Grades by Semester</h1>");
                    out.println("<form action=\"StudentServlet\" method=\"GET\">");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"gradesForSemester\">");
                    out.println("<select name=\"semester\">");
                    while (resultSet.next()) {
                        String semester = resultSet.getString("semester");
                        out.println("<option value=\"" + semester + "\">" + semester + "</option>");
                    }
                    out.println("</select>");
                    out.println("<button type=\"submit\">View</button>");
                    out.println("</form>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while listing semesters", e);
        }
    }

    /**
     * Displays all grades this student received in one specific semester.
     * Satisfies assignment step 2.3.1: "view grades per semester."
     */
    private void showGradesForSemester(HttpServletRequest request, HttpServletResponse response, int studentId)
            throws ServletException, IOException {

        String semester = request.getParameter("semester");

        String sql = "SELECT c.course_name, g.score " +
                "FROM grades g JOIN courses c ON g.course_id = c.course_id " +
                "WHERE g.student_id = ? AND g.semester = ? ORDER BY c.course_name";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setString(2, semester);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>Grades for " + semester + "</h1>");
                    out.println("<table border=\"1\"><tr><th>Course</th><th>Score</th></tr>");
                    while (resultSet.next()) {
                        out.println("<tr><td>" + resultSet.getString("course_name") + "</td><td>" +
                                resultSet.getDouble("score") + "</td></tr>");
                    }
                    out.println("</table>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while fetching semester grades", e);
        }
    }

    /**
     * Computes and displays this student's overall average grade (GPA)
     * across all courses they've been examined in.
     * Satisfies assignment step 2.3.1: "view overall/total grade."
     */
    private void showOverallGrade(HttpServletRequest request, HttpServletResponse response, int studentId)
            throws ServletException, IOException {

        String sql = "SELECT AVG(score) AS gpa, COUNT(*) AS courseCount FROM grades WHERE student_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>Overall Grade</h1>");
                    if (resultSet.next() && resultSet.getInt("courseCount") > 0) {
                        double gpa = resultSet.getDouble("gpa");
                        int courseCount = resultSet.getInt("courseCount");
                        out.printf("<p>Average grade across %d examined course(s): <strong>%.2f</strong></p>%n",
                                courseCount, gpa);
                    } else {
                        out.println("<p>No grades recorded yet.</p>");
                    }
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while computing overall grade", e);
        }
    }
}