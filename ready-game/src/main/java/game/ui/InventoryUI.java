package game.ui;

import game.core.GameManager;
import game.education.InformationCard;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/** Renders the Knowledge Menu: all InformationCards the player has collected. */
public class InventoryUI {

    public void render(Graphics2D g, GameManager gm) {
        int boxX = 30;
        int boxY = 30;
        int boxWidth = Constants.SCREEN_WIDTH - 60;
        int boxHeight = Constants.SCREEN_HEIGHT - 60;
        UiUtils.drawPanel(g, boxX, boxY, boxWidth, boxHeight);

        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.setColor(Constants.COLOR_ACCENT);
        g.drawString("Knowledge Menu", boxX + 20, boxY + 30);

        List<InformationCard> cards = gm.getPlayer().getInventory().getInformationCards();
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.setColor(Constants.COLOR_TEXT);

        if (cards.isEmpty()) {
            g.drawString("You haven't collected any information cards yet.", boxX + 20, boxY + 70);
        } else {
            int y = boxY + 60;
            for (InformationCard card : cards) {
                g.setFont(new Font("SansSerif", Font.BOLD, 15));
                g.setColor(Constants.COLOR_ACCENT);
                g.drawString("* " + card.getTitle() + " (" + card.getTopic().getLabel() + ")", boxX + 20, y);
                y += 20;

                g.setFont(new Font("SansSerif", Font.PLAIN, 13));
                g.setColor(Constants.COLOR_TEXT);
                for (String line : UiUtils.wrapText(g, card.getExplanation(), boxWidth - 60)) {
                    g.drawString(line, boxX + 34, y);
                    y += 18;
                }
                y += 10;
            }
        }

        g.setFont(new Font("SansSerif", Font.ITALIC, 12));
        g.setColor(Constants.COLOR_ACCENT);
        g.drawString("Press I or ESC to close", boxX + 20, boxY + boxHeight - 14);
    }
}
