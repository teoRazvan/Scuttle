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
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import static scuttle.model.Constants.*;
import scuttle.model.Direction;
import scuttle.model.Player;
import scuttle.model.World;

/**
 *
 * Game panel is the drawing surface
 *
 * TEMP it runs the game loop and handles input, will be changed later
 */
public class GamePanel extends JPanel implements ActionListener {

    private static final int ORIGINAL_TILE_SIZE = 16; // Sprite size in pixels
    private static final int SCALE = 3;// How much sprites are enlarged

    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; //48x48 pixels tile

    private static final int SCREEN_WIDTH = VISIBLE_COLUMNS * TILE_SIZE; // 768 pixels
    private static final int SCREEN_HEIGHT = VISIBLE_ROWS * TILE_SIZE; // 576 pixels

    private final Timer timer;
    private final Map<Direction, BufferedImage> directionImageMap = new EnumMap<>(Direction.class);
    private final KeyHandler keyH = new KeyHandler();
    private World world = new World();

    public GamePanel() {
        timer = new Timer(TICK_DELAY, this);
        this.getPlayerImage();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
        this.repaint();
    }

    private void getPlayerImage() {

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

    public void startGame() {
        timer.start();
        this.requestFocusInWindow();
    }

    public void update() {
        Direction direction = null;
        if (keyH.upReleased == true) {
            direction = Direction.UP;
            keyH.upReleased = false;
        }

        if (keyH.downReleased == true) {
            direction = Direction.DOWN;
            keyH.downReleased = false;
        }

        if (keyH.leftReleased == true) {
            direction = Direction.LEFT;
            keyH.leftReleased = false;
        }

        if (keyH.rightReleased == true) {
            direction = Direction.RIGHT;
            keyH.rightReleased = false;
        }
        if (direction != null) {
            world.movePlayer(direction);
        }
        world.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        Player player = world.getPlayer();
        BufferedImage image = directionImageMap.get(player.getFacing());
        int posX = player.getPosition().getColumn() * TILE_SIZE;
        int posY = (VISIBLE_ROWS - 1 - player.getPosition().getRow()) * TILE_SIZE;
        //Lane Rendering
        for (int i = 0; i < VISIBLE_ROWS; i++) {
            for (int j = 0; j < VISIBLE_COLUMNS; j++) {
                if (!world.getLanes().get(i).canStepOn(j)) {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(j * TILE_SIZE, (VISIBLE_ROWS - 1 - i) * TILE_SIZE, TILE_SIZE,
                            TILE_SIZE);
                }
                if (world.getLanes().get(i).isDeadly(j)) {
                    g2.setColor(Color.RED);
                    g2.fillRect(j * TILE_SIZE, (VISIBLE_ROWS - 1 - i) * TILE_SIZE, TILE_SIZE,
                            TILE_SIZE);
                }
            }
        }
        g2.drawImage(image, posX, posY, TILE_SIZE, TILE_SIZE, null);
        //TEMP separate method
        if (world.isDead()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
            g2.setColor(new Color(255, 0, 0, 255));

            g2.drawString("GAME OVER", (SCREEN_WIDTH - g2.getFontMetrics().stringWidth(
                    "GAME OVER")) / 2, (SCREEN_HEIGHT - g2.getFontMetrics().getHeight()) / 2
                    + g2.getFontMetrics().getAscent());

        }

    }

}
