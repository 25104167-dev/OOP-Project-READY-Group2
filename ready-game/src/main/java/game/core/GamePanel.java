package game.core;

import game.ui.DialogueBox;
import game.ui.GameOverScreen;
import game.ui.HUD;
import game.ui.InstructionsScreen;
import game.ui.InventoryUI;
import game.ui.MainMenuScreen;
import game.ui.PauseScreen;
import game.ui.QuizUI;
import game.ui.VictoryScreen;
import game.utils.Constants;
import game.utils.InputHandler;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * The Swing surface the game renders to. Owns the {@link GameManager} and a
 * {@link Timer}-based game loop running at roughly {@link Constants#FPS}.
 * Rendering is dispatched by {@link GameState}; each state has its own
 * small UI class responsible for drawing itself.
 */
public class GamePanel extends JPanel {

    private final GameManager gameManager = new GameManager();
    private final InputHandler inputHandler = new InputHandler();

    private final MainMenuScreen mainMenuScreen = new MainMenuScreen();
    private final InstructionsScreen instructionsScreen = new InstructionsScreen();
    private final HUD hud = new HUD();
    private final DialogueBox dialogueBox = new DialogueBox();
    private final QuizUI quizUI = new QuizUI();
    private final InventoryUI inventoryUI = new InventoryUI();
    private final PauseScreen pauseScreen = new PauseScreen();
    private final GameOverScreen gameOverScreen = new GameOverScreen();
    private final VictoryScreen victoryScreen = new VictoryScreen();

    private Timer loopTimer;
    private long lastNanoTime;

    public GamePanel() {
        setPreferredSize(new java.awt.Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setFocusable(true);
        addKeyListener(inputHandler);
        setBackground(Constants.COLOR_BG);
    }

    public void start() {
        lastNanoTime = System.nanoTime();
        loopTimer = new Timer(1000 / Constants.FPS, e -> tick());
        loopTimer.start();
        requestFocusInWindow();
    }

    private void tick() {
        long now = System.nanoTime();
        double deltaSeconds = (now - lastNanoTime) / 1_000_000_000.0;
        lastNanoTime = now;

        gameManager.update(inputHandler, deltaSeconds);
        inputHandler.endFrame();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        switch (gameManager.getState()) {
            case MAIN_MENU -> mainMenuScreen.render(g, gameManager);
            case INSTRUCTIONS -> instructionsScreen.render(g, gameManager);
            case PLAYING -> renderPlaying(g);
            case DIALOGUE -> {
                renderPlaying(g);
                dialogueBox.render(g, gameManager);
            }
            case QUIZ -> {
                renderPlaying(g);
                quizUI.render(g, gameManager);
            }
            case INVENTORY -> {
                renderPlaying(g);
                inventoryUI.render(g, gameManager);
            }
            case PAUSED -> {
                renderPlaying(g);
                dimOverlay(g);
                pauseScreen.render(g, gameManager);
            }
            case GAME_OVER -> gameOverScreen.render(g, gameManager);
            case VICTORY -> victoryScreen.render(g, gameManager);
        }
    }

    private void renderPlaying(Graphics2D g) {
        g.setColor(Constants.COLOR_BG);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        gameManager.getWorld().getCurrentRoom().render(g);
        gameManager.getPlayer().render(g);
        hud.render(g, gameManager);
    }

    private void dimOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }
}
