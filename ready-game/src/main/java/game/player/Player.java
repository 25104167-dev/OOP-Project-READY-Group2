package game.player;

import game.objects.Character;
import game.objects.Damageable;
import game.utils.Constants;
import game.utils.InputHandler;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * The player-controlled character. Extends {@link Character} (inheritance)
 * and implements {@link Damageable} so hazards can call
 * {@code takeDamage(...)} on it polymorphically without knowing it's
 * specifically a Player.
 */
public class Player extends Character implements Damageable {

    private final PlayerStats stats = new PlayerStats();
    private final Inventory inventory = new Inventory();
    private double invulnerabilityTimer = 0;
    private String lastDamageReason = null;

    public Player(int x, int y) {
        super(x, y, Constants.PLAYER_SIZE, Constants.PLAYER_SIZE, Constants.COLOR_PLAYER, "Student");
    }

    public PlayerStats getStats() {
        return stats;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isInvulnerable() {
        return invulnerabilityTimer > 0;
    }

    /** Reads input and moves the player, respecting the given solid obstacles for collision. */
    public void handleMovement(InputHandler input, Rectangle[] solidBounds) {
        int dx = 0;
        int dy = 0;
        if (input.isMovingUp()) {
            dy -= Constants.PLAYER_SPEED;
            setFacing(Direction.UP);
        }
        if (input.isMovingDown()) {
            dy += Constants.PLAYER_SPEED;
            setFacing(Direction.DOWN);
        }
        if (input.isMovingLeft()) {
            dx -= Constants.PLAYER_SPEED;
            setFacing(Direction.LEFT);
        }
        if (input.isMovingRight()) {
            dx += Constants.PLAYER_SPEED;
            setFacing(Direction.RIGHT);
        }

        tryMove(dx, 0, solidBounds);
        tryMove(0, dy, solidBounds);
    }

    private void tryMove(int dx, int dy, Rectangle[] solidBounds) {
        if (dx == 0 && dy == 0) {
            return;
        }
        int newX = getX() + dx;
        int newY = getY() + dy;
        newX = Math.max(0, Math.min(newX, Constants.SCREEN_WIDTH - getWidth()));
        newY = Math.max(0, Math.min(newY, Constants.SCREEN_HEIGHT - Constants.HUD_HEIGHT - getHeight()));

        Rectangle proposed = new Rectangle(newX, newY, getWidth(), getHeight());
        for (Rectangle solid : solidBounds) {
            if (proposed.intersects(solid)) {
                return; // blocked - do not apply this movement component
            }
        }
        setPosition(newX, newY);
    }

    @Override
    public void update(double deltaSeconds) {
        if (invulnerabilityTimer > 0) {
            invulnerabilityTimer -= deltaSeconds;
        }
    }

    @Override
    public void takeDamage(int amount, String reason) {
        if (isInvulnerable()) {
            return;
        }
        stats.getLifeSystem().loseLife();
        invulnerabilityTimer = Constants.INVULNERABILITY_SECONDS;
        lastDamageReason = reason;
    }

    public String getLastDamageReason() {
        return lastDamageReason;
    }

    @Override
    public boolean isAlive() {
        return !stats.getLifeSystem().isGameOver();
    }

    @Override
    public void render(Graphics2D g) {
        if (isInvulnerable() && (System.currentTimeMillis() / 150) % 2 == 0) {
            return; // flicker effect while invulnerable
        }
        renderPixelSprite(g);
    }
}
