package game.education;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Owns the full question bank, organized by topic, and hands out a
 * randomized (but not-yet-used) question when a room's quiz station is
 * triggered. Encapsulates all question data behind simple accessor methods.
 */
public class QuizManager {

    private final Map<EducationalTopic, List<Question>> bank = new EnumMap<>(EducationalTopic.class);
    private final Map<EducationalTopic, List<Question>> remaining = new EnumMap<>(EducationalTopic.class);
    private final LearningProgress progress = new LearningProgress();

    public QuizManager() {
        loadQuestionBank();
        resetRemaining();
    }

    public LearningProgress getProgress() {
        return progress;
    }

    /** Returns a random not-yet-asked question for the topic, refilling if exhausted. */
    public Question drawQuestion(EducationalTopic topic) {
        List<Question> pool = remaining.get(topic);
        if (pool == null || pool.isEmpty()) {
            pool = new ArrayList<>(bank.get(topic));
            Collections.shuffle(pool);
            remaining.put(topic, pool);
        }
        return pool.remove(pool.size() - 1);
    }

    public void resetRemaining() {
        for (EducationalTopic topic : EducationalTopic.values()) {
            List<Question> pool = new ArrayList<>(bank.getOrDefault(topic, List.of()));
            Collections.shuffle(pool);
            remaining.put(topic, pool);
        }
    }

    private void add(EducationalTopic topic, Question q) {
        bank.computeIfAbsent(topic, t -> new ArrayList<>()).add(q);
    }

