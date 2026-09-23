package game.core;

import game.utils.Constants;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Application entry point. Creates the window and starts the game loop.
 * Run with: java -cp out game.core.Game (see README for full instructions).
 */
public class Game {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::createAndShowWindow);
    }

    private static void createAndShowWindow() {
        JFrame frame = new JFrame(Constants.GAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.start();
    }
}
