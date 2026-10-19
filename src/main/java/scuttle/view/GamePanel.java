package scuttle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import scuttle.model.CoinAmount;
import static scuttle.model.Constants.TICK_DELAY;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;
import scuttle.model.Direction;
import scuttle.model.FishLane;
import scuttle.model.GroundLane;
import scuttle.model.HighScore;
import scuttle.model.Lane;
import scuttle.model.Mover;
import scuttle.model.Player;
import scuttle.model.Position;
import scuttle.model.RaftLane;
import scuttle.model.TorpedoLane;
import scuttle.model.World;

/**
 *
 * Game panel is the drawing surface
 *
 * TEMP it runs the game loop and handles input, will be changed later
 */
public class GamePanel extends JPanel implements ActionListener, GameInput {

    private static final int ORIGINAL_TILE_SIZE = 16; // Sprite size in pixels
    private static final int SCALE = 3;// How much sprites are enlarged

    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; //48x48 pixels tile

    private static final int SCREEN_WIDTH = VISIBLE_COLUMNS * TILE_SIZE; // 768 pixels
    private static final int SCREEN_HEIGHT = VISIBLE_ROWS * TILE_SIZE; // 576 pixels
    private static final Color UNDERWATER_TINT = new Color(0, 120, 170, 40);

    private int warningCounter = 0;
    private final Timer timer;
    private final KeyHandler keyH;
    private final JPanel overlay;
    private final JPanel pauseOverlay;
    private final ImageButton restartButton;
    private final ImageButton menuButton;
    private final ImageButton pauseMenuButton;
    private final ImageButton pauseButton;
    private final Runnable onMenu;
    private World world = new World();
    private boolean paused;

    public GamePanel(Runnable onMenu) {
        this.onMenu = onMenu;
        timer = new Timer(TICK_DELAY, this);
        keyH = new KeyHandler(this);
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
        restartButton.addActionListener(e -> restart());
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
        pauseButton.addActionListener(e -> togglePause());

        JPanel hud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        hud.setOpaque(false);
        hud.add(pauseButton);
        this.add(hud, BorderLayout.NORTH);
    }

    public void startGame() {
        paused = false;
        pauseOverlay.setVisible(false);
        pauseButton.setVisible(true);
        timer.start();
        this.requestFocusInWindow();
    }

    @Override
    public void move(Direction direction) {
        if (paused) {
            return;
        }
        world.movePlayer(direction);
    }

    private void drawTile(Graphics2D g, BufferedImage image, int column, int row) {
        g.drawImage(image, columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE, null);
    }

    private void drawTileMirrored(Graphics2D g, BufferedImage image, int column, int row) {
        g.drawImage(image, columnToX(column) + TILE_SIZE, rowToY(row), -TILE_SIZE, TILE_SIZE, null);
    }

    @Override
    public void restart() {
        if (world.isGameOver()) {
            world = new World();
            paused = false;
            pauseOverlay.setVisible(false);
            pauseButton.setVisible(true);
            overlay.setVisible(false);
            this.requestFocusInWindow();
        }
    }

    @Override
    public void togglePause() {
        if (world.isGameOver()) {
            return;
        }
        paused = !paused;
        pauseOverlay.setVisible(paused);
        this.requestFocusInWindow();
    }

