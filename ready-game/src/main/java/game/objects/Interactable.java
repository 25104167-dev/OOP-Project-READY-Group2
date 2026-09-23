package game.objects;

import game.player.Player;

/**
 * Implemented by any object the player can interact with by pressing E.
 * This is the interface that makes {@link game.core.GameManager} able to
 * call {@code interact()} polymorphically without knowing the concrete
 * type of object (Door, NPC, Collectible, QuizStation, ...).
 */
public interface Interactable {

    /** Called when the player presses the interact key while in range. */
    void interact(Player player);

    /** Short text shown to the player as a prompt, e.g. "Press E to open door". */
    String getInteractionPrompt();

    /** Maximum distance (in pixels) at which interaction is allowed. */
    default int getInteractionRange() {
        return 40;
    }
}
