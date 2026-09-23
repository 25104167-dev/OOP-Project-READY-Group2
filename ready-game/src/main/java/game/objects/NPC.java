package game.objects;

import game.player.Player;
import game.utils.Constants;

import java.awt.Graphics2D;
import java.util.List;

/**
 * A non-player character that shows a short line of dialogue when the
 * player interacts with it. Extends {@link Character} and implements
 * {@link Interactable} (polymorphism: its interact() opens dialogue,
 * unlike a Door's interact() which unlocks).
 */
public class NPC extends Character implements Interactable {

    private final List<String> dialogueLines;
    private int lineIndex = 0;

    public NPC(int x, int y, String displayName, List<String> dialogueLines) {
        super(x, y, 26, 26, Constants.COLOR_NPC, displayName);
        this.dialogueLines = dialogueLines;
    }

    @Override
    public void interact(Player player) {
        // Dialogue advance is handled by the caller (GameManager) which
        // reads getCurrentLine() and calls advanceDialogue(); this method
        // exists so NPC still satisfies Interactable for polymorphic use.
    }

    public String getCurrentLine() {
        return dialogueLines.get(Math.min(lineIndex, dialogueLines.size() - 1));
    }

    public boolean hasMoreLines() {
        return lineIndex < dialogueLines.size() - 1;
    }

    public void advanceDialogue() {
        if (hasMoreLines()) {
            lineIndex++;
        }
    }

    public void resetDialogue() {
        lineIndex = 0;
    }

    @Override
    public String getInteractionPrompt() {
        return "Press E to talk to " + getDisplayName();
    }

    @Override
    public void render(Graphics2D g) {
        renderPixelSprite(g);
    }
}
