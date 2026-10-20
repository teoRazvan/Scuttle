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
 *
 * Game panel is the drawing surface
 *
 * TEMP it runs the game loop and handles input, will be changed later
 */
public class GamePanel extends JPanel implements ActionListener {

    private final Timer timer;
    private final KeyHandler keyH;
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
        keyH = new KeyHandler(controller);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setLayout(new BorderLayout());

        restartButton = new ImageButton(
                GuiAssets.button(GuiAssets.RESTART, GuiAssets.IDLE),
                GuiAssets.button(GuiAssets.RESTART, GuiAssets.HOVER),
                GuiAssets.button(GuiAssets.RESTART, GuiAssets.PRESSED));
        menuButton = new ImageButton(
                GuiAssets.button(GuiAssets.MENU, GuiAssets.IDLE),
                GuiAssets.button(GuiAssets.MENU, GuiAssets.HOVER),
                GuiAssets.button(GuiAssets.MENU, GuiAssets.PRESSED));
        restartButton.addActionListener(e -> {
            controller.restart();
            requestFocusInWindow();
        });
        menuButton.addActionListener(e -> returnToMenu());

        overlay = new JPanel(new GridBagLayout()) {
            @Override
            public void doLayout() {
                double scale = (Math.min(getWidth(), getHeight()) * 0.14) / GuiAssets.BUTTON_WIDTH;
                restartButton.applyScale(scale);
                menuButton.applyScale(scale);
                super.doLayout();
            }
        };
        overlay.setOpaque(false);
        overlay.setVisible(false);

        JPanel spacerTop = new JPanel();
        spacerTop.setOpaque(false);
        JPanel spacerBottom = new JPanel();
        spacerBottom.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;

        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        overlay.add(spacerTop, constraints);

        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.gridy = 1;
        overlay.add(restartButton, constraints);
        constraints.gridy = 2;
        overlay.add(menuButton, constraints);

        constraints.gridy = 3;
        constraints.weighty = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        overlay.add(spacerBottom, constraints);

        pauseMenuButton = new ImageButton(
                GuiAssets.button(GuiAssets.MENU, GuiAssets.IDLE),
                GuiAssets.button(GuiAssets.MENU, GuiAssets.HOVER),
                GuiAssets.button(GuiAssets.MENU, GuiAssets.PRESSED));
        pauseMenuButton.addActionListener(e -> returnToMenu());

        pauseOverlay = new JPanel(new GridBagLayout()) {
            @Override
            public void doLayout() {
                double scale = (Math.min(getWidth(), getHeight()) * 0.2) / GuiAssets.BUTTON_WIDTH;
                pauseMenuButton.applyScale(scale);
                super.doLayout();
            }
        };
        pauseOverlay.setOpaque(false);
        pauseOverlay.setVisible(false);

        JPanel pauseSpacerTop = new JPanel();
        pauseSpacerTop.setOpaque(false);
        JPanel pauseSpacerBottom = new JPanel();
        pauseSpacerBottom.setOpaque(false);

        GridBagConstraints pauseConstraints = new GridBagConstraints();
        pauseConstraints.gridx = 0;
        pauseConstraints.gridy = 0;
        pauseConstraints.weighty = 1;
        pauseConstraints.fill = GridBagConstraints.BOTH;
        pauseOverlay.add(pauseSpacerTop, pauseConstraints);

        pauseConstraints.weighty = 0;
        pauseConstraints.fill = GridBagConstraints.NONE;
        pauseConstraints.insets = new Insets(8, 8, 8, 8);
        pauseConstraints.gridy = 1;
        pauseOverlay.add(pauseMenuButton, pauseConstraints);

        pauseConstraints.gridy = 2;
        pauseConstraints.weighty = 0.6;
        pauseConstraints.fill = GridBagConstraints.BOTH;
        pauseConstraints.insets = new Insets(0, 0, 0, 0);
        pauseOverlay.add(pauseSpacerBottom, pauseConstraints);

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
        this.add(layers, BorderLayout.CENTER);

        BufferedImage pauseIcon = GuiAssets.pauseIcon();
        pauseButton = new ImageButton(pauseIcon, pauseIcon, pauseIcon);
        pauseButton.applyScale(48.0 / pauseIcon.getWidth());
        pauseButton.addActionListener(e -> {
            controller.togglePause();
            requestFocusInWindow();
        });

        JPanel hud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        hud.setOpaque(false);
        hud.add(pauseButton);
        this.add(hud, BorderLayout.NORTH);

    }

    public void startGame() {
        syncOverlay();
        timer.start();
        this.requestFocusInWindow();
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
        World world = controller.getWorld();
        super.paintComponent(g);
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

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

}
