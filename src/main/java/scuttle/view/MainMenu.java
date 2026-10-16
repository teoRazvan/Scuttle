package scuttle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import scuttle.model.CoinAmount;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;
import scuttle.model.HighScore;

public class MainMenu extends JPanel {

    private static final int TILE_SIZE = 16 * 3;
    private static final int SCREEN_WIDTH = VISIBLE_COLUMNS * TILE_SIZE;
    private static final int SCREEN_HEIGHT = VISIBLE_ROWS * TILE_SIZE;
    private static final int MARGIN = 24;

    private final BufferedImage scene;
    private final BufferedImage background;
    private final BufferedImage logo;
    private final JPanel buttons;
    private final ImageButton play;
    private final ImageButton options;
    private final ImageButton quit;

    private int crateX;
    private int crateY;
    private int crateWidth;
    private int crateHeight;

    public MainMenu(Runnable onPlay, Runnable onQuit) {
        scene = GuiAssets.menuScene();
        background = GuiAssets.menuBackground();
        logo = GuiAssets.menuLogo();

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);

        buttons = new JPanel(new GridBagLayout());
        buttons.setOpaque(false);

        play = new ImageButton(
                GuiAssets.button(GuiAssets.PLAY, GuiAssets.IDLE),
                GuiAssets.button(GuiAssets.PLAY, GuiAssets.HOVER),
                GuiAssets.button(GuiAssets.PLAY, GuiAssets.PRESSED));
        options = new ImageButton(
                GuiAssets.button(GuiAssets.OPTIONS, GuiAssets.IDLE),
                GuiAssets.button(GuiAssets.OPTIONS, GuiAssets.HOVER),
                GuiAssets.button(GuiAssets.OPTIONS, GuiAssets.PRESSED));
        quit = new ImageButton(
                GuiAssets.button(GuiAssets.QUIT, GuiAssets.IDLE),
                GuiAssets.button(GuiAssets.QUIT, GuiAssets.HOVER),
                GuiAssets.button(GuiAssets.QUIT, GuiAssets.PRESSED));

        play.addActionListener(e -> onPlay.run());
        quit.addActionListener(e -> onQuit.run());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(8, 8, 8, 8);

        constraints.gridy = 0;
        buttons.add(play, constraints);
        constraints.gridy = 1;
        buttons.add(options, constraints);
        constraints.gridy = 2;
        buttons.add(quit, constraints);
        add(buttons);
    }

    @Override
    public void doLayout() {
        layoutCrate();
    }

    private void layoutCrate() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        double scale = Math.min(
                (getWidth() - 2.0 * MARGIN) / background.getWidth(),
                (getHeight() - 2.0 * MARGIN) / background.getHeight());
        crateWidth = (int) Math.round(background.getWidth() * scale);
        crateHeight = (int) Math.round(background.getHeight() * scale);
        crateX = (getWidth() - crateWidth) / 2;
        crateY = (getHeight() - crateHeight) / 2;

        int innerX = crateX + crateWidth / 8;
        int innerY = crateY + crateHeight * 28 / 100;
        int innerWidth = crateWidth * 3 / 4;
        int innerHeight = crateHeight * 62 / 100;
        buttons.setBounds(innerX, innerY, innerWidth, innerHeight);

        double buttonScale = (crateWidth * 0.55) / GuiAssets.BUTTON_WIDTH;
        play.applyScale(buttonScale);
        options.applyScale(buttonScale);
        quit.applyScale(buttonScale);
        buttons.doLayout();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        double sceneScale = Math.max(
                (double) getWidth() / scene.getWidth(),
                (double) getHeight() / scene.getHeight());
        int sceneWidth = (int) Math.round(scene.getWidth() * sceneScale);
        int sceneHeight = (int) Math.round(scene.getHeight() * sceneScale);
        int sceneX = (getWidth() - sceneWidth) / 2;
        int sceneY = (getHeight() - sceneHeight) / 2;
        g2.drawImage(scene, sceneX, sceneY, sceneWidth, sceneHeight, null);
        g2.drawImage(background, crateX, crateY, crateWidth, crateHeight, null);

        int logoWidth = Math.min(140, Math.max(80, getWidth() / 10));
        int logoHeight = logoWidth * logo.getHeight() / logo.getWidth();
        int logoX = getWidth() - logoWidth - 16;
        int logoY = 12;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);

        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        g2.drawString("Best: " + HighScore.getBest(), 100, 28);

        g2.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 22));
        g2.setColor(Color.YELLOW);
        g2.drawString("Coins: " + CoinAmount.getAmount(), 100, 50);
    }
}
