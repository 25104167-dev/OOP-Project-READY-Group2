package game.ui;

import game.core.GameManager;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;

/** Renders the instructions/how-to-play screen. */
public class InstructionsScreen {

    private static final String[] LINES = {
            "MOVEMENT: W A S D or Arrow Keys",
            "INTERACT: E  (talk, open doors, pick up items, start challenges)",
            "PAUSE: ESC        CONFIRM / SELECT: ENTER",
            "KNOWLEDGE MENU: I  (review collected information cards)",
            "",
            "OBJECTIVE: Explore the school, learn important safety",
            "information, complete each room's challenge, and safely",
            "reach the evacuation assembly area.",
            "",
            "Your decisions affect your lives - choose safe actions",
            "and answer questions carefully!",
    };

    public void render(Graphics2D g, GameManager gm) {
        g.setColor(Constants.COLOR_BG);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        g.setColor(Constants.COLOR_ACCENT);
        g.setFont(new Font("Monospaced", Font.BOLD, 28));
        g.drawString("How to Play", 40, 60);

        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        int y = 110;
        for (String line : LINES) {
            g.drawString(line, 40, y);
            y += 26;
        }

        g.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g.setColor(Constants.COLOR_ACCENT);
        g.drawString("Press ENTER or ESC to return to the menu", 40, Constants.SCREEN_HEIGHT - 40);
    }
}
