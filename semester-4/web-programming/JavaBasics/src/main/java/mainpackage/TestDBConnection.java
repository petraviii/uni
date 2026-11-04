package mainpackage;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Standalone test class to verify DBConnection works correctly,
 * just a temporary diagnostic tool.
 *
 * @author Petros Travlos P21173
 */
public class TestDBConnection {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("✅ Successfully connected to grade_management database!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}