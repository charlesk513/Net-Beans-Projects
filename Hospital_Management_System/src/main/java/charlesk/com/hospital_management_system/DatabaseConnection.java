/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package charlesk.com.hospital_management_system;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author charles
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_management";
    private static final String USER = "charles";
    private static final String PASSWORD = "k1c2@513";
    
    public static Connection getConnection() throws SQLException{
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
    
}
