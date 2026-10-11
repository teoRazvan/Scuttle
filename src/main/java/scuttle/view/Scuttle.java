package scuttle.view;

import javax.swing.SwingUtilities;

public class Scuttle {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow().show());
    }
}