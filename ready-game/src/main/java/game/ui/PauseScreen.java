package game.ui;

import game.core.GameManager;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;

/** Renders the pause menu overlay. */
public class PauseScreen {

    public void render(Graphics2D g, GameManager gm) {
        int boxWidth = 280;
        int boxHeight = 160;
        int boxX = (Constants.SCREEN_WIDTH - boxWidth) / 2;
        int boxY = (Constants.SCREEN_HEIGHT - boxHeight) / 2;
        UiUtils.drawPanel(g, boxX, boxY, boxWidth, boxHeight);

        g.setFont(new Font("Monospaced", Font.BOLD, 22));
        g.setColor(Constants.COLOR_ACCENT);
        g.drawString("Paused", boxX + 90, boxY + 40);

        String[] options = gm.getPauseMenuOptions();
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        int y = boxY + 80;
        for (int i = 0; i < options.length; i++) {
            boolean selected = i == gm.getPauseMenuIndex();
            g.setColor(selected ? Constants.COLOR_ACCENT : Constants.COLOR_TEXT);
            g.drawString((selected ? "> " : "  ") + options[i], boxX + 70, y);
            y += 32;
        }
    }
}
