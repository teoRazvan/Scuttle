package scuttle.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class ImageButton extends JButton {

    ImageButton(BufferedImage idle, BufferedImage hover, BufferedImage pressed) {
        setIcon(new ImageIcon(idle));
        setRolloverIcon(new ImageIcon(hover));
        setPressedIcon(new ImageIcon(pressed));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setFocusable(false);
        Dimension size = new Dimension(idle.getWidth(), idle.getHeight());
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
    }
}
