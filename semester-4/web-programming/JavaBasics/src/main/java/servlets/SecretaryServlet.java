package servlets;

import mainpackage.DBConnection;
import mainpackage.PasswordUtil;

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
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Servlet handling all Secretary-role actions: viewing all courses,
 * viewing courses with semester/professor, assigning a professor to
 * a course, and creating new student accounts.
 *
 * @author Petros Travlos P21173
 */
@WebServlet(name = "SecretaryServlet", urlPatterns = {"/SecretaryServlet"})
public class SecretaryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"secretary".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("viewCourses".equals(action)) {
            handleViewCourses(request, response);
        } else if ("viewCoursesWithProfessors".equals(action)) {
            handleViewCoursesWithProfessors(request, response);
        } else if ("showAssignForm".equals(action)) {
            handleShowAssignForm(request, response);
        } else if ("showAddStudentForm".equals(action)) {
            handleShowAddStudentForm(request, response);
        } else if ("showAddCourseForm".equals(action)) {
            handleShowAddCourseForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"secretary".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("assignProfessor".equals(action)) {
            handleAssignProfessor(request, response);
        } else if ("addStudent".equals(action)) {
            handleAddStudent(request, response);
        } else if ("addCourse".equals(action)) {
            handleAddCourse(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    private void handleViewCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "SELECT course_id, course_name FROM courses ORDER BY course_name";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");
                out.println("<h1>All Courses</h1>");
                out.println("<ul>");
                while (resultSet.next()) {
                    out.println("<li>" + resultSet.getString("course_name") + "</li>");
                }
                out.println("</ul>");
                out.println("<a href=\"SecretaryServlet?action=viewCoursesWithProfessors\">View with professors</a>");
                out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while listing courses", e);
        }
    }

    private void handleViewCoursesWithProfessors(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "SELECT c.course_name, c.semester, p.name, p.surname " +
                "FROM courses c " +
                "LEFT JOIN professors p ON c.professor_id = p.professor_id " +
                "ORDER BY c.course_name";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");
                out.println("<h1>Courses, Semester, and Professor</h1>");
                out.println("<table border=\"1\"><tr><th>Course</th><th>Semester</th><th>Professor</th></tr>");
                while (resultSet.next()) {
                    String professorName = resultSet.getString("name");
                    String display = (professorName == null)
                            ? "Unassigned"
                            : professorName + " " + resultSet.getString("surname");
                    out.println("<tr><td>" + resultSet.getString("course_name") + "</td><td>" +
                            resultSet.getString("semester") + "</td><td>" + display + "</td></tr>");
                }
                out.println("</table>");
                out.println("<a href=\"SecretaryServlet?action=showAssignForm\">Assign a professor to a course</a>");
                out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while listing courses with professors", e);
        }
    }

    private void handleShowAssignForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String coursesSql = "SELECT course_id, course_name FROM courses ORDER BY course_name";
        String professorsSql = "SELECT professor_id, name, surname FROM professors ORDER BY surname";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement courseStmt = connection.prepareStatement(coursesSql);
             ResultSet courseRs = courseStmt.executeQuery();
             PreparedStatement profStmt = connection.prepareStatement(professorsSql);
             ResultSet profRs = profStmt.executeQuery()) {

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");
                out.println("<h1>Assign Professor to Course</h1>");
                out.println("<form action=\"SecretaryServlet\" method=\"POST\">");
                out.println("<input type=\"hidden\" name=\"action\" value=\"assignProfessor\">");

                out.println("<label>Course:</label>");
                out.println("<select name=\"courseId\">");
                while (courseRs.next()) {
                    out.println("<option value=\"" + courseRs.getInt("course_id") + "\">" +
                            courseRs.getString("course_name") + "</option>");
                }
                out.println("</select><br><br>");

                out.println("<label>Professor:</label>");
                out.println("<select name=\"professorId\">");
                while (profRs.next()) {
                    out.println("<option value=\"" + profRs.getInt("professor_id") + "\">" +
                            profRs.getString("name") + " " + profRs.getString("surname") + "</option>");
                }
                out.println("</select><br><br>");

                out.println("<button type=\"submit\">Assign</button>");
                out.println("</form>");
                out.println("<br><a href=\"index.jsp\">Back to menu</a>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while building assignment form", e);
        }
    }

    private void handleAssignProfessor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        int professorId = Integer.parseInt(request.getParameter("professorId"));

        String sql = "UPDATE courses SET professor_id = ? WHERE course_id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, professorId);
            statement.setInt(2, courseId);
            statement.executeUpdate();

            response.sendRedirect("SecretaryServlet?action=viewCoursesWithProfessors");

        } catch (SQLException e) {
            throw new ServletException("Database error while assigning professor to course", e);
        }
    }

    /**
     * Displays the "create new student" form.
     * NOTE: this feature is not required by the assignment spec —
     * added at the user's request as extra functionality.
     */

    private void handleShowAddStudentForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Add New Student</h1>");

            if ("1".equals(request.getParameter("error"))) {
                out.println("<p style=\"color:red;\">That username is already taken.</p>");
            }

            out.println("<form action=\"SecretaryServlet\" method=\"POST\">");
            out.println("<input type=\"hidden\" name=\"action\" value=\"addStudent\">");

            out.println("<label>Username:</label>");
            out.println("<input type=\"text\" name=\"username\" required><br><br>");

            out.println("<label>Password:</label>");
            out.println("<input type=\"password\" name=\"password\" required><br><br>");

            out.println("<label>First name:</label>");
            out.println("<input type=\"text\" name=\"name\" required><br><br>");

            out.println("<label>Last name:</label>");
            out.println("<input type=\"text\" name=\"surname\" required><br><br>");

            out.println("<label>Department:</label>");
            out.println("<input type=\"text\" name=\"department\" required><br><br>");

            out.println("<p><em>Registration number will be generated automatically.</em></p>");

            out.println("<button type=\"submit\">Create Student</button>");
            out.println("</form>");
            out.println("<br><a href=\"index.jsp\">Back to menu</a>");
            out.println("</body></html>");
        }
    }

    /**
     * Creates a new student account: hashes the submitted password
     * with a fresh salt (same as LoginServlet's verification scheme),
     * then inserts the new row. Handles duplicate username/registration
     * number gracefully instead of crashing.
     * NOTE: this feature is not required by the assignment spec —
     * added at the user's request as extra functionality.
     */
    private void handleAddStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String department = request.getParameter("department");

        String salt = PasswordUtil.generateSalt();
        String passwordHash = PasswordUtil.hashPassword(password, salt);

        String nextRegNumberSql = "SELECT COALESCE(MAX(registration_number), 10000) + 1 AS nextRegNumber FROM students";
        String insertSql = "INSERT INTO students (username, salt, password_hash, name, surname, department, registration_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getInstance().getConnection()) {

            int registrationNumber;
            try (PreparedStatement nextRegStatement = connection.prepareStatement(nextRegNumberSql);
                 ResultSet resultSet = nextRegStatement.executeQuery()) {
                resultSet.next();
                registrationNumber = resultSet.getInt("nextRegNumber");
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, salt);
                insertStatement.setString(3, passwordHash);
                insertStatement.setString(4, name);
                insertStatement.setString(5, surname);
                insertStatement.setString(6, department);
                insertStatement.setInt(7, registrationNumber);
                insertStatement.executeUpdate();
            }

            response.sendRedirect("index.jsp");

        } catch (SQLIntegrityConstraintViolationException e) {
            // Only the username can collide now — registration_number is
            // computed fresh each time, so it will not collide under
            // normal (non-concurrent) use.
            response.sendRedirect("SecretaryServlet?action=showAddStudentForm&error=1");
        } catch (SQLException e) {
            throw new ServletException("Database error while creating student", e);
        }
    }
    /**
     * Displays the "create new course" form.
     * NOTE: this feature is not required by the assignment spec —
     * added at the user's request as extra functionality for testing.
     * Wires up the createCourseRecord stub originally sketched in
     * Phase 1's Secretaries class.
     */
    private void handleShowAddCourseForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Add New Course</h1>");

            out.println("<form action=\"SecretaryServlet\" method=\"POST\">");
            out.println("<input type=\"hidden\" name=\"action\" value=\"addCourse\">");

            out.println("<label>Course name:</label>");
            out.println("<input type=\"text\" name=\"courseName\" required><br><br>");

            out.println("<label>Semester (e.g. 2026-Spring):</label>");
            out.println("<input type=\"text\" name=\"semester\" required><br><br>");

            out.println("<p><em>No professor assigned yet — use \"Assign a course to a professor\" afterward.</em></p>");

            out.println("<button type=\"submit\">Create Course</button>");
            out.println("</form>");
            out.println("<br><a href=\"index.jsp\">Back to menu</a>");
            out.println("</body></html>");
        }
    }

    /**
     * Creates a new course with no professor assigned yet.
     * NOTE: this feature is not required by the assignment spec —
     * added at the user's request as extra functionality for testing.
     */
    private void handleAddCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String courseName = request.getParameter("courseName");
        String semester = request.getParameter("semester");

        String sql = "INSERT INTO courses (course_name, semester, professor_id) VALUES (?, ?, NULL)";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, courseName);
            statement.setString(2, semester);
            statement.executeUpdate();

            response.sendRedirect("SecretaryServlet?action=viewCoursesWithProfessors");

        } catch (SQLException e) {
            throw new ServletException("Database error while creating course", e);
        }
    }
}