    private void loadQuestionBank() {
        // --- PREPAREDNESS BASICS ---
        add(EducationalTopic.PREPAREDNESS_BASICS, new Question(
                "What is the main purpose of an emergency kit?",
                List.of(
                        "To make your bag heavier",
                        "To have essential supplies ready before a disaster happens",
                        "To decorate your classroom",
                        "It has no real purpose"
                ), 1,
                "Correct! Having supplies ready ahead of time reduces panic and risk during an emergency.",
                "Not quite. An emergency kit exists so you already have essential supplies ready before disaster strikes.",
                EducationalTopic.PREPAREDNESS_BASICS));

        add(EducationalTopic.PREPAREDNESS_BASICS, new Question(
                "Why is it important to know your school's evacuation routes in advance?",
                List.of(
                        "So you can find the exit quickly and safely during an emergency",
                        "So you can avoid class",
                        "It is not important",
                        "Only teachers need to know this"
                ), 0,
                "Correct! Knowing routes in advance helps you move quickly and calmly when it matters most.",
                "Not quite. Knowing evacuation routes ahead of time helps everyone exit quickly and safely.",
                EducationalTopic.PREPAREDNESS_BASICS));

        add(EducationalTopic.PREPAREDNESS_BASICS, new Question(
                "Which of these is a good item to include in a basic emergency kit?",
                List.of(
                        "A flashlight and extra batteries",
                        "A television",
                        "A bag of candy only",
                        "Nothing is needed"
                ), 0,
                "Correct! A flashlight helps you see and signal for help if the power goes out.",
                "Not quite. A flashlight with extra batteries is a core emergency-kit item.",
                EducationalTopic.PREPAREDNESS_BASICS));

        // --- EARTHQUAKE SAFETY ---
        add(EducationalTopic.EARTHQUAKE_SAFETY, new Question(
                "During an earthquake, which action is safest?",
                List.of(
                        "Stand beside a window",
                        "Hide under a sturdy desk and protect yourself",
                        "Run outside immediately while the shaking is happening",
                        "Stand beside a tall bookshelf"
                ), 1,
                "Correct! Drop, Cover, and Hold On under a sturdy desk protects you from falling objects.",
                "Not quite. Windows, bookshelves, and running outside during shaking all expose you to falling or shattering hazards. Get under something sturdy instead.",
                EducationalTopic.EARTHQUAKE_SAFETY));

        add(EducationalTopic.EARTHQUAKE_SAFETY, new Question(
                "What should you do immediately after the shaking stops?",
                List.of(
                        "Use the elevator to leave quickly",
                        "Calmly check for hazards, then evacuate using stairs if instructed",
                        "Light a candle to see better",
                        "Ignore any alarms"
                ), 1,
                "Correct! Elevators can fail after an earthquake, so use stairs and follow instructions calmly.",
                "Not quite. Avoid elevators and open flames after an earthquake; calmly check for hazards and use the stairs.",
                EducationalTopic.EARTHQUAKE_SAFETY));

        add(EducationalTopic.EARTHQUAKE_SAFETY, new Question(
                "Which object is a hazard to avoid taking cover near during an earthquake?",
                List.of(
                        "A sturdy table",
                        "An interior wall corner",
                        "A tall unsecured cabinet that could tip over",
                        "The floor next to a low, strong desk"
                ), 2,
                "Correct! Tall, unsecured furniture can tip over and cause injury.",
                "Not quite. Tall unsecured cabinets can topple — stay clear of them and take cover under sturdy furniture instead.",
                EducationalTopic.EARTHQUAKE_SAFETY));

        // --- ENVIRONMENTAL AWARENESS ---
        add(EducationalTopic.ENVIRONMENTAL_AWARENESS, new Question(
                "How can blocked drainage make flooding worse during heavy rain?",
                List.of(
                        "It has no effect on flooding",
                        "It prevents water from flowing away, causing it to build up",
                        "It makes rain fall faster",
                        "It cools the water down"
                ), 1,
                "Correct! Clogged drains stop water from flowing away, so it accumulates and floods faster.",
                "Not quite. Blocked drains stop water from flowing away, which makes flooding worse.",
                EducationalTopic.ENVIRONMENTAL_AWARENESS));

        add(EducationalTopic.ENVIRONMENTAL_AWARENESS, new Question(
                "Which of these items is most likely to clog a drainage system?",
                List.of(
                        "Rainwater",
                        "Plastic bottles and food packaging",
                        "Sunlight",
                        "Fresh air"
                ), 1,
                "Correct! Improperly discarded plastic waste is a common cause of clogged drainage.",
                "Not quite. Plastic bottles and packaging are common causes of clogged drains.",
                EducationalTopic.ENVIRONMENTAL_AWARENESS));

        add(EducationalTopic.ENVIRONMENTAL_AWARENESS, new Question(
                "Does protecting the environment guarantee that no natural disasters will ever happen?",
                List.of(
                        "Yes, disasters would never happen again",
                        "No, but it can help reduce certain risks and make communities more resilient",
                        "Yes, but only during summer",
                        "Environmental care has no relation to disaster risk at all"
                ), 1,
                "Correct! Environmental responsibility reduces certain risks and builds resilience, though it can't prevent all natural disasters.",
                "Not quite. Natural disasters can't be fully prevented, but responsible environmental practices can reduce certain risks.",
                EducationalTopic.ENVIRONMENTAL_AWARENESS));

        // --- FIRST AID ---
        add(EducationalTopic.FIRST_AID, new Question(
                "What is the very first thing you should do before helping an injured person?",
                List.of(
                        "Make sure the surrounding area is safe",
                        "Move them immediately no matter what",
                        "Take a photo",
                        "Ignore them"
                ), 0,
                "Correct! Checking that the scene is safe protects both you and the injured person.",
                "Not quite. Always check that the area is safe before approaching or helping someone.",
                EducationalTopic.FIRST_AID));

        add(EducationalTopic.FIRST_AID, new Question(
                "If an injury looks serious, what should you do?",
                List.of(
                        "Try advanced medical procedures yourself",
                        "Seek or call for appropriate adult/medical help",
                        "Do nothing at all",
                        "Wait several hours before telling anyone"
                ), 1,
                "Correct! For serious injuries, alert an adult or emergency responders as soon as it's safe to do so.",
                "Not quite. Serious injuries need professional help — alert an adult or call emergency services.",
                EducationalTopic.FIRST_AID));

        add(EducationalTopic.FIRST_AID, new Question(
                "What is a correct basic response to a minor scrape or cut?",
                List.of(
                        "Ignore it completely",
                        "Gently clean it and cover it, telling an adult if unsure",
                        "Apply random household chemicals",
                        "Rub dirt on it"
                ), 1,
                "Correct! Gentle cleaning and covering, with an adult's help if unsure, is the appropriate basic response.",
                "Not quite. Minor wounds should be gently cleaned and covered; ask an adult if you're unsure.",
                EducationalTopic.FIRST_AID));

        // --- EVACUATION ---
        add(EducationalTopic.EVACUATION, new Question(
                "When evacuating, which route should you choose?",
                List.of(
                        "The shortest route even if it is blocked by a hazard",
                        "The route marked as the official, safer evacuation path, even if longer",
                        "Any random hallway",
                        "Whichever route your friends pick, regardless of safety"
                ), 1,
                "Correct! A longer but hazard-free official route is safer than a shortcut through danger.",
                "Not quite. Always prefer the official, clearly marked safe route, even if it takes longer.",
                EducationalTopic.EVACUATION));

        add(EducationalTopic.EVACUATION, new Question(
                "What should you do once you reach the designated safe assembly area?",
                List.of(
                        "Immediately go back inside for your belongings",
                        "Stay there, stay calm, and wait for further instructions",
                        "Run around and leave the area",
                        "Ignore staff instructions"
                ), 1,
                "Correct! Staying at the assembly point and waiting for instructions keeps everyone accounted for and safe.",
                "Not quite. You should stay at the safe assembly area and wait calmly for official instructions.",
                EducationalTopic.EVACUATION));

        add(EducationalTopic.EVACUATION, new Question(
                "If you see someone struggling during an evacuation, what is an appropriate response?",
                List.of(
                        "Ignore them completely",
                        "Help if you can do so safely, or alert an adult/responder",
                        "Push past them to move faster",
                        "Stop and panic with them"
                ), 1,
                "Correct! Helping safely or alerting an adult keeps everyone moving without putting yourself at unnecessary risk.",
                "Not quite. Help only if it's safe to do so, or alert an adult or responder.",
                EducationalTopic.EVACUATION));
    }
}
