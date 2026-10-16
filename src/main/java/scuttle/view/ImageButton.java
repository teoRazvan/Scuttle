package scuttle.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class ImageButton extends JButton {

    private final BufferedImage idle;
    private final BufferedImage hover;
    private final BufferedImage pressed;
    private int scaledWidth;
    private int scaledHeight;

    ImageButton(BufferedImage idle, BufferedImage hover, BufferedImage pressed) {
        this.idle = idle;
        this.hover = hover;
        this.pressed = pressed;
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setFocusable(false);
        applyScale(1);
    }

    void applyScale(double scale) {
        int width = Math.max(1, (int) Math.round(idle.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(idle.getHeight() * scale));
        if (width == scaledWidth && height == scaledHeight) {
            return;
        }
        scaledWidth = width;
        scaledHeight = height;
        setIcon(new ImageIcon(GuiAssets.scaleNearest(idle, width, height)));
        setRolloverIcon(new ImageIcon(GuiAssets.scaleNearest(hover, width, height)));
        setPressedIcon(new ImageIcon(GuiAssets.scaleNearest(pressed, width, height)));
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }
}
