package game.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Abstract base class for anything that exists in the game world and can be
 * drawn / collided with (players, NPCs, doors, hazards, collectibles...).
 *
 * <p>This is the root of the game's inheritance hierarchy:</p>
 * <pre>
 * GameObject
 * ├── Character (player.Player, objects.NPC)
 * └── InteractiveObject
 *     ├── Door
 *     ├── Collectible
 *     ├── Hazard
 *     └── QuizStation
 * </pre>
 *
 * <p>Encapsulation: position/size fields are private with controlled
 * getters/setters. Abstraction: subclasses must define what "update" and
 * "render" mean for them; callers only ever depend on this common type.</p>
 */
public abstract class GameObject {

    private int x;
    private int y;
    private final int width;
    private final int height;
    private boolean solid; // true = blocks movement (collision)

    protected GameObject(int x, int y, int width, int height, boolean solid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solid = solid;
    }

    /** Per-frame logic (animation, movement, timers). Default: no-op. */
    public void update(double deltaSeconds) {
        // most static objects don't need per-frame updates
    }

    /** Draw this object using the given graphics context. */
    public abstract void render(Graphics2D g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSolid() {
        return solid;
    }

    protected void setSolid(boolean solid) {
        this.solid = solid;
    }

    /** Axis-aligned bounding box used for collision and interaction range checks. */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }

    /** Straight-line distance in pixels between the centers of two objects. */
    public double distanceTo(GameObject other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
