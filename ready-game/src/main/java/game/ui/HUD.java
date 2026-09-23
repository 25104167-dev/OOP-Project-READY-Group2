package game.ui;

import game.core.GameManager;
import game.player.Player;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;

/** Draws the bottom HUD strip: lives, keys, knowledge points, and the current room objective. */
public class HUD {

    public void render(Graphics2D g, GameManager gm) {
        int hudY = Constants.SCREEN_HEIGHT - Constants.HUD_HEIGHT;
        g.setColor(Constants.COLOR_HUD_BG);
        g.fillRect(0, hudY, Constants.SCREEN_WIDTH, Constants.HUD_HEIGHT);

        Player player = gm.getPlayer();
        g.setFont(new Font("Monospaced", Font.BOLD, 16));

        // Lives as hearts
        StringBuilder hearts = new StringBuilder();
        int lives = player.getStats().getLifeSystem().getLives();
        int maxLives = player.getStats().getLifeSystem().getMaxLives();
        for (int i = 0; i < maxLives; i++) {
            hearts.append(i < lives ? "\u2764 " : "\u2661 ");
        }
        g.setColor(Constants.COLOR_HAZARD);
        g.drawString(hearts.toString(), 16, hudY + 26);

        g.setColor(Constants.COLOR_TEXT);
        g.drawString("KEYS: " + player.getInventory().getKeyCount(), 190, hudY + 26);
        g.drawString("KNOWLEDGE: " + player.getStats().getScoreSystem().getKnowledgePoints(), 300, hudY + 26);
        g.drawString("[I] Knowledge Menu", 470, hudY + 26);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(Constants.COLOR_ACCENT);
        String objective = gm.getWorld().getCurrentRoom().getObjectiveText();
        g.drawString(gm.getWorld().getCurrentRoom().getDisplayName() + ": " + objective, 16, hudY + 46);

        String toast = gm.getToastMessage();
        if (toast != null) {
            renderToast(g, toast);
        }
    }

    private void renderToast(Graphics2D g, String toast) {
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        int maxWidth = Constants.SCREEN_WIDTH - 80;
        var lines = UiUtils.wrapText(g, toast, maxWidth);
        int boxHeight = 20 + lines.size() * 18;
        int boxY = 20;
        UiUtils.drawPanel(g, 40, boxY, maxWidth + 20, boxHeight);
        g.setColor(Constants.COLOR_TEXT);
        int ly = boxY + 22;
        for (String line : lines) {
            g.drawString(line, 50, ly);
            ly += 18;
        }
    }
}
