package game.objects;

import game.education.EducationalTopic;
import game.player.Player;
import game.utils.Constants;

import java.awt.Graphics2D;

/**
 * A station the player interacts with to trigger a quiz question on a
 * specific topic. The actual question selection and quiz flow is handled
 * by the GameManager/QuizManager; this class just marks a location in the
 * room and remembers whether its challenge has been completed.
 */
public class QuizStation extends InteractiveObject {

    private final EducationalTopic topic;
    private boolean completed = false;

    public QuizStation(String id, int x, int y, EducationalTopic topic) {
        super(id, x, y, 28, 28, false);
        this.topic = topic;
    }

    public EducationalTopic getTopic() {
        return topic;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }

    @Override
    public void interact(Player player) {
        // Triggering the quiz UI itself is handled by GameManager, which
        // checks isCompleted() first and then calls markCompleted() once
        // the player answers (correctly or not, the attempt counts).
    }

    @Override
    public String getInteractionPrompt() {
        return completed ? "Challenge already completed" : "Press E to start challenge: " + topic.getLabel();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(completed ? Constants.COLOR_DOOR_UNLOCKED : Constants.COLOR_QUIZ_STATION);
        g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
        g.setColor(java.awt.Color.WHITE);
        g.drawString("?", getX() + getWidth() / 2 - 3, getY() + getHeight() / 2 + 5);
    }
}
