package scuttle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import scuttle.model.CoinAmount;
import static scuttle.view.ViewConstants.SCREEN_HEIGHT;
import static scuttle.view.ViewConstants.SCREEN_WIDTH;

public class ShopMenu extends JPanel {

    private static final int MARGIN = 24;
    private static final int HEADER_HEIGHT = 92;
    private static final int FOOTER_TOP = 305;
    private static final int CRATE_HEIGHT = 336;
    private static final int MARK_X = 18;
    private static final int MARK_Y = 34;
    private static final int MARK_WIDTH = 71;
    private static final int MARK_HEIGHT = 90;
    private static final Color COINS_COLOR = new Color(255, 214, 90);
    private static final Color SHADOW_COLOR = new Color(51, 50, 61);

    private final BufferedImage scene;
    private final BufferedImage background;
    private final BufferedImage logo;
    private final ImageButton back;
    private final SkinShelf shelf;

    private int crateX;
    private int crateY;
    private int crateWidth;
    private int crateHeight;
    private int logoX;
    private int logoY;
    private int logoWidth;
    private int logoHeight;
    private int sceneX;
    private int sceneY;
    private double sceneScale;

    public ShopMenu(Runnable onMenu) {
        scene = GuiAssets.menuScene();
        background = GuiAssets.menuShopBackground();
        logo = GuiAssets.menuLogo();

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);

        back = new ImageButton(
                GuiAssets.backArrow(GuiAssets.IDLE),
                GuiAssets.backArrow(GuiAssets.HOVER),
                GuiAssets.backArrow(GuiAssets.PRESSED));
        back.addActionListener(e -> onMenu.run());
        shelf = new SkinShelf(this::refreshShop);
        add(back);
        add(shelf);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && shelf != null) {
            refreshShop();
        }
    }

    private void refreshShop() {
        shelf.refresh();
        repaint();
    }

    @Override
    public void doLayout() {
        layoutScene();
        layoutCrate();
        layoutLogo();
        layoutBack();
        layoutShelf();
    }

    private void layoutScene() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        sceneScale = Math.max(
                (double) getWidth() / scene.getWidth(),
                (double) getHeight() / scene.getHeight());
        int sceneWidth = (int) Math.round(scene.getWidth() * sceneScale);
        int sceneHeight = (int) Math.round(scene.getHeight() * sceneScale);
        sceneX = (getWidth() - sceneWidth) / 2;
        sceneY = (getHeight() - sceneHeight) / 2;
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
    }

    private void layoutLogo() {
        logoWidth = Math.min(140, Math.max(80, getWidth() / 10));
        logoHeight = logoWidth * logo.getHeight() / logo.getWidth();
        logoX = getWidth() - logoWidth - 16;
        logoY = 28;
    }

    private void layoutBack() {
        int markX = (int) Math.round(sceneX + MARK_X * sceneScale);
        int markY = (int) Math.round(sceneY + MARK_Y * sceneScale);
        int markW = (int) Math.round(MARK_WIDTH * sceneScale);
        int markH = (int) Math.round(MARK_HEIGHT * sceneScale);
        int size = Math.max(markW, markH);
        double buttonScale = (double) size / GuiAssets.BACK_ARROW_SIZE;
        back.applyScale(buttonScale);
        size = (int) Math.round(GuiAssets.BACK_ARROW_SIZE * buttonScale);
        back.setBounds(markX + (markW - size) / 2, markY + (markH - size) / 2, size, size);
    }

    private void layoutShelf() {
        if (crateWidth <= 0) {
            return;
        }
        int headerBottom = crateY + crateHeight * HEADER_HEIGHT / CRATE_HEIGHT;
        int footerTop = crateY + crateHeight * FOOTER_TOP / CRATE_HEIGHT;
        int fontSize = Math.max(12, crateWidth / 18);
        int top = headerBottom + fontSize + crateHeight / 16;
        int innerX = crateX + crateWidth / 8;
        int innerWidth = crateWidth * 3 / 4;
        shelf.setBounds(innerX, top, innerWidth, Math.max(1, footerTop - top));
        shelf.fit();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        layoutScene();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int sceneWidth = (int) Math.round(scene.getWidth() * sceneScale);
        int sceneHeight = (int) Math.round(scene.getHeight() * sceneScale);
        g2.drawImage(scene, sceneX, sceneY, sceneWidth, sceneHeight, null);
        g2.drawImage(background, crateX, crateY, crateWidth, crateHeight, null);

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);

        drawCoins(g2);
    }

    private void drawCoins(Graphics2D g) {
        String text = "Coins: " + CoinAmount.getAmount();
        int fontSize = Math.max(12, crateWidth / 18);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
        int textWidth = g.getFontMetrics().stringWidth(text);
        int iconSize = g.getFontMetrics().getAscent();
        int gap = Math.max(4, iconSize / 4);
        int rowWidth = iconSize + gap + textWidth;
        int x = crateX + (crateWidth - rowWidth) / 2;
        int headerBottom = crateY + crateHeight * HEADER_HEIGHT / CRATE_HEIGHT;
        int y = headerBottom + g.getFontMetrics().getAscent() + crateHeight / 40;
        int iconY = y - iconSize;

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(Sprites.COIN, x, iconY, iconSize, iconSize, null);

        int textX = x + iconSize + gap;
        g.setColor(SHADOW_COLOR);
        g.drawString(text, textX + 2, y + 2);
        g.setColor(COINS_COLOR);
        g.drawString(text, textX, y);
    }

}
