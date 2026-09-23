package game.objects;

import game.player.Player;
import game.utils.Constants;

import java.awt.Graphics2D;

/**
 * An environmental hazard (falling debris, fire, flooded area, blocked
 * path...) that damages the player on contact rather than acting as a
 * traditional violent enemy. Optionally patrols back and forth between two
 * x-coordinates to require the player to time their movement.
 */
public class Hazard extends InteractiveObject {

    private final String label;
    private final String damageExplanation;
    private final int damageAmount;

    // optional simple patrol behavior
    private final boolean moving;
    private final int patrolMinX;
    private final int patrolMaxX;
    private int patrolDirection = 1;
    private final int patrolSpeed = 1;

    public Hazard(String id, int x, int y, int width, int height, String label,
                   String damageExplanation, int damageAmount) {
        this(id, x, y, width, height, label, damageExplanation, damageAmount, false, x, x);
    }

    public Hazard(String id, int x, int y, int width, int height, String label,
                   String damageExplanation, int damageAmount,
                   boolean moving, int patrolMinX, int patrolMaxX) {
        super(id, x, y, width, height, false);
        this.label = label;
        this.damageExplanation = damageExplanation;
        this.damageAmount = damageAmount;
        this.moving = moving;
        this.patrolMinX = patrolMinX;
        this.patrolMaxX = patrolMaxX;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void update(double deltaSeconds) {
        if (!moving) {
            return;
        }
        int newX = getX() + patrolDirection * patrolSpeed;
        if (newX < patrolMinX || newX > patrolMaxX) {
            patrolDirection *= -1;
            newX = getX() + patrolDirection * patrolSpeed;
        }
        setPosition(newX, getY());
    }

    /** Called by the game loop when the player's bounds intersect this hazard. */
    public void applyTo(Player player) {
        player.takeDamage(damageAmount, damageExplanation);
    }

    @Override
    public void interact(Player player) {
        // Hazards damage on touch (handled in GameManager collision check),
        // not via the interact key, but must implement Interactable to be
        // treated uniformly by code that lists nearby interactables.
    }

    @Override
    public String getInteractionPrompt() {
        return "Hazard: " + label + " - avoid contact!";
    }

    @Override
    public int getInteractionRange() {
        return 0; // hazards aren't triggered by the E key
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Constants.COLOR_HAZARD);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        g.setColor(java.awt.Color.YELLOW);
        g.drawRect(getX(), getY(), getWidth() - 1, getHeight() - 1);
        // small hazard stripe pattern for readability without relying on color alone
        g.setColor(java.awt.Color.BLACK);
        for (int i = 0; i < getWidth(); i += 6) {
            g.drawLine(getX() + i, getY() + getHeight(), getX() + i + 3, getY());
        }
    }
}
