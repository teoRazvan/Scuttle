package scuttle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import scuttle.model.CoinAmount;
import static scuttle.model.Constants.TICK_DELAY;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;
import scuttle.model.Direction;
import scuttle.model.HighScore;
import scuttle.model.Lane;
import scuttle.model.LaneType;
import scuttle.model.Player;
import scuttle.model.Position;
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

    private int warningCounter = 0;
    private final Timer timer;
    private final Map<Direction, BufferedImage> directionImageMap = new EnumMap<>(Direction.class);
    private final KeyHandler keyH;
    private final JPanel overlay;
    private final JButton restartButton;
    private final JButton menuButton;
    private final Runnable onMenu;
    private World world = new World();

    public GamePanel(Runnable onMenu) {
        this.onMenu = onMenu;
        timer = new Timer(TICK_DELAY, this);
        keyH = new KeyHandler(this);
        this.loadPlayerSprites();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setLayout(new BorderLayout());

        overlay = new JPanel(new GridBagLayout());
        overlay.setOpaque(false);
        overlay.setVisible(false);

        restartButton = new JButton("RESTART");
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> restart());

        menuButton = new JButton("MENU");
        menuButton.setFocusable(false);
        menuButton.addActionListener(e -> returnToMenu());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(167, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridy = 0;
        overlay.add(restartButton, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(8, 8, 8, 8);
        overlay.add(menuButton, constraints);
        this.add(overlay, BorderLayout.CENTER);
    }

    public void startGame() {
        timer.start();
        this.requestFocusInWindow();
    }

    private void loadPlayerSprites() {

        try {

            BufferedImage up = ImageIO.read(
                    getClass().getResourceAsStream("/player/Scuttle_up1.png"));
            BufferedImage down = ImageIO.read(getClass().getResourceAsStream(
                    "/player/Scuttle_up1.png"));
            BufferedImage left = ImageIO.read(getClass().getResourceAsStream(
                    "/player/Scuttle_up1.png"));
            BufferedImage right = ImageIO.read(getClass().getResourceAsStream(
                    "/player/Scuttle_up1.png"));
            directionImageMap.put(Direction.UP, up);
            directionImageMap.put(Direction.DOWN, down);
            directionImageMap.put(Direction.RIGHT, right);
            directionImageMap.put(Direction.LEFT, left);

            // change these for all 8 sprites
        } catch (IOException e) {

            System.getLogger(GamePanel.class.getName()).log(System.Logger.Level.ERROR, (String) null,
                    e);

        } catch (IllegalArgumentException e) {

            System.err.println("Could not find one or more image resources in /player/");

            e.printStackTrace();

        }
    }

    @Override
    public void move(Direction direction) {
        world.movePlayer(direction);
    }

    @Override
    public void restart() {
        if (world.isGameOver()) {
            world = new World();
            overlay.setVisible(false);
            this.requestFocusInWindow();
        }
    }

    private void returnToMenu() {
        timer.stop();
        world = new World();
        overlay.setVisible(false);
        onMenu.run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.warningCounter++;
        this.update();
        this.repaint();
    }

    private Color laneColor(LaneType t) {
        switch (t) {
            case LaneType.FISH -> {
                return new Color(90, 160, 210);
            }
            case LaneType.GROUND -> {
                return new Color(230, 205, 150);
            }
            case LaneType.RAFT -> {
                return new Color(40, 110, 180);
            }
            case LaneType.TORPEDO -> {
                return new Color(20, 40, 90);
            }
            default -> {
                return null;
            }
        }
    }

    private int rowToY(int row) {
        return (VISIBLE_ROWS - 1 - (row - world.getCameraRow())) * TILE_SIZE;
    }

    private int columnToX(int column) {
        return column * TILE_SIZE;
    }

    private void drawLanes(Graphics2D g) {
        List<Lane> lanes = world.getLanes();

        for (int row = world.getCameraRow(); row < Math.min(VISIBLE_ROWS + world.getCameraRow(),
                lanes.size()); row++) {
            Lane lane = lanes.get(row);
            g.setColor(laneColor(lane.getType()));
            g.fillRect(columnToX(0), rowToY(row), SCREEN_WIDTH, TILE_SIZE);
            for (int column = 0; column < VISIBLE_COLUMNS; column++) {
                if (!lane.canStepOn(column)) {
                    g.setColor(new Color(240, 110, 110));
                    g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                }
                if (lane.getType() == LaneType.FISH) {
                    if (lane.isDeadly(column)) {
                        g.setColor(new Color(255, 150, 40));
                        g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                    }
                } else if (lane.getType() == LaneType.RAFT) {
                    if (!lane.isDeadly(column)) {
                        g.setColor(new Color(140, 90, 50));
                        g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                    }
                }
                if (lane instanceof TorpedoLane) {
                    if (((TorpedoLane) lane).isWarning() && (warningCounter / 10) % 2 == 0) {
                        g.setColor(new Color(255, 40, 40, 110));
                        g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                    } else if (((TorpedoLane) lane).isFiring()) {
                        g.setColor(new Color(60, 60, 70));
                        g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                    } else {
                        g.setColor(new Color(20, 40, 90));
                        g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                    }
                }

            }
        }
    }

    private void drawCoins(Graphics2D g) {
        g.setColor(new Color(255, 200, 40));
        int pad = TILE_SIZE / 4;
        int size = TILE_SIZE / 2;
        for (Position coin: world.getCoins()) {
            g.fillOval(columnToX(coin.getColumn()) + pad, rowToY(coin.getRow()) + pad, size, size);
        }
    }

    private void drawPlayer(Graphics2D g) {

        Player player = world.getPlayer();
        BufferedImage image = directionImageMap.get(player.getFacing());
        int posX = columnToX(player.getPosition().getColumn());
        int posY = rowToY(player.getPosition().getRow());
        g.drawImage(image, posX, posY, TILE_SIZE, TILE_SIZE, null);
    }

    private void drawGameOver(Graphics2D g) {
        final String gameOverString = "GAME OVER";

        //TEMP
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
        int y = SCREEN_HEIGHT / 2 + 40;  // below the centered title
        g.drawString(scoreText, x, y);

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
        Graphics2D g2 = (Graphics2D) g;
        drawLanes(g2);
        drawCoins(g2);
        drawPlayer(g2);
        if (world.isGameOver()) {
            drawGameOver(g2);
        }
        drawScore(g2);

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
        }
    }

}
