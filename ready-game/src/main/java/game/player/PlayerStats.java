package game.player;

import game.education.LearningProgress;
import game.gameplay.LifeSystem;
import game.gameplay.ScoreSystem;

/**
 * Bundles the player's run-time statistics (lives, score, learning
 * progress) so screens like the Game Over / Victory summary can read a
 * single object instead of threading three separate systems through.
 */
public class PlayerStats {

    private final LifeSystem lifeSystem = new LifeSystem();
    private final ScoreSystem scoreSystem = new ScoreSystem();
    private final LearningProgress learningProgress = new LearningProgress();

    public LifeSystem getLifeSystem() {
        return lifeSystem;
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public LearningProgress getLearningProgress() {
        return learningProgress;
    }
}
