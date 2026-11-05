
package duelarena;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println("MySQL Database Connected Successfully!");

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }
}
