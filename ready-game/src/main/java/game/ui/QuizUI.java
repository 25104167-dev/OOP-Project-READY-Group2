package game.ui;

import game.core.GameManager;
import game.education.Quiz;
import game.utils.Constants;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/** Renders the multiple-choice quiz overlay used by QuizStations. */
public class QuizUI {

    public void render(Graphics2D g, GameManager gm) {
        Quiz quiz = gm.getActiveQuiz();
        if (quiz == null) {
            return;
        }

        int boxX = 30;
        int boxY = 40;
        int boxWidth = Constants.SCREEN_WIDTH - 60;
        int boxHeight = Constants.SCREEN_HEIGHT - 120;
        UiUtils.drawPanel(g, boxX, boxY, boxWidth, boxHeight);

        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(Constants.COLOR_ACCENT);
        g.drawString(quiz.getQuestion().getTopic().getLabel().toUpperCase(), boxX + 20, boxY + 24);

        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.setColor(Constants.COLOR_TEXT);
        List<String> promptLines = UiUtils.wrapText(g, quiz.getQuestion().getPrompt(), boxWidth - 40);
        int y = boxY + 56;
        for (String line : promptLines) {
            g.drawString(line, boxX + 20, y);
            y += 24;
        }

        y += 12;
        List<String> options = quiz.getQuestion().getOptions();
        for (int i = 0; i < options.size(); i++) {
            boolean selected = i == gm.getSelectedQuizOption();
            boolean showResult = quiz.isAnswered();
            boolean isCorrectOption = i == quiz.getQuestion().getCorrectIndex();
            boolean isPickedOption = i == quiz.getSelectedIndex();

            if (showResult && isCorrectOption) {
                g.setColor(Constants.COLOR_DOOR_UNLOCKED);
            } else if (showResult && isPickedOption) {
                g.setColor(Constants.COLOR_HAZARD);
            } else if (selected) {
                g.setColor(Constants.COLOR_ACCENT);
            } else {
                g.setColor(Constants.COLOR_TEXT);
            }

            g.setFont(new Font("SansSerif", Font.PLAIN, 16));
            String prefix = (i + 1) + ") ";
            List<String> optLines = UiUtils.wrapText(g, prefix + options.get(i), boxWidth - 60);
            for (String line : optLines) {
                g.drawString(line, boxX + 30, y);
                y += 22;
            }
            y += 6;
        }

        if (quiz.isAnswered()) {
            y += 6;
            g.setFont(new Font("SansSerif", Font.ITALIC, 14));
            g.setColor(quiz.wasCorrect() ? Constants.COLOR_DOOR_UNLOCKED : Constants.COLOR_HAZARD);
            List<String> feedback = UiUtils.wrapText(g, quiz.getQuestion().getFeedback(quiz.wasCorrect()), boxWidth - 40);
            for (String line : feedback) {
                g.drawString(line, boxX + 20, y);
                y += 20;
            }
            g.setColor(Constants.COLOR_ACCENT);
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g.drawString("Press ENTER to continue", boxX + 20, boxY + boxHeight - 16);
        } else {
            g.setColor(Constants.COLOR_ACCENT);
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g.drawString("Use W/S or number keys to choose, ENTER to confirm", boxX + 20, boxY + boxHeight - 16);
        }
    }
}
