package game;

import game.core.GameManager;
import game.core.GameState;
import game.objects.Door;
import game.objects.QuizStation;
import game.utils.InputHandler;
import game.world.Room;
import game.world.RoomFactory;
import game.world.World;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Headless smoke test (no window created) that exercises the core game
 * logic end-to-end: world construction, then a full simulated playthrough
 * of the Entrance room (collect item -> answer quiz -> receive key ->
 * unlock door -> transition room), to catch wiring mistakes between
 * RoomFactory, GameManager and the quiz/key/door systems.
 */
public class SmokeTest {

    private static int failures = 0;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");

        testWorldBuildsAllRooms();
        testRoomProgressionWiringConsistent();
        testFullEntranceRoomPlaythrough();

        if (failures == 0) {
            System.out.println("ALL SMOKE TESTS PASSED");
        } else {
            System.out.println(failures + " SMOKE TEST(S) FAILED");
            System.exit(1);
        }
    }

    private static void check(String label, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + label);
        } else {
            System.out.println("[FAIL] " + label);
            failures++;
        }
    }

    private static void testWorldBuildsAllRooms() {
        World world = RoomFactory.buildWorld();
        check("world has 6 rooms", world.getRoomCount() == 6);
        for (String id : List.of("entrance", "classroom", "environment", "first_aid", "evacuation", "final")) {
            Room r = world.getRoom(id);
            check("room exists: " + id, r != null);
            if (r != null) {
                check("room " + id + " has solid bounds computed", r.getSolidBounds().length > 0);
            }
        }
    }

    private static void testRoomProgressionWiringConsistent() {
        World world = RoomFactory.buildWorld();
        // Every non-final room must have exactly one Door whose targetRoomId matches an existing room.
        for (String id : List.of("entrance", "classroom", "environment", "first_aid", "evacuation")) {
            Room room = world.getRoom(id);
            long doorCount = room.getObjects().stream().filter(o -> o instanceof Door).count();
            check("room " + id + " has exactly one door", doorCount == 1);
            room.getObjects().stream().filter(o -> o instanceof Door).map(o -> (Door) o).forEach(door -> {
                Room target = world.getRoom(door.getTargetRoomId());
                check("door in " + id + " targets a real room (" + door.getTargetRoomId() + ")", target != null);
            });
            long quizCount = room.getObjects().stream().filter(o -> o instanceof QuizStation).count();
            check("room " + id + " has at least one quiz station", quizCount >= 1);
        }
    }

    private static void testFullEntranceRoomPlaythrough() {
        GameManager gm = new GameManager();
        gm.startNewGame();
        check("state is PLAYING after startNewGame", gm.getState() == GameState.PLAYING);
        check("starts in entrance room", gm.getWorld().getCurrentRoom().getId().equals("entrance"));
        check("starts with 3 lives", gm.getPlayer().getStats().getLifeSystem().getLives() == 3);

        InputHandler input = new InputHandler();
        JPanel dummySource = new JPanel();

        // Walk toward the quiz station and interact until the quiz completes correctly.
        Room entrance = gm.getWorld().getCurrentRoom();
        QuizStation station = entrance.getObjects().stream()
                .filter(o -> o instanceof QuizStation)
                .map(o -> (QuizStation) o)
                .findFirst().orElse(null);
        check("entrance quiz station found", station != null);

        // Teleport player onto the quiz station via repeated movement isn't necessary for
        // this logic test - directly place the player at the station's position so the
        // interaction-range check in Room.findNearestInteractable succeeds.
        gm.getPlayer().setPosition(station.getX(), station.getY());

        pressAndRelease(input, dummySource, KeyEvent.VK_E);
        gm.update(input, 0.016);
        input.endFrame();
        check("entering quiz opens QUIZ state", gm.getState() == GameState.QUIZ);

        // Select the correct option every time by trying each option index until one is marked correct.
        int correctIndex = gm.getActiveQuiz().getQuestion().getCorrectIndex();
        pressAndRelease(input, dummySource, KeyEvent.VK_1 + correctIndex);
        gm.update(input, 0.016);
        input.endFrame();
        check("quiz answered", gm.getActiveQuiz().isAnswered());
        check("quiz answered correctly", gm.getActiveQuiz().wasCorrect());

        pressAndRelease(input, dummySource, KeyEvent.VK_ENTER);
        gm.update(input, 0.016);
        input.endFrame();
        check("returns to PLAYING after closing quiz", gm.getState() == GameState.PLAYING);
        check("player earned the entrance key",
                gm.getPlayer().getInventory().hasKey(game.gameplay.Keys.ENTRANCE_KEY));

        // Now walk up to the door and interact - it should be unlocked and transition rooms.
        Door door = entrance.getObjects().stream()
                .filter(o -> o instanceof Door)
                .map(o -> (Door) o)
                .findFirst().orElse(null);
        check("entrance door found", door != null);
        gm.getPlayer().setPosition(door.getX() - 10, door.getY());

        gm.update(input, 0.016); // let door refresh its lock state against the now-keyed player
        check("door unlocked after earning key", door.isUnlocked());

        pressAndRelease(input, dummySource, KeyEvent.VK_E);
        gm.update(input, 0.016);
        input.endFrame();
        check("transitioned to classroom room", gm.getWorld().getCurrentRoom().getId().equals("classroom"));
    }

    private static void pressAndRelease(InputHandler input, JPanel source, int keyCode) {
        input.keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED));
        input.keyReleased(new KeyEvent(source, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED));
    }
}
