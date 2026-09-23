package game.ui;

import game.core.GameManager;
import game.objects.NPC;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/** Renders an NPC dialogue box over the paused game world. */
public class DialogueBox {

    public void render(Graphics2D g, GameManager gm) {
        NPC npc = gm.getActiveDialogueNpc();
        if (npc == null) {
            return;
        }

        int boxX = 40;
        int boxY = Constants.SCREEN_HEIGHT - Constants.HUD_HEIGHT - 140;
        int boxWidth = Constants.SCREEN_WIDTH - 80;
        int boxHeight = 120;
        UiUtils.drawPanel(g, boxX, boxY, boxWidth, boxHeight);

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(Constants.COLOR_ACCENT);
        g.drawString(npc.getDisplayName(), boxX + 20, boxY + 30);

        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.setColor(Constants.COLOR_TEXT);
        List<String> lines = UiUtils.wrapText(g, npc.getCurrentLine(), boxWidth - 40);
        int ly = boxY + 58;
        for (String line : lines) {
            g.drawString(line, boxX + 20, ly);
            ly += 22;
        }

        g.setFont(new Font("SansSerif", Font.ITALIC, 12));
        g.setColor(Constants.COLOR_ACCENT);
        String prompt = npc.hasMoreLines() ? "Press ENTER to continue..." : "Press ENTER to close";
        g.drawString(prompt, boxX + boxWidth - 200, boxY + boxHeight - 14);
    }
}
