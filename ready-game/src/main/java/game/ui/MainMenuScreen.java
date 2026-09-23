package game.ui;

import game.core.GameManager;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;

/** Renders the main menu / title screen. */
public class MainMenuScreen {

    public void render(Graphics2D g, GameManager gm) {
        g.setColor(Constants.COLOR_BG);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        g.setColor(Constants.COLOR_ACCENT);
        g.setFont(new Font("Monospaced", Font.BOLD, 42));
        centerString(g, "READY", Constants.SCREEN_HEIGHT / 2 - 140);

        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("SansSerif", Font.ITALIC, 16));
        centerString(g, "Disaster Preparedness Adventure", Constants.SCREEN_HEIGHT / 2 - 100);

        String[] options = gm.getMainMenuOptions();
        g.setFont(new Font("Monospaced", Font.PLAIN, 20));
        int startY = Constants.SCREEN_HEIGHT / 2 - 20;
        for (int i = 0; i < options.length; i++) {
            boolean selected = i == gm.getMainMenuIndex();
            g.setColor(selected ? Constants.COLOR_ACCENT : Constants.COLOR_TEXT);
            String label = (selected ? "> " : "  ") + options[i];
            centerString(g, label, startY + i * 34);
        }

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(Constants.COLOR_TEXT);
        centerString(g, "W/S or Arrow Keys to navigate - ENTER to select", Constants.SCREEN_HEIGHT - 40);

        String toast = gm.getToastMessage();
        if (toast != null) {
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            var lines = UiUtils.wrapText(g, toast, Constants.SCREEN_WIDTH - 80);
            int y = Constants.SCREEN_HEIGHT - 70 - lines.size() * 16;
            for (String line : lines) {
                centerString(g, line, y);
                y += 16;
            }
        }
    }

    private void centerString(Graphics2D g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (Constants.SCREEN_WIDTH - width) / 2, y);
    }
}
