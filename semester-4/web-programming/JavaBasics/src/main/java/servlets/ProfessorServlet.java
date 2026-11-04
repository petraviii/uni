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
 * Servlet handling all Professor-role actions: viewing the grade list
 * for an already-graded course, and entering grades for a not-yet-graded
 * course.
 *
 * All course lists are scoped to the logged-in professor's own ID
 * (from the session), so a professor only ever sees their own courses.
 *
 * @author Petros Travlos P21173
 */
@WebServlet(name = "ProfessorServlet", urlPatterns = {"/ProfessorServlet"})
public class ProfessorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"professor".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }
        int professorId = (int) session.getAttribute("userId");

        String action = request.getParameter("action");

        if ("viewGradedCourse".equals(action)) {
            showGradedCourseSelectionForm(request, response, professorId);
        } else if ("gradeListForCourse".equals(action)) {
            showGradeListForCourse(request, response, professorId);
        } else if ("selectCourseToGrade".equals(action)) {
            showUngradedCourseSelectionForm(request, response, professorId);
        } else if ("showGradeEntryForm".equals(action)) {
            showGradeEntryForm(request, response, professorId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"professor".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }
        int professorId = (int) session.getAttribute("userId");

        String action = request.getParameter("action");

        if ("submitGrade".equals(action)) {
            submitGrade(request, response, professorId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    /**
     * Shows a dropdown of this professor's courses that already have
     * at least one grade recorded.
     */
    private void showGradedCourseSelectionForm(HttpServletRequest request, HttpServletResponse response, int professorId)
            throws ServletException, IOException {

        String sql = "SELECT DISTINCT c.course_id, c.course_name " +
                "FROM courses c JOIN grades g ON c.course_id = g.course_id " +
                "WHERE c.professor_id = ? ORDER BY c.course_name";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, professorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>View Grade List (Already-Graded Courses)</h1>");
                    out.println("<form action=\"ProfessorServlet\" method=\"GET\">");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"gradeListForCourse\">");
                    out.println("<select name=\"courseId\">");
                    boolean any = false;
                    while (resultSet.next()) {
                        any = true;
                        out.println("<option value=\"" + resultSet.getInt("course_id") + "\">" +
                                resultSet.getString("course_name") + "</option>");
                    }
                    out.println("</select>");
                    if (any) {
                        out.println("<button type=\"submit\">View</button>");
                    } else {
                        out.println("(No graded courses yet)");
                    }
                    out.println("</form>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while listing graded courses", e);
        }
    }

    /**
     * Displays every student's grade for one specific course.
     * Satisfies assignment step 2.3.2: "view grade list per course
     * (for already-graded courses)."
     */
    private void showGradeListForCourse(HttpServletRequest request, HttpServletResponse response, int professorId)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));

        String sql = "SELECT s.name, s.surname, s.registration_number, g.semester, g.score " +
                "FROM grades g " +
                "JOIN students s ON g.student_id = s.student_id " +
                "JOIN courses c ON g.course_id = c.course_id " +
                "WHERE g.course_id = ? AND c.professor_id = ? " +
                "ORDER BY s.surname";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);
            statement.setInt(2, professorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>Grades for This Course</h1>");
                    out.println("<table border=\"1\"><tr><th>Student</th><th>Reg. No.</th><th>Semester</th><th>Score</th></tr>");
                    while (resultSet.next()) {
                        out.println("<tr><td>" + resultSet.getString("name") + " " + resultSet.getString("surname") +
                                "</td><td>" + resultSet.getInt("registration_number") +
                                "</td><td>" + resultSet.getString("semester") +
                                "</td><td>" + resultSet.getDouble("score") + "</td></tr>");
                    }
                    out.println("</table>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while fetching grade list", e);
        }
    }

    /**
     * Shows a dropdown of ALL courses this professor teaches, so they
     * can pick one to enter a grade for. (Previously this only showed
     * courses with zero grades at all — redesigned so professors can
     * grade additional students in a course that already has some
     * grades recorded.)
     */
    private void showUngradedCourseSelectionForm(HttpServletRequest request, HttpServletResponse response, int professorId)
            throws ServletException, IOException {

        String sql = "SELECT course_id, course_name FROM courses WHERE professor_id = ? ORDER BY course_name";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, professorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>Enter a Grade</h1>");
                    out.println("<form action=\"ProfessorServlet\" method=\"GET\">");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"showGradeEntryForm\">");
                    out.println("<select name=\"courseId\">");
                    boolean any = false;
                    while (resultSet.next()) {
                        any = true;
                        out.println("<option value=\"" + resultSet.getInt("course_id") + "\">" +
                                resultSet.getString("course_name") + "</option>");
                    }
                    out.println("</select>");
                    if (any) {
                        out.println("<button type=\"submit\">Continue</button>");
                    } else {
                        out.println("(You have no assigned courses)");
                    }
                    out.println("</form>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while listing your courses", e);
        }
    }

    /**
     * Shows the grade-entry form for one specific course: pick a
     * student who does NOT already have a grade in this course,
     * enter a semester and score.
     */

    private void showGradeEntryForm(HttpServletRequest request, HttpServletResponse response, int professorId)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));

        // Only students who do NOT yet have a grade for this specific course.
        String sql = "SELECT student_id, name, surname, registration_number FROM students " +
                "WHERE student_id NOT IN (SELECT student_id FROM grades WHERE course_id = ?) " +
                "ORDER BY surname";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<html><body>");
                    out.println("<h1>Enter a Grade</h1>");

                    out.println("<form action=\"ProfessorServlet\" method=\"POST\">");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"submitGrade\">");
                    out.println("<input type=\"hidden\" name=\"courseId\" value=\"" + courseId + "\">");

                    out.println("<label>Student:</label>");
                    out.println("<select name=\"studentId\">");
                    boolean any = false;
                    while (resultSet.next()) {
                        any = true;
                        out.println("<option value=\"" + resultSet.getInt("student_id") + "\">" +
                                resultSet.getString("name") + " " + resultSet.getString("surname") +
                                " (" + resultSet.getInt("registration_number") + ")</option>");
                    }
                    out.println("</select><br><br>");

                    if (any) {
                        out.println("<label>Semester (e.g. 2026-Spring):</label>");
                        out.println("<input type=\"text\" name=\"semester\" required><br><br>");

                        out.println("<label>Score:</label>");
                        out.println("<input type=\"number\" step=\"0.01\" min=\"0\" max=\"10\" name=\"score\" required><br><br>");

                        out.println("<button type=\"submit\">Submit Grade</button>");
                    } else {
                        out.println("<p>Every student already has a grade for this course.</p>");
                    }
                    out.println("</form>");
                    out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                    out.println("</body></html>");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while building grade entry form", e);
        }
    }

    /**
     * Inserts a new grade record for the given student/course/semester/score.
     * Satisfies assignment step 2.3.2: "enter grade per course
     * (for not-yet-graded courses)."
     */
    private void submitGrade(HttpServletRequest request, HttpServletResponse response, int professorId)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String semester = request.getParameter("semester");
        double score = Double.parseDouble(request.getParameter("score"));

        // Verify this course actually belongs to this professor before
        // inserting anything — prevents a professor from grading a
        // course that isn't theirs, even if someone tampers with the form.
        String ownershipCheckSql = "SELECT course_id FROM courses WHERE course_id = ? AND professor_id = ?";
        String insertSql = "INSERT INTO grades (student_id, course_id, semester, score) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(ownershipCheckSql)) {

            checkStatement.setInt(1, courseId);
            checkStatement.setInt(2, professorId);

            try (ResultSet checkResult = checkStatement.executeQuery()) {
                if (!checkResult.next()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not teach this course.");
                    return;
                }
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setInt(1, studentId);
                insertStatement.setInt(2, courseId);
                insertStatement.setString(3, semester);
                insertStatement.setDouble(4, score);
                insertStatement.executeUpdate();
            }

            response.sendRedirect("ProfessorServlet?action=viewGradedCourse");

        } catch (SQLException e) {
            throw new ServletException("Database error while submitting grade", e);
        }
    }
}