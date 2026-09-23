package game.world;

/** The type of a single grid cell in a room's tile map. */
public enum Tile {
    FLOOR(false),
    WALL(true);

    private final boolean solid;

    Tile(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }
}
