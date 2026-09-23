package game.core;

/** High-level states the game can be in. GameManager controls transitions between these. */
public enum GameState {
    MAIN_MENU,
    INSTRUCTIONS,
    PLAYING,
    DIALOGUE,
    QUIZ,
    INVENTORY,
    PAUSED,
    GAME_OVER,
    VICTORY
}
