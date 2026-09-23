package game.objects;

import game.education.InformationCard;
import game.player.Player;
import game.utils.Constants;

import java.awt.Graphics2D;

/**
 * A collectible educational item (pamphlet, poster, first-aid card...).
 * When collected, adds its {@link InformationCard} to the player's
 * inventory and marks itself as consumed so it disappears from the room.
 */
public class Collectible extends InteractiveObject {

    private final InformationCard card;
    private boolean collected = false;

    public Collectible(String id, int x, int y, InformationCard card) {
        super(id, x, y, 18, 18, false);
        this.card = card;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public void interact(Player player) {
        if (!collected) {
            player.getInventory().addInformationCard(card);
            collected = true;
        }
    }

    @Override
    public String getInteractionPrompt() {
        return "Press E to pick up: " + card.getTitle();
    }

    @Override
    public void render(Graphics2D g) {
        if (collected) {
            return;
        }
        g.setColor(Constants.COLOR_COLLECTIBLE);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
        g.setColor(g.getColor().darker());
        g.drawOval(getX(), getY(), getWidth() - 1, getHeight() - 1);
    }
}
