package game.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracks which keys are currently pressed and exposes convenience
 * queries used by the rest of the game. Also tracks "just pressed"
 * events for menu navigation so a single tap doesn't repeat every frame.
 */
public class InputHandler implements KeyListener {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> justPressedKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (!pressedKeys.contains(code)) {
            justPressedKeys.add(code);
        }
        pressedKeys.add(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    public boolean isDown(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /** True only on the single frame the key transitioned from up to down. */
    public boolean wasJustPressed(int keyCode) {
        return justPressedKeys.contains(keyCode);
    }

    /** Call once per game loop tick after input has been read. */
    public void endFrame() {
        justPressedKeys.clear();
    }

    public boolean isMovingUp() {
        return isDown(KeyEvent.VK_W) || isDown(KeyEvent.VK_UP);
    }

    public boolean isMovingDown() {
        return isDown(KeyEvent.VK_S) || isDown(KeyEvent.VK_DOWN);
    }

    public boolean isMovingLeft() {
        return isDown(KeyEvent.VK_A) || isDown(KeyEvent.VK_LEFT);
    }

    public boolean isMovingRight() {
        return isDown(KeyEvent.VK_D) || isDown(KeyEvent.VK_RIGHT);
    }

    public boolean isInteract() {
        return wasJustPressed(KeyEvent.VK_E);
    }

    public boolean isConfirm() {
        return wasJustPressed(KeyEvent.VK_ENTER);
    }

    public boolean isPause() {
        return wasJustPressed(KeyEvent.VK_ESCAPE);
    }
}
