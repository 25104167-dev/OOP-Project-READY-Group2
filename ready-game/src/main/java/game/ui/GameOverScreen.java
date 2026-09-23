package game.ui;

import game.core.GameManager;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/** Renders the Game Over screen (shown when lives reach zero; respawns at checkpoint on confirm). */
public class GameOverScreen {

    public void render(Graphics2D g, GameManager gm) {
        g.setColor(Constants.COLOR_BG);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        g.setColor(Constants.COLOR_HAZARD);
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        centerString(g, "GAME OVER", Constants.SCREEN_HEIGHT / 2 - 90);

        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("SansSerif", Font.PLAIN, 15));
        String reason = gm.getPlayer().getLastDamageReason();
        if (reason != null) {
            List<String> lines = UiUtils.wrapText(g, "What went wrong: " + reason, Constants.SCREEN_WIDTH - 160);
            int y = Constants.SCREEN_HEIGHT / 2 - 30;
            for (String line : lines) {
                centerString(g, line, y);
                y += 22;
            }
        }

        g.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g.setColor(Constants.COLOR_ACCENT);
        centerString(g, "Press ENTER to try again from your last checkpoint", Constants.SCREEN_HEIGHT / 2 + 60);
        centerString(g, "Press ESC to return to the main menu", Constants.SCREEN_HEIGHT / 2 + 90);
    }

    private void centerString(Graphics2D g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (Constants.SCREEN_WIDTH - width) / 2, y);
    }
}
