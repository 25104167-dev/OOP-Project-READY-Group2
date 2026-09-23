package game.education;

/** The learning topics the game teaches, one per major room. */
public enum EducationalTopic {
    PREPAREDNESS_BASICS("Emergency Preparedness"),
    EARTHQUAKE_SAFETY("Earthquake Safety"),
    ENVIRONMENTAL_AWARENESS("Environmental Awareness"),
    FIRST_AID("Basic First Aid"),
    EVACUATION("Evacuation & Emergency Decision-Making");

    private final String label;

    EducationalTopic(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
