
package duelarena;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MatchHistoryDAO {

    public boolean recordMatch(
            PlayerAccount player1,
            PlayerAccount player2,
            PlayerAccount winner,
            String player1Character,
            String player2Character,
            int pointsAwarded) {

        if (player1 == null || player2 == null || winner == null) {
            System.out.println("Match not saved: account information missing.");
            return false;
        }

        if (player1.getPlayerId() == player2.getPlayerId()) {
            System.out.println("Match not saved: players must be different.");
            return false;
        }

        if (winner.getPlayerId() != player1.getPlayerId()
                && winner.getPlayerId() != player2.getPlayerId()) {
            System.out.println("Match not saved: invalid winner.");
            return false;
        }

        String insertMatch =
                "INSERT INTO match_history "
                        + "(player1_id, player2_id, winner_id, "
                        + "player1_character, player2_character, points_awarded) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

        String updateMatches =
                "UPDATE players SET total_matches = total_matches + 1 "
                        + "WHERE player_id = ?";

        String updateWinner =
                "UPDATE players SET total_wins = total_wins + 1, "
                        + "total_points = total_points + ? "
                        + "WHERE player_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                // Save match history.
                try (PreparedStatement ps =
                             con.prepareStatement(insertMatch)) {

                    ps.setInt(1, player1.getPlayerId());
                    ps.setInt(2, player2.getPlayerId());
                    ps.setInt(3, winner.getPlayerId());
                    ps.setString(4, player1Character);
                    ps.setString(5, player2Character);
                    ps.setInt(6, pointsAwarded);

                    ps.executeUpdate();
                }

                // Increase both players' match counts.
                try (PreparedStatement ps =
                             con.prepareStatement(updateMatches)) {

                    ps.setInt(1, player1.getPlayerId());
                    ps.executeUpdate();

                    ps.setInt(1, player2.getPlayerId());
                    ps.executeUpdate();
                }

                // Increase winner's wins and points.
                try (PreparedStatement ps =
                             con.prepareStatement(updateWinner)) {

                    ps.setInt(1, pointsAwarded);
                    ps.setInt(2, winner.getPlayerId());
                    ps.executeUpdate();
                }

                con.commit();

                System.out.println("Match saved successfully!");
                return true;

            } catch (SQLException e) {

                con.rollback();
                System.out.println("Match saving failed. Changes rolled back.");
                e.printStackTrace();
                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println("Database error while saving match.");
            e.printStackTrace();
            return false;
        }
    }
}
