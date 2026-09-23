package game.education;

import java.util.List;

/**
 * A single multiple-choice question. Immutable data holder — encapsulation
 * is provided by exposing only read access; nothing outside this class can
 * mutate a question once created.
 */
public class Question {

    private final String prompt;
    private final List<String> options; // e.g. ["Stand beside a window", ...]
    private final int correctIndex;
    private final String correctFeedback;
    private final String incorrectFeedback;
    private final EducationalTopic topic;

    public Question(String prompt, List<String> options, int correctIndex,
                     String correctFeedback, String incorrectFeedback, EducationalTopic topic) {
        if (correctIndex < 0 || correctIndex >= options.size()) {
            throw new IllegalArgumentException("correctIndex out of range for question: " + prompt);
        }
        this.prompt = prompt;
        this.options = List.copyOf(options);
        this.correctIndex = correctIndex;
        this.correctFeedback = correctFeedback;
        this.incorrectFeedback = incorrectFeedback;
        this.topic = topic;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctIndex;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getFeedback(boolean wasCorrect) {
        return wasCorrect ? correctFeedback : incorrectFeedback;
    }

    public EducationalTopic getTopic() {
        return topic;
    }
}
