package scuttle.view;

import java.awt.CardLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GameWindow {

    private static final String MENU_CARD = "MENU";
    private static final String GAME_CARD = "GAME";
    private static final String TOGGLE_FULLSCREEN = "toggleFullscreen";
    private static final String EXIT_FULLSCREEN = "exitFullscreen";
    private boolean fullscreen;

    private final JFrame window;
    private final CardLayout cards;
    private final JPanel root;
    private final GamePanel gamePanel;

    public GameWindow() {
        window = new JFrame("Scuttle");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);

        cards = new CardLayout();
        root = new JPanel(cards);
        gamePanel = new GamePanel(this::showMenu);
        MainMenu menu = new MainMenu(this::startGame, this::quit);

        root.add(menu, MENU_CARD);
        root.add(gamePanel, GAME_CARD);

        window.add(root);

        var input = window.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        var actions = window.getRootPane().getActionMap();
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), TOGGLE_FULLSCREEN);
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), EXIT_FULLSCREEN);
        actions.put(TOGGLE_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFullscreen(!fullscreen);
            }
        });
        actions.put(EXIT_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fullscreen) {
                    setFullscreen(false);
                }
            }
        });
    }

    private static Rectangle screenBounds() {
        GraphicsDevice device = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        return device.getDefaultConfiguration().getBounds();
    }

    private void setFullscreen(boolean on) {
        fullscreen = on;
        boolean visible = window.isVisible();

        if (window.isDisplayable()) {
            window.dispose();
        }

        window.setUndecorated(on);

        if (on) {
            window.setResizable(false);
            window.setBounds(screenBounds());
        } else {
            window.setResizable(true);
            window.pack();
            window.setLocationRelativeTo(null);
        }

        window.setVisible(visible);
    }

    public void show() {
        showMenu();
        setFullscreen(true);
        window.setVisible(true);
        window.toFront();
    }

    public void showMenu() {
        cards.show(root, MENU_CARD);
    }

    public void startGame() {
        cards.show(root, GAME_CARD);
        gamePanel.startGame();
    }

    public void quit() {
        window.dispose();
        System.exit(0);
    }

}
