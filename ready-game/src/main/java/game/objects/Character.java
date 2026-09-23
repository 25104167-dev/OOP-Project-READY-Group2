package game.objects;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Common base for any living/moving entity (the Player and NPCs).
 * Demonstrates inheritance: both {@link game.player.Player} and {@link NPC}
 * extend this class and inherit facing-direction bookkeeping and the basic
 * pixel-art "sprite" rendering, while each overrides {@code render()} /
 * behavior where they need something different (polymorphism).
 */
public abstract class Character extends GameObject {

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private Direction facing = Direction.DOWN;
    private final Color bodyColor;
    private String displayName;

    protected Character(int x, int y, int width, int height, Color bodyColor, String displayName) {
        super(x, y, width, height, true);
        this.bodyColor = bodyColor;
        this.displayName = displayName;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public String getDisplayName() {
        return displayName;
    }

    protected void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    protected Color getBodyColor() {
        return bodyColor;
    }

    /**
     * Simple shared "pixel-art" style renderer: a colored body block plus a
     * small darker "face" indicator on the side the character is facing.
     * Subclasses may call this and then layer extra detail on top.
     */
    protected void renderPixelSprite(Graphics2D g) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        g.setColor(bodyColor);
        g.fillRect(x, y, w, h);

        g.setColor(bodyColor.darker());
        g.fillRect(x, y, w, 4); // "hair"/top band

        // facing indicator (little darker square = eyes looking direction)
        g.setColor(Color.BLACK);
        int eyeSize = Math.max(3, w / 8);
        switch (facing) {
            case UP -> g.fillRect(x + w / 2 - eyeSize / 2, y + 4, eyeSize, eyeSize);
            case DOWN -> g.fillRect(x + w / 2 - eyeSize / 2, y + h - 8, eyeSize, eyeSize);
            case LEFT -> g.fillRect(x + 2, y + h / 2 - eyeSize / 2, eyeSize, eyeSize);
            case RIGHT -> g.fillRect(x + w - 2 - eyeSize, y + h / 2 - eyeSize / 2, eyeSize, eyeSize);
        }
    }
}
