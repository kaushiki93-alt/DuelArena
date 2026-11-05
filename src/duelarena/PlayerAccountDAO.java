
package duelarena;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.util.Base64;

public class PlayerAccountDAO {

    // Generate a random salt for each account
    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    // Convert password into a secure hash
    private String hashPassword(String password, String salt)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                Base64.getDecoder().decode(salt),
                65536,
                256
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);

        } finally {
            spec.clearPassword();
        }
    }

    // Register a new player
    public boolean registerPlayer(String username, String password) {

        String salt = generateSalt();

        try {
            String passwordHash = hashPassword(password, salt);

            String sql = "INSERT INTO players "
                    + "(username, password_hash, password_salt) "
                    + "VALUES (?, ?, ?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, passwordHash);
                ps.setString(3, salt);

                ps.executeUpdate();

                System.out.println("Player registered successfully!");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    // Login an existing player
    public PlayerAccount loginPlayer(String username, String password) {

        String sql = "SELECT * FROM players WHERE username = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String salt = rs.getString("password_salt");
                    String storedHash = rs.getString("password_hash");

                    String enteredHash = hashPassword(password, salt);

                    boolean passwordMatches = MessageDigest.isEqual(
                            Base64.getDecoder().decode(storedHash),
                            Base64.getDecoder().decode(enteredHash)
                    );

                    if (passwordMatches) {

                        System.out.println("Login successful!");

                        return new PlayerAccount(
                                rs.getInt("player_id"),
                                rs.getString("username"),
                                rs.getInt("total_wins"),
                                rs.getInt("total_matches"),
                                rs.getInt("total_points"),
                                rs.getString("current_character")
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        System.out.println("Invalid username or password.");
        return null;
    }
}
