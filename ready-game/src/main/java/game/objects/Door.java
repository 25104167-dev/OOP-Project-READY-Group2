package game.objects;

import game.gameplay.Key;
import game.player.Player;
import game.utils.Constants;

import java.awt.Graphics2D;

/**
 * A door that leads to another room. May require a {@link Key} to unlock.
 * Demonstrates polymorphism: {@code interact()} here transitions rooms
 * rather than opening dialogue or granting an item.
 */
public class Door extends InteractiveObject {

    private final String targetRoomId;
    private final Key requiredKey; // null = always unlocked
    private boolean unlocked;

    public Door(String id, int x, int y, int width, int height, String targetRoomId, Key requiredKey) {
        super(id, x, y, width, height, requiredKey != null);
        this.targetRoomId = targetRoomId;
        this.requiredKey = requiredKey;
        this.unlocked = (requiredKey == null);
    }

    /** A door blocks movement while locked, and becomes walk-through once unlocked. */
    @Override
    public boolean isSolid() {
        return !unlocked;
    }

    public String getTargetRoomId() {
        return targetRoomId;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    /** Call each frame (or on interact) to re-check whether the player now holds the required key. */
    public void refreshLockState(Player player) {
        if (!unlocked && requiredKey != null && player.getInventory().hasKey(requiredKey)) {
            unlocked = true;
        }
    }

    @Override
    public void interact(Player player) {
        refreshLockState(player);
        // Actual room transition is orchestrated by GameManager, which checks isUnlocked().
    }

    @Override
    public int getInteractionRange() {
        return 44;
    }

    @Override
    public String getInteractionPrompt() {
        if (unlocked) {
            return "Press E to go through the door";
        }
        return "This door is locked. Complete the room's challenge to unlock it.";
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(unlocked ? Constants.COLOR_DOOR_UNLOCKED : Constants.COLOR_DOOR_LOCKED);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        g.setColor(g.getColor().darker());
        g.drawRect(getX(), getY(), getWidth() - 1, getHeight() - 1);
    }
}
