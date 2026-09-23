package game.world;

/** A respawn point: which room and coordinates the player returns to after losing all lives. */
public class Checkpoint {

    private final String roomId;
    private final int x;
    private final int y;

    public Checkpoint(String roomId, int x, int y) {
        this.roomId = roomId;
        this.x = x;
        this.y = y;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
