package charlesk.com.examprep;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author charles
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/exam_prep";
    private static final String USER = "charles";
    private static final String PASSWORD = "k1c2@513";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection( URL, USER, PASSWORD);
    }
    
}