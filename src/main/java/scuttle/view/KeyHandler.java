package scuttle.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import scuttle.model.Direction;

public class KeyHandler implements KeyListener {

    private final GameInput gameInput;
    private boolean upHeld;
    private boolean downHeld;
    private boolean leftHeld;
    private boolean rightHeld;

    public KeyHandler(GameInput gameInput) {
        this.gameInput = gameInput;
        upHeld = false;
        downHeld = false;
        leftHeld = false;
        rightHeld = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used: movement reacts to pressed/released
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if (!upHeld) {
                    gameInput.move(Direction.UP);
                    upHeld = true;
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if (!downHeld) {
                    gameInput.move(Direction.DOWN);
                    downHeld = true;
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if (!leftHeld) {
                    gameInput.move(Direction.LEFT);
                    leftHeld = true;
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if (!rightHeld) {
                    gameInput.move(Direction.RIGHT);
                    rightHeld = true;
                }
            }
            //TEMP untill Game Over menu
            case KeyEvent.VK_R -> {
                gameInput.restart();
            }
            case KeyEvent.VK_ESCAPE -> {
                gameInput.togglePause();
            }
            default -> {/*other input is ignored*/
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W, KeyEvent.VK_UP ->
                upHeld = false;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN ->
                downHeld = false;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT ->
                leftHeld = false;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT ->
                rightHeld = false;
            default -> {/*other input is ignored*/
            }

        }
    }

}
