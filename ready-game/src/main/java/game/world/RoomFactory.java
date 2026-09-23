package game.world;

import game.education.EducationalTopic;
import game.education.InformationCard;
import game.gameplay.Keys;
import game.objects.Collectible;
import game.objects.Door;
import game.objects.Hazard;
import game.objects.NPC;
import game.objects.QuizStation;
import game.utils.Constants;

import java.util.List;

/**
 * Builds the six rooms of the game with their tile layouts and contents.
 * Kept as a separate factory (rather than in Room itself) so room content
 * definitions don't clutter the Room class's actual runtime behavior.
 */
public final class RoomFactory {

    private static final int COLS = Constants.ROOM_COLS;
    private static final int ROWS = Constants.ROOM_ROWS;
    private static final int TS = Constants.TILE_SIZE;

    private RoomFactory() {
    }

    /** Builds a bordered room of plain floor, with optional gaps (doorways) in the border walls. */
    private static Tile[][] baseGrid(int... gapCols) {
        Tile[][] grid = new Tile[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                boolean border = (r == 0 || r == ROWS - 1 || c == 0 || c == COLS - 1);
                grid[r][c] = border ? Tile.WALL : Tile.FLOOR;
            }
        }
        return grid;
    }

    private static void carveDoorway(Tile[][] grid, int row, int col) {
        grid[row][col] = Tile.FLOOR;
        if (row == 0 || row == ROWS - 1) {
            // widen horizontally for a comfortable doorway
            if (col > 0) grid[row][col - 1] = Tile.FLOOR;
            if (col < COLS - 1) grid[row][col + 1] = Tile.FLOOR;
        } else {
            if (row > 0) grid[row - 1][col] = Tile.FLOOR;
            if (row < ROWS - 1) grid[row + 1][col] = Tile.FLOOR;
        }
    }

    public static World buildWorld() {
        World world = new World();
        world.addRoom(buildEntrance());
        world.addRoom(buildClassroom());
        world.addRoom(buildEnvironmentRoom());
        world.addRoom(buildFirstAidRoom());
        world.addRoom(buildEvacuationRoom());
        world.addRoom(buildFinalRoom());
        return world;
    }

    // ---------------------------------------------------------------
    // ROOM 1: School Entrance
    // ---------------------------------------------------------------
    private static Room buildEntrance() {
        Tile[][] grid = baseGrid();
        int doorRow = ROWS / 2;
        carveDoorway(grid, doorRow, COLS - 1);

        Room room = new Room("entrance", "School Entrance", grid,
                TS * 2, TS * doorRow,
                "Explore the entrance, talk to the guard, and learn the basics before heading to class.");

        room.addObject(new NPC(TS * 4, TS * (doorRow - 2), "Guard Mel", List.of(
                "Whoa there! Big earthquake drill today, but sensors say this one might be real.",
                "Before you go anywhere: always know your nearest exits and keep calm.",
                "There's an emergency board over there. Check it, then head through that door to class.")));

        room.addObject(new Collectible("entrance_pamphlet", TS * 8, TS * 4,
                new InformationCard("Emergency Kit Basics",
                        "A basic kit includes water, a flashlight, first-aid supplies, and a whistle.",
                        EducationalTopic.PREPAREDNESS_BASICS)));

        room.addObject(new QuizStation("entrance_quiz", TS * 12, TS * (doorRow), EducationalTopic.PREPAREDNESS_BASICS));

        Door doorToClassroom = new Door("door_entrance_to_classroom",
                (COLS - 1) * TS, doorRow * TS, TS, TS, "classroom", Keys.ENTRANCE_KEY);
        room.addObject(doorToClassroom);

        return room;
    }

    // ---------------------------------------------------------------
    // ROOM 2: Classroom (earthquake safety)
    // ---------------------------------------------------------------
    private static Room buildClassroom() {
        Tile[][] grid = baseGrid();
        int doorRow = ROWS / 2;
        carveDoorway(grid, doorRow, 0);          // entry from entrance
        carveDoorway(grid, doorRow, COLS - 1);   // exit to environment room

        Room room = new Room("classroom", "Classroom", grid,
                TS * 2, TS * doorRow,
                "An earthquake is coming! Learn what to do, then complete the safety challenge.");

        // "Hazards" representing unsafe furniture to avoid standing near during the drill
        room.addObject(new Hazard("hazard_window", TS * 5, TS * 3, TS, TS * 2,
                "Window", "Standing beside a window during an earthquake risks injury from broken glass.", 1));
        room.addObject(new Hazard("hazard_bookshelf", TS * 14, TS * 9, TS * 2, TS,
                "Tall Bookshelf", "Tall unsecured bookshelves can tip over during shaking.", 1));

        room.addObject(new NPC(TS * 9, TS * 5, "Classmate Rio", List.of(
                "The ground is shaking! Quick, get under something sturdy!",
                "Remember: Drop, Cover, and Hold On. Avoid windows and tall furniture.")));

        room.addObject(new Collectible("classroom_card", TS * 10, TS * 10,
                new InformationCard("Drop, Cover, Hold On",
                        "During shaking: drop to the ground, take cover under sturdy furniture, and hold on.",
                        EducationalTopic.EARTHQUAKE_SAFETY)));

        room.addObject(new QuizStation("classroom_quiz", TS * 9, TS * 7, EducationalTopic.EARTHQUAKE_SAFETY));

        Door doorToEnv = new Door("door_classroom_to_environment",
                (COLS - 1) * TS, doorRow * TS, TS, TS, "environment", Keys.CLASSROOM_KEY);
        room.addObject(doorToEnv);

        return room;
    }

    // ---------------------------------------------------------------
    // ROOM 3: Environmental Awareness Room
    // ---------------------------------------------------------------
    private static Room buildEnvironmentRoom() {
        Tile[][] grid = baseGrid();
        int doorRow = ROWS / 2;
        carveDoorway(grid, doorRow, 0);
        carveDoorway(grid, doorRow, COLS - 1);

        Room room = new Room("environment", "Environmental Awareness Room", grid,
                TS * 2, TS * doorRow,
                "Clear the blocked drainage of hazardous waste, then learn how the environment relates to disaster risk.");

        room.addObject(new NPC(TS * 6, TS * 3, "Groundskeeper Ana", List.of(
                "This drain is clogged with trash again! Clogged drains make flooding much worse.",
                "Being kind to the environment is part of being prepared for disasters too.")));

        // "Trash" hazards near the drain that represent clogging material (touching = minor setback, not damage)
        room.addObject(new Collectible("env_bottle", TS * 10, TS * 4,
                new InformationCard("Plastic Waste & Drainage",
                        "Improperly discarded plastic bottles and packaging commonly clog drainage systems.",
                        EducationalTopic.ENVIRONMENTAL_AWARENESS)));

        room.addObject(new Collectible("env_fact_card", TS * 13, TS * 8,
                new InformationCard("Environment & Disaster Risk",
                        "Protecting the environment won't stop every disaster, but it reduces certain risks and builds resilience.",
                        EducationalTopic.ENVIRONMENTAL_AWARENESS)));

        room.addObject(new Hazard("hazard_flood", TS * 6, TS * 9, TS * 3, TS,
                "Flooded Patch", "Flooded areas from blocked drainage can hide hazards underfoot.", 1));

        room.addObject(new QuizStation("environment_quiz", TS * 9, TS * 6, EducationalTopic.ENVIRONMENTAL_AWARENESS));

        Door doorToFirstAid = new Door("door_environment_to_firstaid",
                (COLS - 1) * TS, doorRow * TS, TS, TS, "first_aid", Keys.ENVIRONMENT_KEY);
        room.addObject(doorToFirstAid);

        return room;
    }

    // ---------------------------------------------------------------
    // ROOM 4: First Aid Room
    // ---------------------------------------------------------------
    private static Room buildFirstAidRoom() {
        Tile[][] grid = baseGrid();
        int doorRow = ROWS / 2;
        carveDoorway(grid, doorRow, 0);
        carveDoorway(grid, doorRow, COLS - 1);

        Room room = new Room("first_aid", "First Aid Room", grid,
                TS * 2, TS * doorRow,
                "A classmate was hurt when the shaking knocked over a chair. Help them the right way.");

        room.addObject(new NPC(TS * 10, TS * 6, "Classmate Jo", List.of(
                "Ow... I twisted my ankle when a chair fell over.",
                "Can you help me? Remember to check the area is safe first!")));

        room.addObject(new Collectible("firstaid_card", TS * 7, TS * 4,
                new InformationCard("First Aid Steps",
                        "1) Check the area is safe. 2) Get help. 3) Respond to the injury calmly and carefully.",
                        EducationalTopic.FIRST_AID)));

        room.addObject(new Hazard("hazard_fallen_chair", TS * 12, TS * 9, TS, TS,
                "Fallen Chair", "A fallen chair left in a walkway can cause a trip injury.", 1));

        room.addObject(new QuizStation("firstaid_quiz", TS * 9, TS * 8, EducationalTopic.FIRST_AID));

        Door doorToEvac = new Door("door_firstaid_to_evacuation",
                (COLS - 1) * TS, doorRow * TS, TS, TS, "evacuation", Keys.FIRST_AID_KEY);
        room.addObject(doorToEvac);

        return room;
    }

    // ---------------------------------------------------------------
    // ROOM 5: Evacuation Area
    // ---------------------------------------------------------------
    private static Room buildEvacuationRoom() {
        Tile[][] grid = baseGrid();
        int doorRow = ROWS / 2;
        carveDoorway(grid, doorRow, 0);
        carveDoorway(grid, doorRow, COLS - 1);

        Room room = new Room("evacuation", "Evacuation Area", grid,
                TS * 2, TS * doorRow,
                "Choose the safer evacuation route to reach the assembly area, then complete the final check.");

        room.addObject(new NPC(TS * 4, TS * 3, "Teacher Mr. Cruz", List.of(
                "Everyone, stay calm and follow the marked evacuation route!",
                "The shorter path is blocked by debris - take the longer, marked safe route instead.")));

        // Route A: short but blocked by hazard debris
        room.addObject(new Hazard("hazard_debris_route_a", TS * 9, TS * 5, TS * 2, TS,
                "Debris", "This shorter route is blocked by unstable debris - it is not safe to pass through.", 1));

        room.addObject(new Collectible("evac_card", TS * 5, TS * 9,
                new InformationCard("Safe Evacuation Routes",
                        "Always use the official marked evacuation route, even if it is longer than a shortcut.",
                        EducationalTopic.EVACUATION)));

        room.addObject(new QuizStation("evacuation_quiz", TS * 15, TS * 9, EducationalTopic.EVACUATION));

        Door doorToFinal = new Door("door_evacuation_to_final",
                (COLS - 1) * TS, doorRow * TS, TS, TS, "final", Keys.EVACUATION_KEY);
        room.addObject(doorToFinal);

        return room;
    }

    // ---------------------------------------------------------------
    // ROOM 6: Final Challenge / Assembly Area
    // ---------------------------------------------------------------
    private static Room buildFinalRoom() {
        Tile[][] grid = baseGrid();
        int doorRow = ROWS / 2;
        carveDoorway(grid, doorRow, 0);

        Room room = new Room("final", "Emergency Assembly Area", grid,
                TS * 2, TS * doorRow,
                "You made it to the assembly area! Complete the final scenario to prove what you've learned.");

        room.addObject(new NPC(TS * 9, TS * 5, "Principal Reyes", List.of(
                "You made it safely! That's what preparation looks like.",
                "Let's see if you can apply everything you've learned in one final scenario.")));

        room.addObject(new QuizStation("final_quiz_1", TS * 6, TS * 8, EducationalTopic.EARTHQUAKE_SAFETY));
        room.addObject(new QuizStation("final_quiz_2", TS * 9, TS * 8, EducationalTopic.ENVIRONMENTAL_AWARENESS));
        room.addObject(new QuizStation("final_quiz_3", TS * 12, TS * 8, EducationalTopic.FIRST_AID));
        room.addObject(new QuizStation("final_quiz_4", TS * 15, TS * 8, EducationalTopic.EVACUATION));
        room.addObject(new QuizStation("final_quiz_5", TS * 10, TS * 4, EducationalTopic.PREPAREDNESS_BASICS));

        return room;
    }
}
