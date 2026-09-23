package game.education;

/**
 * A single active quiz "session" wrapping one {@link Question}. Kept
 * separate from Question itself so the QuizUI has somewhere to store
 * transient state (whether it's been answered yet, which option was picked)
 * without mutating the immutable Question data.
 */
public class Quiz {

    private final Question question;
    private boolean answered = false;
    private int selectedIndex = -1;

    public Quiz(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void submitAnswer(int index) {
        this.selectedIndex = index;
        this.answered = true;
    }

    public boolean isAnswered() {
        return answered;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public boolean wasCorrect() {
        return answered && question.isCorrect(selectedIndex);
    }
}
