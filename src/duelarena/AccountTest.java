
package duelarena;

public class AccountTest {

    public static void main(String[] args) {

        PlayerAccountDAO dao = new PlayerAccountDAO();

        // Register a test account
        String username = "TestPlayer";
        String password = "TestPass123";

        boolean registered = dao.registerPlayer(username, password);

        if (registered) {
            System.out.println("Registration Test: PASSED");
        } else {
            System.out.println("Registration Test: Account may already exist.");
        }

        // Test login
        PlayerAccount account =
                dao.loginPlayer(username, password);

        if (account != null) {
            System.out.println("Login Test: PASSED");
            System.out.println("Player ID: " + account.getPlayerId());
            System.out.println("Username: " + account.getUsername());
        } else {
            System.out.println("Login Test: FAILED");
        }
    }
}
