package game.core;

import game.education.Question;
import game.education.Quiz;
import game.education.QuizManager;
import game.gameplay.Keys;
import game.objects.Collectible;
import game.objects.Door;
import game.objects.Hazard;
import game.objects.Interactable;
import game.objects.NPC;
import game.objects.QuizStation;
import game.player.Player;
import game.utils.InputHandler;
import game.world.Checkpoint;
import game.world.Room;
import game.world.RoomFactory;
import game.world.World;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * The central controller of the game: owns the {@link World}, the
 * {@link Player}, the {@link QuizManager}, and the current {@link GameState}.
 * All state transitions (menu -> playing -> quiz -> ... ) happen here, and
 * every frame the active {@link game.ui} screen reads this class to know
 * what to draw.
 */
public class GameManager {

    private GameState state = GameState.MAIN_MENU;
    private GameState stateBeforePause = GameState.PLAYING;

    private final World world;
    private Player player;
    private final QuizManager quizManager = new QuizManager();

    private Checkpoint checkpoint;

    private NPC activeDialogueNpc;
    private Quiz activeQuiz;
    private QuizStation activeQuizStation;
    private int selectedQuizOption = 0;

    private int mainMenuIndex = 0;
    private static final String[] MAIN_MENU_OPTIONS = {"Start Game", "Instructions", "Credits", "Exit"};

    private int pauseMenuIndex = 0;
    private static final String[] PAUSE_MENU_OPTIONS = {"Resume", "Main Menu"};

    private String toastMessage = null;
    private double toastTimer = 0;

    // Maps each room's quiz-station-completion requirement to the key it grants.
    private final Map<String, game.gameplay.Key> roomCompletionKey = new HashMap<>();
    private final Map<String, String> roomNextId = new HashMap<>();

    public GameManager() {
        this.world = RoomFactory.buildWorld();
        setupRoomProgression();
    }

    private void setupRoomProgression() {
        roomCompletionKey.put("entrance", Keys.ENTRANCE_KEY);
        roomCompletionKey.put("classroom", Keys.CLASSROOM_KEY);
        roomCompletionKey.put("environment", Keys.ENVIRONMENT_KEY);
        roomCompletionKey.put("first_aid", Keys.FIRST_AID_KEY);
        roomCompletionKey.put("evacuation", Keys.EVACUATION_KEY);

        roomNextId.put("entrance", "classroom");
        roomNextId.put("classroom", "environment");
        roomNextId.put("environment", "first_aid");
        roomNextId.put("first_aid", "evacuation");
        roomNextId.put("evacuation", "final");
    }

