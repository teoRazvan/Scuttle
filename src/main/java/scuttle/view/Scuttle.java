package scuttle.view;

import javax.swing.SwingUtilities;

/**
 * The entry point. It opens the game window.
 */
public class Scuttle {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow().show());
    }

}
