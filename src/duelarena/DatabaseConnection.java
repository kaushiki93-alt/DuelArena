
package duelarena;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/duel_arena_db";

    private static final String USER = "root";

    // Enter your MySQL root password here
    private static final String PASSWORD = "Mysql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
