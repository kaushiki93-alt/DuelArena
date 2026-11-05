package duelarena;

public class PlayerAccount {

    private final int playerId;
    private final String username;
    private final int totalWins;
    private final int totalMatches;
    private final int totalPoints;
    private final String currentCharacter;

    public PlayerAccount(int playerId, String username,
                         int totalWins, int totalMatches,
                         int totalPoints, String currentCharacter) {

        this.playerId = playerId;
        this.username = username;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.totalPoints = totalPoints;
        this.currentCharacter = currentCharacter;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalMatches() {
        return totalMatches;

    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getCurrentCharacter() {
        return currentCharacter;
    }
}