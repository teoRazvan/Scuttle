package scuttle.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * A JButton drawn with pixel-art images for its idle, hover and pressed states, scaled without
 * blurring.
 */
class ImageButton extends JButton {

    private BufferedImage idle;
    private BufferedImage hover;
    private BufferedImage pressed;
    private int scaledWidth;
    private int scaledHeight;
    private double scale = 1;

    ImageButton(BufferedImage idle, BufferedImage hover, BufferedImage pressed) {
        this.idle = idle;
        this.hover = hover;
        this.pressed = pressed;
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder());
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setFocusable(false);
        applyScale(1);
    }

    void setImages(BufferedImage idle, BufferedImage hover, BufferedImage pressed) {
        this.idle = idle;
        this.hover = hover;
        this.pressed = pressed;
        scaledWidth = 0;
        applyScale(scale);
    }

    static ImageButton fromAssets(int buttonRow) {
        return new ImageButton(GuiAssets.button(buttonRow, GuiAssets.IDLE),
                GuiAssets.button(buttonRow, GuiAssets.HOVER),
                GuiAssets.button(buttonRow, GuiAssets.PRESSED));
    }

    void applyScale(double scale) {
        scale = scale;
        int width = Math.max(1, (int) Math.round(idle.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(idle.getHeight() * scale));
        if (width == scaledWidth && height == scaledHeight) {
            return;
        }
        scaledWidth = width;
        scaledHeight = height;
        setDisabledIcon(new ImageIcon(GuiAssets.scaleNearest(idle, width, height)));
        setIcon(new ImageIcon(GuiAssets.scaleNearest(idle, width, height)));
        setRolloverIcon(new ImageIcon(GuiAssets.scaleNearest(hover, width, height)));
        setPressedIcon(new ImageIcon(GuiAssets.scaleNearest(pressed, width, height)));
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

}
