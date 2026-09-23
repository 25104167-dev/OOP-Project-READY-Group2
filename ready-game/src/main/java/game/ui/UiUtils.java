package game.ui;

import game.utils.Constants;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/** Small shared helpers used by several UI screens (panels, word-wrapped text). */
final class UiUtils {

    private UiUtils() {
    }

    static void drawPanel(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(Constants.COLOR_PANEL);
        g.fillRoundRect(x, y, width, height, 16, 16);
        g.setColor(Constants.COLOR_ACCENT);
        g.drawRoundRect(x, y, width, height, 16, 16);
    }

    /** Wraps text to fit within maxWidth pixels using the graphics context's current font. */
    static List<String> wrapText(Graphics2D g, String text, int maxWidth) {
        FontMetrics fm = g.getFontMetrics();
        List<String> lines = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String word : text.split(" ")) {
            String candidate = current.isEmpty() ? word : current + " " + word;
            if (fm.stringWidth(candidate) > maxWidth && !current.isEmpty()) {
                lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(candidate);
            }
        }
        if (!current.isEmpty()) {
            lines.add(current.toString());
        }
        return lines;
    }
}
