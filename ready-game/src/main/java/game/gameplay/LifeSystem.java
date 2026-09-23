package game.gameplay;

import game.utils.Constants;

/**
 * Tracks the player's remaining lives. Encapsulated so nothing outside this
 * class can set lives to an invalid value directly.
 */
public class LifeSystem {

    private int lives;
    private final int maxLives;

    public LifeSystem() {
        this(Constants.STARTING_LIVES);
    }

    public LifeSystem(int startingLives) {
        this.maxLives = startingLives;
        this.lives = startingLives;
    }

    /** Returns true if this loss caused a game over (lives reached zero). */
    public boolean loseLife() {
        if (lives > 0) {
            lives--;
        }
        return lives <= 0;
    }

    public void resetLives() {
        lives = maxLives;
    }

    public int getLives() {
        return lives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }
}
