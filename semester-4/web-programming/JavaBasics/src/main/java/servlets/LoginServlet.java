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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Shared login servlet for all three user roles (Student, Professor,
 * Secretary). Checks the appropriate table based on the "role" form
 * field, verifying the submitted password against the stored salted
 * hash using PasswordUtil.
 *
 * @author Petros Travlos P21173
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = request.getParameter("role");
        String username = request.getParameter("username");
        String passwordAttempt = request.getParameter("password");

        String table;
        String idColumn;
        if ("student".equals(role)) {
            table = "students";
            idColumn = "student_id";
        } else if ("professor".equals(role)) {
            table = "professors";
            idColumn = "professor_id";
        } else if ("secretary".equals(role)) {
            table = "secretaries";
            idColumn = "secretary_id";
        } else {
            response.sendRedirect("index.jsp?error=1");
            return;
        }

        // Step 1: fetch salt, password_hash, and identity info by username ONLY.
        // We cannot filter by password in SQL anymore, since we no longer
        // store the plaintext password to compare against.
        String sql = "SELECT " + idColumn + ", name, surname, salt, password_hash FROM " + table +
                " WHERE username = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedSalt = resultSet.getString("salt");
                    String storedHash = resultSet.getString("password_hash");

                    // Step 2 + 3: hash the submitted password with the
                    // stored salt, and compare to the stored hash.
                    boolean passwordCorrect = PasswordUtil.verifyPassword(passwordAttempt, storedSalt, storedHash);

                    if (passwordCorrect) {
                        HttpSession session = request.getSession();
                        session.setAttribute("role", role);
                        session.setAttribute("userId", resultSet.getInt(idColumn));
                        session.setAttribute("userName", resultSet.getString("name") + " " + resultSet.getString("surname"));
                        response.sendRedirect("index.jsp");
                    } else {
                        response.sendRedirect("index.jsp?error=1");
                    }
                } else {
                    // No user with that username in this table at all
                    response.sendRedirect("index.jsp?error=1");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }
}