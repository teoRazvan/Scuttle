package scuttle.view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import scuttle.model.GameState;


public class GameWindow {

    private static final String MENU_CARD = "MENU";
    private static final String GAME_CARD = "GAME";

    private final JFrame window;
    private final CardLayout cards;
    private final JPanel root;
    private final GamePanel gamePanel;

    public GameWindow() {
        window = new JFrame("Scuttle");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        cards = new CardLayout();
        root = new JPanel(cards);
        gamePanel = new GamePanel(this::showMenu);
        MainMenu menu = new MainMenu(this::startGame, this::quit);

        root.add(menu, MENU_CARD);
        root.add(gamePanel, GAME_CARD);

        window.add(root);
        window.pack();
        window.setLocationRelativeTo(null);
    }

    public void show() {
        showMenu();
        window.setVisible(true);
    }

    public void showMenu() {
        GameState.state = GameState.MENU;
        cards.show(root, MENU_CARD);
    }

    public void startGame() {
        GameState.state = GameState.PLAYING;
        cards.show(root, GAME_CARD);
        gamePanel.startGame();
    }

    public void quit() {
        GameState.state = GameState.QUIT;
        window.dispose();
        System.exit(0);
    }
}