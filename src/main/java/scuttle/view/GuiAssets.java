package scuttle.view;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

final class GuiAssets {

    static final int BUTTON_WIDTH = 356;
    static final int BUTTON_HEIGHT = 140;
    private static final int ATLAS_X = 426;
    private static final int ATLAS_Y = 57;

    static final int PLAY = 0;
    static final int OPTIONS = 1;
    static final int QUIT = 2;
    static final int SHOP = 3;
    static final int MENU = 4;
    static final int RESTART = 5;

    static final int IDLE = 0;
    static final int HOVER = 1;
    static final int PRESSED = 2;

    private static BufferedImage atlas;

    private GuiAssets() {
    }

    static BufferedImage menuBackground() {
        return load("/gui/menu_background.png");
    }

    static BufferedImage menuScene() {
        return load("/gui/background.png");
    }

    static BufferedImage menuLogo() {
        return load("/gui/TUe-logo-scarlet-L.png");
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

    static BufferedImage button(int row, int col) {
        if (atlas == null) {
            atlas = load("/gui/NEW_BUTTONS.png");
        }
        return atlas.getSubimage(
                ATLAS_X + col * BUTTON_WIDTH,
                ATLAS_Y + row * BUTTON_HEIGHT,
                BUTTON_WIDTH,
                BUTTON_HEIGHT);
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
