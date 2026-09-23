package game.utils;

/**
 * Central place for game-wide constant values (screen size, tile size,
 * colors, timing). Keeping these in one place avoids "magic numbers"
 * scattered across the codebase.
 */
public final class Constants {

    private Constants() {
        // utility class - prevent instantiation
    }

    // --- Window / rendering ---
    public static final int TILE_SIZE = 32;
    public static final int ROOM_COLS = 20;
    public static final int ROOM_ROWS = 14;
    public static final int SCREEN_WIDTH = TILE_SIZE * ROOM_COLS;   // 640
    public static final int SCREEN_HEIGHT = TILE_SIZE * ROOM_ROWS + 64; // + HUD strip
    public static final int HUD_HEIGHT = 64;
    public static final int FPS = 60;

    // --- Player ---
    public static final int PLAYER_SIZE = 24;
    public static final int PLAYER_SPEED = 3;
    public static final int STARTING_LIVES = 3;
    public static final float INVULNERABILITY_SECONDS = 1.5f;

    // --- Colors (retro / 8-bit inspired palette) ---
    public static final java.awt.Color COLOR_BG = new java.awt.Color(20, 22, 34);
    public static final java.awt.Color COLOR_FLOOR = new java.awt.Color(198, 182, 140);
    public static final java.awt.Color COLOR_WALL = new java.awt.Color(72, 60, 50);
    public static final java.awt.Color COLOR_DOOR_LOCKED = new java.awt.Color(140, 40, 40);
    public static final java.awt.Color COLOR_DOOR_UNLOCKED = new java.awt.Color(60, 150, 80);
    public static final java.awt.Color COLOR_PLAYER = new java.awt.Color(70, 130, 220);
    public static final java.awt.Color COLOR_NPC = new java.awt.Color(230, 190, 60);
    public static final java.awt.Color COLOR_HAZARD = new java.awt.Color(210, 60, 40);
    public static final java.awt.Color COLOR_COLLECTIBLE = new java.awt.Color(90, 210, 190);
    public static final java.awt.Color COLOR_QUIZ_STATION = new java.awt.Color(180, 100, 220);
    public static final java.awt.Color COLOR_TEXT = new java.awt.Color(240, 240, 235);
    public static final java.awt.Color COLOR_HUD_BG = new java.awt.Color(10, 10, 16);
    public static final java.awt.Color COLOR_PANEL = new java.awt.Color(30, 32, 46, 235);
    public static final java.awt.Color COLOR_ACCENT = new java.awt.Color(255, 205, 70);

    public static final String GAME_TITLE = "READY: Disaster Preparedness Adventure";
    public static final String SAVE_FILE = "ready_save.dat";
}
