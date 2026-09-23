package game.gameplay;

/** Tracks the player's "Knowledge Points" score. */
public class ScoreSystem {

    private int knowledgePoints = 0;

    public void addPoints(int amount) {
        if (amount > 0) {
            knowledgePoints += amount;
        }
    }

    public int getKnowledgePoints() {
        return knowledgePoints;
    }
}
