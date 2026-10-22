package scuttle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import scuttle.controller.GameController;
import static scuttle.model.Constants.TICK_DELAY;
import scuttle.model.World;
import static scuttle.view.ViewConstants.*;

/**
 * The game screen. A Swing timer calls controller.tick() every TICK_DELAY ms and repaints. On top
 * of the world it shows the HUD, the pause button, and the pause and game-over overlays.
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {

    private static final double GAME_OVER_BUTTON_SIZE = 0.14;
    private static final double GAME_OVER_BOTTOM_WEIGHT = 0.5;
    private static final double PAUSE_BUTTON_SIZE = 0.2;
    private static final double PAUSE_BOTTOM_WEIGHT = 0.6;
    private static final double PAUSE_ICON_SIZE = 48.0;
    private static final int PAUSE_BUTTON_MARGIN = 35;
    private static final int BUTTON_INSETS = 8;

    private final Timer timer;
    private final JPanel overlay;
    private final JPanel pauseOverlay;
    private final ImageButton restartButton;
    private final ImageButton menuButton;
    private final ImageButton pauseMenuButton;
    private final ImageButton pauseButton;
    private final Runnable onMenu;

    private final GameController controller = new GameController();

    private final WorldRenderer worldRenderer = new WorldRenderer();
    private final HudRenderer hudRenderer = new HudRenderer();

    public GamePanel(Runnable onMenu) {
        this.onMenu = onMenu;
        timer = new Timer(TICK_DELAY, this);
        KeyHandler keyHandler = new KeyHandler(controller);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(keyHandler);
        setFocusable(true);
        setLayout(new BorderLayout());

        restartButton = ImageButton.fromAssets(GuiAssets.RESTART);
        menuButton = ImageButton.fromAssets(GuiAssets.MENU);
        restartButton.addActionListener(e -> {
            controller.restart();
            requestFocusInWindow();
        });
        menuButton.addActionListener(e -> returnToMenu());

        overlay = createButtonOverlay(GAME_OVER_BUTTON_SIZE, GAME_OVER_BOTTOM_WEIGHT, restartButton,
                menuButton);

        pauseMenuButton = ImageButton.fromAssets(GuiAssets.MENU);
        pauseMenuButton.addActionListener(e -> returnToMenu());

        pauseOverlay = createButtonOverlay(PAUSE_BUTTON_SIZE, PAUSE_BOTTOM_WEIGHT, pauseMenuButton);

        JPanel layers = new JPanel(null) {
            @Override
            public void doLayout() {
                overlay.setBounds(0, 0, getWidth(), getHeight());
                pauseOverlay.setBounds(0, 0, getWidth(), getHeight());
            }
        };
        layers.setOpaque(false);
        layers.add(overlay);
        layers.add(pauseOverlay);
        add(layers, BorderLayout.CENTER);

        BufferedImage pauseIcon = GuiAssets.pauseIcon();
        pauseButton = new ImageButton(pauseIcon, pauseIcon, pauseIcon);
        pauseButton.applyScale(PAUSE_ICON_SIZE / pauseIcon.getWidth());
        pauseButton.addActionListener(e -> {
            controller.togglePause();
            requestFocusInWindow();
        });

        JPanel hud = new JPanel(new FlowLayout(FlowLayout.RIGHT, PAUSE_BUTTON_MARGIN,
                PAUSE_BUTTON_MARGIN));
        hud.setOpaque(false);
        hud.add(pauseButton);
        add(hud, BorderLayout.NORTH);
    }

    private JPanel createButtonOverlay(double sizeFactor, double bottomWeight,
            ImageButton... buttons) {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            public void doLayout() {
                double scale = (Math.min(getWidth(), getHeight()) * sizeFactor)
                        / GuiAssets.BUTTON_WIDTH;
                for (ImageButton button: buttons) {
                    button.applyScale(scale);
                }
                super.doLayout();
            }
        };
        panel.setOpaque(false);
        panel.setVisible(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;

        JPanel spacerTop = spacer();
        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(spacerTop, constraints);

        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(BUTTON_INSETS, BUTTON_INSETS, BUTTON_INSETS,
                BUTTON_INSETS);
        for (int i = 0; i < buttons.length; i++) {
            constraints.gridy = i + 1;
            panel.add(buttons[i], constraints);
        }

        JPanel spacerBottom = spacer();

        constraints.gridy = buttons.length + 1;
        constraints.weighty = bottomWeight;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        panel.add(spacerBottom, constraints);

        return panel;
    }

    private static JPanel spacer() {
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        return spacer;
    }

    public void startGame() {
        syncOverlay();
        timer.start();
        requestFocusInWindow();
    }

    private void syncOverlay() {
        overlay.setVisible(controller.isGameOver());
        pauseOverlay.setVisible(controller.isPaused() && !controller.isGameOver());
        pauseButton.setVisible(!controller.isGameOver());
    }

    private void returnToMenu() {
        timer.stop();
        controller.reset();
        syncOverlay();
        onMenu.run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.tick();
        syncOverlay();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        World world = controller.getWorld();
        Graphics2D g2 = (Graphics2D) g.create();

        try {
            double scaleX = getWidth() <= 0 ? 1 : (double) getWidth() / SCREEN_WIDTH;
            double scaleY = getHeight() <= 0 ? 1 : (double) getHeight() / SCREEN_HEIGHT;
            g2.scale(scaleX, scaleY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            worldRenderer.draw(g2, world, controller.getFrameCounter());
            if (world.isGameOver()) {
                hudRenderer.drawGameOver(g2, world.getScore());
            } else if (controller.isPaused()) {
                hudRenderer.drawPaused(g2);
            }
            hudRenderer.drawScore(g2, world.getScore(), world.getCoinAmount());
        } finally {
            g2.dispose();
        }
    }

    /**
     * Returns false because the overlays sit on top of each other. This tells Swing to repaint the
     * layers in the right order.
     */
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

}
