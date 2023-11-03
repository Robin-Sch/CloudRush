package components;

/**
 * An entry in the leaderboard.
 */
public class LeaderboardEntry {
    private final String level; // Levels are strings like "eindhoven", "warsaw" and "newyork"
    private final String score;
    private final String nickname;

    /**
     * Initialize LeaderboardEntry.
     * @param level The level of the entry
     * @param score The score reached
     * @param nickname The nickname of the player
     */
    public LeaderboardEntry(String level, String score, String nickname) {
        this.level = level;
        this.score = score;
        this.nickname = nickname;
    }

    /**
     * Get the entry's level.
     */
    public String getLevel() {
        return this.level;
    }

    /**
     * Get the entry's score.
     */
    public String getScore() {
        return this.score;
    }

    /**
     * Get the entry's nickname.
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Correct formatting of the entry.
     */
    @Override
    public String toString() {
        return this.level + "," + this.score + "," + this.nickname;
    }
}