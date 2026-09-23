package game.ui;

import game.core.GameManager;
import game.education.LearningProgress;
import game.player.Player;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;

/** Renders the Victory / completion summary screen. */
public class VictoryScreen {

    public void render(Graphics2D g, GameManager gm) {
        g.setColor(Constants.COLOR_BG);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        g.setColor(Constants.COLOR_DOOR_UNLOCKED);
        g.setFont(new Font("Monospaced", Font.BOLD, 34));
        centerString(g, "YOU'RE READY!", Constants.SCREEN_HEIGHT / 2 - 160);

        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("SansSerif", Font.PLAIN, 15));
        centerString(g, "You safely navigated the school and reached the assembly area.",
                Constants.SCREEN_HEIGHT / 2 - 120);

        Player player = gm.getPlayer();
        LearningProgress progress = gm.getQuizManager().getProgress();

        String[] stats = {
                "Knowledge Points: " + player.getStats().getScoreSystem().getKnowledgePoints(),
                "Collectibles Found: " + player.getInventory().getCollectibleCount(),
                "Questions Answered Correctly: " + progress.getQuestionsCorrect() + " / " + progress.getQuestionsAnswered()
                        + " (" + progress.getAccuracyPercent() + "%)",
                "Topics Completed: " + progress.getTopicsCompletedCount() + " / "
                        + game.education.EducationalTopic.values().length,
                "Lives Remaining: " + player.getStats().getLifeSystem().getLives()
                        + " / " + player.getStats().getLifeSystem().getMaxLives(),
        };

        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        int y = Constants.SCREEN_HEIGHT / 2 - 70;
        for (String line : stats) {
            centerString(g, line, y);
            y += 26;
        }

        g.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g.setColor(Constants.COLOR_ACCENT);
        centerString(g, "You've practiced real disaster-preparedness skills - well done!",
                Constants.SCREEN_HEIGHT / 2 + 90);
        centerString(g, "Press ENTER to return to the main menu", Constants.SCREEN_HEIGHT / 2 + 120);
    }

    private void centerString(Graphics2D g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (Constants.SCREEN_WIDTH - width) / 2, y);
    }
}
