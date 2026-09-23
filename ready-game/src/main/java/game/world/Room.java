package game.world;

import game.objects.GameObject;
import game.objects.Interactable;
import game.player.Player;
import game.utils.Constants;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * A single room/area of the school. Holds a simple tile grid (for walls and
 * floor) plus a list of {@link GameObject}s (NPCs, doors, hazards,
 * collectibles, quiz stations) that live in it.
 */
public class Room {

    private final String id;
    private final String displayName;
    private final Tile[][] grid; // [row][col]
    private final List<GameObject> objects = new ArrayList<>();
    private final int playerEntryX;
    private final int playerEntryY;
    private final String objectiveText;

    public Room(String id, String displayName, Tile[][] grid, int playerEntryX, int playerEntryY, String objectiveText) {
        this.id = id;
        this.displayName = displayName;
        this.grid = grid;
        this.playerEntryX = playerEntryX;
        this.playerEntryY = playerEntryY;
        this.objectiveText = objectiveText;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPlayerEntryX() {
        return playerEntryX;
    }

    public int getPlayerEntryY() {
        return playerEntryY;
    }

    public String getObjectiveText() {
        return objectiveText;
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public void update(double deltaSeconds) {
        for (GameObject obj : objects) {
            obj.update(deltaSeconds);
        }
    }

    public void render(Graphics2D g) {
        renderTiles(g);
        for (GameObject obj : objects) {
            obj.render(g);
        }
    }

    private void renderTiles(Graphics2D g) {
        int ts = Constants.TILE_SIZE;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Tile tile = grid[row][col];
                g.setColor(tile == Tile.WALL ? Constants.COLOR_WALL : Constants.COLOR_FLOOR);
                g.fillRect(col * ts, row * ts, ts, ts);
                g.setColor(Constants.COLOR_BG);
                g.drawRect(col * ts, row * ts, ts, ts); // subtle grid lines for a tiled look
            }
        }
    }

    /** All solid bounds in this room: wall tiles plus any solid game objects. */
    public Rectangle[] getSolidBounds() {
        List<Rectangle> bounds = new ArrayList<>();
        int ts = Constants.TILE_SIZE;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == Tile.WALL) {
                    bounds.add(new Rectangle(col * ts, row * ts, ts, ts));
                }
            }
        }
        for (GameObject obj : objects) {
            if (obj.isSolid()) {
                bounds.add(obj.getBounds());
            }
        }
        return bounds.toArray(new Rectangle[0]);
    }

    /** Finds the closest Interactable within range of the player, if any. */
    public Interactable findNearestInteractable(Player player) {
        Interactable nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (GameObject obj : objects) {
            if (obj instanceof Interactable interactable) {
                double dist = obj.distanceTo(player);
                if (dist <= interactable.getInteractionRange() && dist < nearestDist) {
                    nearest = interactable;
                    nearestDist = dist;
                }
            }
        }
        return nearest;
    }
}
