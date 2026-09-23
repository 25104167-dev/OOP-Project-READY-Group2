package game.objects;

/**
 * Common base for scenery the player interacts with rather than a living
 * character: doors, collectibles, hazards, quiz stations. Concrete
 * subclasses implement {@link Interactable#interact(game.player.Player)}
 * differently (polymorphism) even though the game manager treats them all
 * uniformly as {@code Interactable}.
 */
public abstract class InteractiveObject extends GameObject implements Interactable {

    private final String id;

    protected InteractiveObject(String id, int x, int y, int width, int height, boolean solid) {
        super(x, y, width, height, solid);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
