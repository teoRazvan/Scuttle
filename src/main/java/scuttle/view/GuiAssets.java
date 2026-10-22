package scuttle.view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Load all images (and the font) once from resources.
 */
final class GuiAssets {

    static final int BUTTON_WIDTH = 356;
    static final int BUTTON_HEIGHT = 140;
    static final int BACK_ARROW_SIZE = 140;
    private static final int BACK_ARROW_GAP = 20;
    private static final int ATLAS_X = 426;
    private static final int ATLAS_Y = 57;

    static final int PLAY = 0;
    static final int QUIT = 2;
    static final int SHOP = 3;
    static final int MENU = 4;
    static final int RESTART = 5;

    static final int IDLE = 0;
    static final int HOVER = 1;
    static final int PRESSED = 2;

    private static BufferedImage atlas;
    private static BufferedImage shopAtlas;
    private static BufferedImage[] backArrows;
    private static final Font PIXEL_FONT = loadFont("/fonts/PressStart2P.ttf");

    private GuiAssets() {
    }

    static BufferedImage menuBackground() {
        return load("/gui/menu_background.png");
    }

    static BufferedImage menuShopBackground() {
        return load("/gui/menu_background_shop.png");
    }

    static BufferedImage menuScene() {
        return load("/gui/background.png");
    }

    static BufferedImage menuLogo() {
        return load("/gui/TUe-logo-scarlet-L.png");
    }

    static Font font(float size) {
        return PIXEL_FONT.deriveFont(size);
    }

    private static Font loadFont(String path) {
        try (InputStream in = GuiAssets.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Missing resource " + path);
            }
            return Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            throw new IllegalStateException("Could not load " + path, e);
        }
    }

    static BufferedImage pauseIcon() {
        BufferedImage source = load("/gui/pause_icon.png");
        BufferedImage tinted = new BufferedImage(
                source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int argb = source.getRGB(x, y);
                int alpha = (argb >>> 24) & 0xFF;
                if (alpha > 0) {
                    tinted.setRGB(x, y, (alpha << 24) | 0x00FFFFFF);
                }
            }
        }
        return tinted;
    }

    static BufferedImage backArrow(int col) {
        if (backArrows == null) {
            BufferedImage sheet = load("/gui/back_arrow_states.png");
            backArrows = new BufferedImage[3];
            for (int i = 0; i < backArrows.length; i++) {
                backArrows[i] = sheet.getSubimage(
                        i * (BACK_ARROW_SIZE + BACK_ARROW_GAP), 0,
                        BACK_ARROW_SIZE, BACK_ARROW_SIZE);
            }
        }
        return backArrows[col];
    }

    static BufferedImage button(int row, int col) {
        BufferedImage source = atlasFor(row);
        return source.getSubimage(
                ATLAS_X + col * BUTTON_WIDTH,
                ATLAS_Y + row * BUTTON_HEIGHT,
                BUTTON_WIDTH,
                BUTTON_HEIGHT);
    }

    private static BufferedImage atlasFor(int row) {
        if (row == SHOP) {
            if (shopAtlas == null) {
                shopAtlas = load("/gui/NEW_BUTTONS_recolored.png");
            }
            return shopAtlas;
        }
        if (atlas == null) {
            atlas = load("/gui/NEW_BUTTONS.png");
        }
        return atlas;
    }

    static BufferedImage scaleNearest(BufferedImage source, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(source, 0, 0, width, height, null);
        g.dispose();
        return scaled;
    }

    static BufferedImage load(String path) {
        try (InputStream in = GuiAssets.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Missing resource " + path);
            }
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + path, e);
        }
    }

}
