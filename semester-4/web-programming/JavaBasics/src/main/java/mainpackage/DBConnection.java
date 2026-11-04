package mainpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  utility class responsible for creating JDBC connections
 * to the grade_management MySQL database.
 *
 * There is only ever one instance of DBConnection itself (singleton),
 * but each call to getConnection() returns a fresh java.sql.Connection.
 * This avoids sharing a single Connection object across concurrent
 * servlet requests, which is unsafe since Connection is not thread-safe.
 *
 * @author Petros Travlos P21173
 */

public class DBConnection {

    // ----- Connection details -----
    private static final String URL = "jdbc:mysql://localhost:3306/grade_management";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password_here";

    // The single instance of this class (singleton pattern)
    private static DBConnection instance;

    // NEW: static block runs once when this class is first loaded.
    // Forces the MySQL driver to register itself with DriverManager
    // within this webapp's classloader — fixes "No suitable driver
    // found" errors that can occur inside Tomcat.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found on classpath", e);
        }
    }

    /**
     * Private constructor prevents any other class from doing
     * "new DBConnection()" directly — the only way to get access
     * is through getInstance() below.
     */
    private DBConnection() {
    }

    /**
     * Returns the single shared instance of DBConnection, creating
     * it on first use (lazy initialization).
     *
     * @return the singleton DBConnection instance
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Opens and returns a new JDBC connection to the grade_management
     * database. Callers are responsible for closing this connection
     * when done (a try-with-resources block is recommended).
     *
     * @return an open java.sql.Connection
     * @throws SQLException if the connection cannot be established
     *                       (e.g. MySQL isn't running, wrong credentials)
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}