package game.gameplay;

/**
 * A key/access-card/badge earned by completing a room's challenge, used to
 * unlock the door to the next room. Simple immutable value object.
 */
public class Key {

    private final String id;
    private final String displayName;

    public Key(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Key other && other.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
