package scuttle.view;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

final class GuiAssets {

    static final int BUTTON_WIDTH = 140;
    static final int BUTTON_HEIGHT = 56;

    static final int PLAY = 0;
    static final int OPTIONS = 1; // Not used rn
    static final int QUIT = 2;

    static final int IDLE = 0;
    static final int HOVER = 1;
    static final int PRESSED = 2;

    private GuiAssets() {
    }

    static BufferedImage menuBackground() {
        return load("/gui/menu_background.png");
    }

    static BufferedImage button(int row, int col) {
        BufferedImage atlas = load("/gui/button_atlas.png");
        return atlas.getSubimage(
                col * BUTTON_WIDTH,
                row * BUTTON_HEIGHT,
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

    private static BufferedImage load(String path) {
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
