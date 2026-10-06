package com.mycompany.test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public boolean upReleased, downReleased, leftReleased, rightReleased;

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W && !upPressed) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A && !leftPressed) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S && !downPressed) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D && !rightPressed) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            if (upPressed) {
                upReleased = true;
                upPressed = false;
            }
        }
        if (code == KeyEvent.VK_A) {
            if (leftPressed) {
                leftReleased = true;
                leftPressed = false;
            }
        }
        if (code == KeyEvent.VK_S) {
            if (downPressed) {
                downReleased = true;
                downPressed = false;
            }
        }
        if (code == KeyEvent.VK_D) {
            if (rightPressed) {
                rightReleased = true;
                rightPressed = false;
            }
        }
    }

}
