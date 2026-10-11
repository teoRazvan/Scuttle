package scuttle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;

public class MainMenu extends JPanel {

    private static final int TILE_SIZE = 16 * 3;
    private static final int SCREEN_WIDTH = VISIBLE_COLUMNS * TILE_SIZE;
    private static final int SCREEN_HEIGHT = VISIBLE_ROWS * TILE_SIZE;
    private static final int MARGIN = 24;

    private final BufferedImage background;
    private final int crateX;
    private final int crateY;
    private final int crateWidth;
    private final int crateHeight;

    public MainMenu(Runnable onPlay, Runnable onQuit) {
        background = GuiAssets.menuBackground();

        double scale = Math.min(
                (SCREEN_WIDTH - 2.0 * MARGIN) / background.getWidth(),
                (SCREEN_HEIGHT - 2.0 * MARGIN) / background.getHeight());
        crateWidth = (int) Math.round(background.getWidth() * scale);
        crateHeight = (int) Math.round(background.getHeight() * scale);
        crateX = (SCREEN_WIDTH - crateWidth) / 2;
        crateY = (SCREEN_HEIGHT - crateHeight) / 2;

        int buttonWidth = (int) Math.round(GuiAssets.BUTTON_WIDTH * scale);
        int buttonHeight = (int) Math.round(GuiAssets.BUTTON_HEIGHT * scale);

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setOpaque(false);
        int innerX = crateX + crateWidth / 8;
        int innerY = crateY + crateHeight * 28 / 100;
        int innerWidth = crateWidth * 3 / 4;
        int innerHeight = crateHeight * 62 / 100;
        buttons.setBounds(innerX, innerY, innerWidth, innerHeight);

        ImageButton play = new ImageButton(
                scaledButton(GuiAssets.PLAY, GuiAssets.IDLE, buttonWidth, buttonHeight),
                scaledButton(GuiAssets.PLAY, GuiAssets.HOVER, buttonWidth, buttonHeight),
                scaledButton(GuiAssets.PLAY, GuiAssets.PRESSED, buttonWidth, buttonHeight));
        ImageButton quit = new ImageButton(
                scaledButton(GuiAssets.QUIT, GuiAssets.IDLE, buttonWidth, buttonHeight),
                scaledButton(GuiAssets.QUIT, GuiAssets.HOVER, buttonWidth, buttonHeight),
                scaledButton(GuiAssets.QUIT, GuiAssets.PRESSED, buttonWidth, buttonHeight));

        play.addActionListener(e -> onPlay.run());
        quit.addActionListener(e -> onQuit.run());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(8, 8, 8, 8);

        constraints.gridy = 0;
        buttons.add(play, constraints);
        constraints.gridy = 1;
        buttons.add(quit, constraints);
        add(buttons);
    }

    private static BufferedImage scaledButton(int row, int col, int width, int height) {
        return GuiAssets.scaleNearest(GuiAssets.button(row, col), width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(background, crateX, crateY, crateWidth, crateHeight, null);
    }
}
