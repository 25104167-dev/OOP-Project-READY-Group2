package game.education;

import java.util.EnumSet;
import java.util.Set;

/**
 * Tracks which educational topics the player has completed and basic quiz
 * statistics, used for the end-of-game summary screen.
 */
public class LearningProgress {

    private final Set<EducationalTopic> completedTopics = EnumSet.noneOf(EducationalTopic.class);
    private int questionsAnswered = 0;
    private int questionsCorrect = 0;

    public void markTopicCompleted(EducationalTopic topic) {
        completedTopics.add(topic);
    }

    public boolean isTopicCompleted(EducationalTopic topic) {
        return completedTopics.contains(topic);
    }

    public int getTopicsCompletedCount() {
        return completedTopics.size();
    }

    public void recordAnswer(boolean correct) {
        questionsAnswered++;
        if (correct) {
            questionsCorrect++;
        }
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public int getQuestionsCorrect() {
        return questionsCorrect;
    }

    public int getAccuracyPercent() {
        if (questionsAnswered == 0) {
            return 0;
        }
        return Math.round(100f * questionsCorrect / questionsAnswered);
    }
}