    private void returnToMenu() {
        timer.stop();
        paused = false;
        pauseOverlay.setVisible(false);
        world = new World();
        overlay.setVisible(false);
        onMenu.run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused) {
            this.warningCounter++;
            this.update();
        }
        this.repaint();
    }

    private int rowToY(int row) {
        return (VISIBLE_ROWS - 1 - (row - world.getCameraRow())) * TILE_SIZE;
    }

    private int columnToX(int column) {
        return column * TILE_SIZE;
    }

    private void drawGroundLane(GroundLane lane, Graphics2D g, int row) {
        for (int column = 0; column < VISIBLE_COLUMNS; column++) {
            drawTile(g, variant(Sprites.SAND, column, row), column, row);
            if (!lane.canStepOn(column)) {
                drawTile(g, variant(Sprites.CORAL, column, row), column, row);
            }

        }
    }

    private void drawTint(Graphics2D g) {
        g.setColor(UNDERWATER_TINT);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private BufferedImage getFishSprite(int i, int length, int direction) {
        if (length == 1) {
            return Sprites.FISH_SINGLE;
        } else {
            if (direction == 1) {
                if (i == length - 1) {
                    return Sprites.FISH_HEAD;
                } else if (i == 0) {
                    return Sprites.FISH_TAIL;
                } else {
                    return Sprites.FISH_BODY;
                }
            } else {
                if (i == 0) {
                    return Sprites.FISH_HEAD;
                } else if (i == length - 1) {
                    return Sprites.FISH_TAIL;
                } else {
                    return Sprites.FISH_BODY;
                }
            }
        }
    }

    private void drawFishLane(FishLane lane, Graphics2D g, int row) {
        System.out.println(lane.getDirection());
        for (int column = 0; column < VISIBLE_COLUMNS; column++) {
            drawTile(g, variant(Sprites.TATTERED, column, row), column, row);
        }
        for (Mover m: lane.getFishes()) {
            for (int i = 0; i < m.getLength(); i++) {
                if (lane.getDirection() == 1) {
                    drawTile(g, getFishSprite(i, m.getLength(), lane.getDirection()),
                            m.getStartCell() + i, row);
                } else {
                    drawTileMirrored(g, getFishSprite(i, m.getLength(), lane.getDirection()),
                            m.getStartCell() + i, row);
                }
            }
        }
    }

    private void drawRaftLane(RaftLane lane, Graphics2D g, int row) {
        System.out.println(lane.getDirection());
        for (int column = 0; column < VISIBLE_COLUMNS; column++) {
            drawTile(g, variant(Sprites.CHASM, column, row), column, row);
        }
        for (Mover m: lane.getRafts()) {
            for (int i = 0; i < m.getLength(); i++) {
                if (i == 0) {
                    drawTile(g, Sprites.JELLY_LEFT, m.getStartCell() + i, row);
                } else if (i == m.getLength() - 1) {
                    drawTile(g, Sprites.JELLY_RIGHT, m.getStartCell() + i, row);
                } else {
                    drawTile(g, Sprites.JELLY_MID, m.getStartCell() + i, row);
                }
            }
        }
    }

    private void drawTorpedoLane(TorpedoLane lane, Graphics2D g, int row) {
        System.out.println(lane.getDirection());
        for (int column = 1; column < VISIBLE_COLUMNS; column++) {
            if (((TorpedoLane) lane).isWarning() && (warningCounter / 10) % 2 == 0) {
                drawTile(g, Sprites.TORPEDO_HAZARD, column, row);
            } else if (((TorpedoLane) lane).isFiring()) {
                drawTile(g, Sprites.TORPEDO_BLAST[Math.floorMod(column + warningCounter / 3, 2)],
                        column, row);
            } else {
                drawTile(g, variant(Sprites.TORPEDO_TRACK, column, row), column, row);
            }
        }
        drawTile(g, variant(Sprites.TORPEDO_TRACK, 0, row), 0, row);
        drawTile(g, Sprites.TORPEDO_LAUNCHER, 0, row);

    }

    private BufferedImage variant(BufferedImage[] variants, int column, int row) {
        long randomHash = column * 73856093 ^ row * 19349663;
        return variants[Math.floorMod(randomHash, variants.length)];
    }

    private void drawLanes(Graphics2D g) {
        List<Lane> lanes = world.getLanes();

        for (int row = world.getCameraRow(); row < Math.min(VISIBLE_ROWS + world.getCameraRow(),
                lanes.size()); row++) {
            Lane lane = lanes.get(row);
            switch (lane) {
                case GroundLane ground -> {
                    drawGroundLane(ground, g, row);
                }
                case FishLane fish -> {
                    drawFishLane(fish, g, row);
                }
                case RaftLane raft -> {
                    drawRaftLane(raft, g, row);
                }
                case TorpedoLane torpedo -> {
                    drawTorpedoLane(torpedo, g, row);
                }
                default ->
                    throw new IllegalStateException("Unexpected value: " + lane);
            }
        }
    }

    private void drawCoins(Graphics2D g) {

        for (Position coin: world.getCoins()) {
            drawTile(g, Sprites.COIN, coin.getColumn(), coin.getRow());
        }
    }

    private void drawPlayer(Graphics2D g) {
        Player player = world.getPlayer();
        drawTile(g, Sprites.crab(player.getFacing()), player.getPosition().getColumn(),
                player.getPosition().getRow());
    }

    private void drawGameOver(Graphics2D g) {
        final String gameOverString = "GAME OVER";

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        g.setColor(new Color(255, 0, 0, 255));
        g.drawString(gameOverString, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(gameOverString))
                / 2, (SCREEN_HEIGHT - g.getFontMetrics().getHeight()) / 2
                + g.getFontMetrics().getAscent());

        String scoreText = "Score: " + world.getScore();
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        g.setColor(Color.WHITE);
        int x = (SCREEN_WIDTH - g.getFontMetrics().stringWidth(scoreText)) / 2;
        int y = SCREEN_HEIGHT / 2 + 40;
        g.drawString(scoreText, x, y);
    }

    private void drawPaused(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        g.setColor(Color.WHITE);
        final String pausedString = "PAUSED";
        g.drawString(pausedString, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(pausedString)) / 2,
                SCREEN_HEIGHT / 2 + g.getFontMetrics().getAscent() / 2);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + world.getScore(), 16, 28);

        g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        g.setColor(Color.YELLOW);
        g.drawString("Coins: " + world.getCoinAmount(), 16, 52);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            double scaleX = getWidth() <= 0 ? 1 : (double) getWidth() / SCREEN_WIDTH;
            double scaleY = getHeight() <= 0 ? 1 : (double) getHeight() / SCREEN_HEIGHT;
            g2.scale(scaleX, scaleY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            drawLanes(g2);
            drawCoins(g2);
            drawPlayer(g2);
            drawTint(g2);
            if (world.isGameOver()) {
                drawGameOver(g2);
            } else if (paused) {
                drawPaused(g2);
            }
            drawScore(g2);
        } finally {
            g2.dispose();
        }
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    private void update() {
        world.update();
        if (world.isGameOver() != overlay.isVisible()) {
            HighScore.submit(world.getScore());
            CoinAmount.add(world.getCoinAmount());
            overlay.setVisible(world.isGameOver());
            pauseButton.setVisible(!world.isGameOver());
            if (world.isGameOver()) {
                paused = false;
                pauseOverlay.setVisible(false);
            }
        }
    }

}
