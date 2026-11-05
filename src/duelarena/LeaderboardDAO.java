
package duelarena;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardDAO {

    public List<Object[]> getLeaderboard() {

        List<Object[]> leaderboard = new ArrayList<>();

        String sql =
                "SELECT p.username, " +
                        "p.total_wins, " +
                        "p.total_matches, " +
                        "p.total_points, " +
                        "COALESCE(latest.character_type, 0) AS character_type " +

                        "FROM players p " +

                        "LEFT JOIN ( " +

                        "   SELECT player_id, " +
                        "   CASE LOWER(TRIM(character_value)) " +
                        "       WHEN '0' THEN 0 " +
                        "       WHEN '1' THEN 1 " +
                        "       WHEN '2' THEN 2 " +
                        "       WHEN '3' THEN 3 " +
                        "       WHEN 'classic fighter' THEN 0 " +
                        "       WHEN 'girl fighter' THEN 1 " +
                        "       WHEN 'ninja' THEN 2 " +
                        "       WHEN 'panda' THEN 3 " +
                        "       ELSE 0 " +
                        "   END AS character_type " +

                        "   FROM ( " +

                        "       SELECT player_id, character_value, " +
                        "       ROW_NUMBER() OVER ( " +
                        "           PARTITION BY player_id " +
                        "           ORDER BY match_id DESC " +
                        "       ) AS rn " +

                        "       FROM ( " +

                        "           SELECT player1_id AS player_id, " +
                        "           player1_character AS character_value, " +
                        "           match_id " +
                        "           FROM match_history " +

                        "           UNION ALL " +

                        "           SELECT player2_id AS player_id, " +
                        "           player2_character AS character_value, " +
                        "           match_id " +
                        "           FROM match_history " +

                        "       ) AS appearances " +

                        "   ) AS ranked_matches " +

                        "   WHERE rn = 1 " +

                        ") AS latest " +

                        "ON p.player_id = latest.player_id " +

                        "ORDER BY p.total_points DESC, p.total_wins DESC";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Object[] player = {

                        rs.getString("username"),

                        rs.getInt("total_wins"),

                        rs.getInt("total_matches"),

                        rs.getInt("total_points"),

                        rs.getInt("character_type")

                };

                leaderboard.add(player);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading leaderboard: " + e.getMessage()
            );

            e.printStackTrace();
        }

        return leaderboard;
    }
}
