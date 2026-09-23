package game.education;

/**
 * A short piece of collectible educational content (a pamphlet, poster,
 * first-aid card, etc.) that gets added to the player's Knowledge Menu
 * when picked up in the world.
 */
public class InformationCard {

    private final String title;
    private final String explanation;
    private final EducationalTopic topic;

    public InformationCard(String title, String explanation, EducationalTopic topic) {
        this.title = title;
        this.explanation = explanation;
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public EducationalTopic getTopic() {
        return topic;
    }
}