    public GameState getState() {
        return state;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public QuizManager getQuizManager() {
        return quizManager;
    }

    public int getMainMenuIndex() {
        return mainMenuIndex;
    }

    public String[] getMainMenuOptions() {
        return MAIN_MENU_OPTIONS;
    }

    public int getPauseMenuIndex() {
        return pauseMenuIndex;
    }

    public String[] getPauseMenuOptions() {
        return PAUSE_MENU_OPTIONS;
    }

    public NPC getActiveDialogueNpc() {
        return activeDialogueNpc;
    }

    public Quiz getActiveQuiz() {
        return activeQuiz;
    }

    public int getSelectedQuizOption() {
        return selectedQuizOption;
    }

    public String getToastMessage() {
        return toastTimer > 0 ? toastMessage : null;
    }

    private void showToast(String message, double seconds) {
        this.toastMessage = message;
        this.toastTimer = seconds;
    }

    /** Starts a brand-new game: fresh player, fresh quiz pool, first room. */
    public void startNewGame() {
        Room first = world.getRoom("entrance");
        this.player = new Player(first.getPlayerEntryX(), first.getPlayerEntryY());
        this.checkpoint = new Checkpoint("entrance", first.getPlayerEntryX(), first.getPlayerEntryY());
        world.goToRoom("entrance");
        quizManager.resetRemaining();
        state = GameState.PLAYING;
    }

    // -----------------------------------------------------------------
    // Main update loop entry point, called once per frame by GamePanel.
    // -----------------------------------------------------------------
    public void update(InputHandler input, double deltaSeconds) {
        if (toastTimer > 0) {
            toastTimer -= deltaSeconds;
        }

        switch (state) {
            case MAIN_MENU -> updateMainMenu(input);
            case INSTRUCTIONS -> updateInstructions(input);
            case PLAYING -> updatePlaying(input, deltaSeconds);
            case DIALOGUE -> updateDialogue(input);
            case QUIZ -> updateQuiz(input);
            case INVENTORY -> updateInventory(input);
            case PAUSED -> updatePaused(input);
            case GAME_OVER -> updateGameOver(input);
            case VICTORY -> updateVictory(input);
        }
    }

    // --- MAIN_MENU ---
    private void updateMainMenu(InputHandler input) {
        if (input.isMovingUp()) {
            mainMenuIndex = (mainMenuIndex - 1 + MAIN_MENU_OPTIONS.length) % MAIN_MENU_OPTIONS.length;
        }
        // isMovingUp/Down fire every frame while held; debounce using wasJustPressed instead
        if (input.wasJustPressed(java.awt.event.KeyEvent.VK_UP) || input.wasJustPressed(java.awt.event.KeyEvent.VK_W)) {
            mainMenuIndex = (mainMenuIndex - 1 + MAIN_MENU_OPTIONS.length) % MAIN_MENU_OPTIONS.length;
        }
        if (input.wasJustPressed(java.awt.event.KeyEvent.VK_DOWN) || input.wasJustPressed(java.awt.event.KeyEvent.VK_S)) {
            mainMenuIndex = (mainMenuIndex + 1) % MAIN_MENU_OPTIONS.length;
        }
        if (input.isConfirm()) {
            switch (mainMenuIndex) {
                case 0 -> startNewGame();
                case 1 -> state = GameState.INSTRUCTIONS;
                case 2 -> showToast("READY was built to teach disaster preparedness under UN SDG 4: Quality Education.", 4);
                case 3 -> System.exit(0);
                default -> { }
            }
        }
    }

    private void updateInstructions(InputHandler input) {
        if (input.isConfirm() || input.isPause()) {
            state = GameState.MAIN_MENU;
        }
    }

    // --- PLAYING ---
    private void updatePlaying(InputHandler input, double deltaSeconds) {
        Room room = world.getCurrentRoom();
        Rectangle[] solids = room.getSolidBounds();
        player.handleMovement(input, solids);
        player.update(deltaSeconds);
        room.update(deltaSeconds);

        // refresh door lock states now that inventory may have changed
        for (var obj : room.getObjects()) {
            if (obj instanceof Door door) {
                door.refreshLockState(player);
            }
        }

        // hazard collision
        for (var obj : room.getObjects()) {
            if (obj instanceof Hazard hazard && hazard.getBounds().intersects(player.getBounds())) {
                boolean wasAlive = player.isAlive();
                hazard.applyTo(player);
                if (player.getLastDamageReason() != null) {
                    showToast(player.getLastDamageReason(), 3);
                }
                if (wasAlive && !player.isAlive()) {
                    state = GameState.GAME_OVER;
                    return;
                }
            }
        }

        if (input.isInteract()) {
            handleInteract(room);
        }

        if (input.wasJustPressed(java.awt.event.KeyEvent.VK_I)) {
            state = GameState.INVENTORY;
        }

        if (input.isPause()) {
            stateBeforePause = GameState.PLAYING;
            pauseMenuIndex = 0;
            state = GameState.PAUSED;
        }
    }

    private void handleInteract(Room room) {
        Interactable target = room.findNearestInteractable(player);
        if (target == null) {
            return;
        }
        if (target instanceof NPC npc) {
            npc.resetDialogue();
            activeDialogueNpc = npc;
            state = GameState.DIALOGUE;
        } else if (target instanceof Door door) {
            door.interact(player);
            if (door.isUnlocked()) {
                travelThroughDoor(door);
            } else {
                showToast(door.getInteractionPrompt(), 2.5);
            }
        } else if (target instanceof Collectible collectible) {
            collectible.interact(player);
        } else if (target instanceof QuizStation station) {
            if (!station.isCompleted()) {
                Question q = quizManager.drawQuestion(station.getTopic());
                activeQuiz = new Quiz(q);
                activeQuizStation = station;
                selectedQuizOption = 0;
                state = GameState.QUIZ;
            } else {
                showToast("You've already completed this challenge.", 2);
            }
        }
    }

    private void travelThroughDoor(Door door) {
        String targetId = door.getTargetRoomId();
        world.goToRoom(targetId);
        Room newRoom = world.getCurrentRoom();
        player.setPosition(newRoom.getPlayerEntryX(), newRoom.getPlayerEntryY());
        checkpoint = new Checkpoint(targetId, newRoom.getPlayerEntryX(), newRoom.getPlayerEntryY());

        if (targetId.equals("final")) {
            // entering final room is itself fine; victory triggers once all final quiz stations complete
        }
    }

    // --- DIALOGUE ---
    private void updateDialogue(InputHandler input) {
        if (input.isConfirm() || input.isInteract()) {
            if (activeDialogueNpc.hasMoreLines()) {
                activeDialogueNpc.advanceDialogue();
            } else {
                activeDialogueNpc = null;
                state = GameState.PLAYING;
            }
        }
    }

    // --- QUIZ ---
    private void updateQuiz(InputHandler input) {
        if (!activeQuiz.isAnswered()) {
            int optionCount = activeQuiz.getQuestion().getOptions().size();
            if (input.wasJustPressed(java.awt.event.KeyEvent.VK_UP) || input.wasJustPressed(java.awt.event.KeyEvent.VK_W)) {
                selectedQuizOption = (selectedQuizOption - 1 + optionCount) % optionCount;
            }
            if (input.wasJustPressed(java.awt.event.KeyEvent.VK_DOWN) || input.wasJustPressed(java.awt.event.KeyEvent.VK_S)) {
                selectedQuizOption = (selectedQuizOption + 1) % optionCount;
            }
            for (int i = 0; i < optionCount && i < 4; i++) {
                if (input.wasJustPressed(java.awt.event.KeyEvent.VK_1 + i)) {
                    selectedQuizOption = i;
                    submitQuizAnswer();
                    return;
                }
            }
            if (input.isConfirm()) {
                submitQuizAnswer();
            }
        } else {
            if (input.isConfirm()) {
                closeQuiz();
            }
        }
    }

    private void submitQuizAnswer() {
        activeQuiz.submitAnswer(selectedQuizOption);
        boolean correct = activeQuiz.wasCorrect();
        quizManager.getProgress().recordAnswer(correct);
        if (correct) {
            player.getStats().getScoreSystem().addPoints(10);
        }
    }

    private void closeQuiz() {
        boolean correct = activeQuiz.wasCorrect();
        activeQuizStation.markCompleted();
        quizManager.getProgress().markTopicCompleted(activeQuiz.getQuestion().getTopic());

        if (!correct) {
            boolean wasAlive = player.isAlive();
            player.takeDamage(1, activeQuiz.getQuestion().getFeedback(false));
            if (wasAlive && !player.isAlive()) {
                activeQuiz = null;
                activeQuizStation = null;
                state = GameState.GAME_OVER;
                return;
            }
        }

        maybeGrantRoomKey();

        activeQuiz = null;
        activeQuizStation = null;

        if (world.getCurrentRoom().getId().equals("final") && allFinalStationsComplete()) {
            state = GameState.VICTORY;
        } else {
            state = GameState.PLAYING;
        }
    }

    private boolean allFinalStationsComplete() {
        for (var obj : world.getCurrentRoom().getObjects()) {
            if (obj instanceof QuizStation qs && !qs.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    /** If every quiz station in the current (non-final) room is complete, grant that room's key. */
    private void maybeGrantRoomKey() {
        Room room = world.getCurrentRoom();
        String roomId = room.getId();
        game.gameplay.Key key = roomCompletionKey.get(roomId);
        if (key == null) {
            return; // final room has no key to grant
        }
        boolean allStationsComplete = true;
        for (var obj : room.getObjects()) {
            if (obj instanceof QuizStation qs && !qs.isCompleted()) {
                allStationsComplete = false;
                break;
            }
        }
        if (allStationsComplete && !player.getInventory().hasKey(key)) {
            player.getInventory().addKey(key);
            showToast("You earned the " + key.getDisplayName() + "! The next door is now unlocked.", 3);
        }
    }

    // --- INVENTORY ---
    private void updateInventory(InputHandler input) {
        if (input.isPause() || input.wasJustPressed(java.awt.event.KeyEvent.VK_I)) {
            state = GameState.PLAYING;
        }
    }

    // --- PAUSED ---
    private void updatePaused(InputHandler input) {
        if (input.wasJustPressed(java.awt.event.KeyEvent.VK_UP) || input.wasJustPressed(java.awt.event.KeyEvent.VK_W)) {
            pauseMenuIndex = (pauseMenuIndex - 1 + PAUSE_MENU_OPTIONS.length) % PAUSE_MENU_OPTIONS.length;
        }
        if (input.wasJustPressed(java.awt.event.KeyEvent.VK_DOWN) || input.wasJustPressed(java.awt.event.KeyEvent.VK_S)) {
            pauseMenuIndex = (pauseMenuIndex + 1) % PAUSE_MENU_OPTIONS.length;
        }
        if (input.isConfirm()) {
            if (pauseMenuIndex == 0) {
                state = stateBeforePause;
            } else {
                state = GameState.MAIN_MENU;
                mainMenuIndex = 0;
            }
        } else if (input.isPause()) {
            state = stateBeforePause;
        }
    }

    // --- GAME_OVER ---
    private void updateGameOver(InputHandler input) {
        if (input.isConfirm()) {
            // Respawn at the last checkpoint rather than restarting the whole game.
            player.getStats().getLifeSystem().resetLives();
            world.goToRoom(checkpoint.getRoomId());
            player.setPosition(checkpoint.getX(), checkpoint.getY());
            state = GameState.PLAYING;
        } else if (input.isPause()) {
            state = GameState.MAIN_MENU;
        }
    }

    // --- VICTORY ---
    private void updateVictory(InputHandler input) {
        if (input.isConfirm() || input.isPause()) {
            state = GameState.MAIN_MENU;
            mainMenuIndex = 0;
        }
    }
}
