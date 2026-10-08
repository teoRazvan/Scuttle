package scuttle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import static scuttle.model.Constants.TICK_DELAY;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;
import scuttle.model.Direction;
import scuttle.model.Lane;
import scuttle.model.LaneType;
import scuttle.model.Player;
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

    private final Timer timer;
    private final Map<Direction, BufferedImage> directionImageMap = new EnumMap<>(Direction.class);
    private final KeyHandler keyH;
    private World world = new World();

    public GamePanel() {
        timer = new Timer(TICK_DELAY, this);
        keyH = new KeyHandler(this);
        this.loadPlayerSprites();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
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
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
        this.repaint();
    }

    private Color laneColor(LaneType t) {
        switch (t) {
            case LaneType.FISH -> {
                return new Color(40, 110, 180);
            }
            case LaneType.GROUND -> {
                return new Color(230, 205, 150);
            }
            case LaneType.RAFT -> {
                return new Color(90, 160, 210);
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
        return (VISIBLE_ROWS - 1 - row) * TILE_SIZE;
    }

    private int columnToX(int column) {
        return column * TILE_SIZE;
    }

    private void drawLanes(Graphics2D g) {
        List<Lane> lanes = world.getLanes();

        for (int row = 0; row < Math.min(VISIBLE_ROWS, lanes.size()); row++) {
            Lane lane = lanes.get(row);
            g.setColor(laneColor(lane.getType()));
            g.fillRect(columnToX(0), rowToY(row), SCREEN_WIDTH, TILE_SIZE);
            for (int column = 0; column < VISIBLE_COLUMNS; column++) {
                if (!lane.canStepOn(column)) {
                    g.setColor(new Color(240, 110, 110));
                    g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                }
                if (lane.isDeadly(column)) {
                    g.setColor(new Color(255, 150, 40));
                    g.fillRect(columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE);
                }
            }
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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawLanes(g2);
        drawPlayer(g2);
        if (world.isGameOver()) {
            drawGameOver(g2);
        }

    }

    private void update() {
        world.update();
    }

}
