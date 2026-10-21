package scuttle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import scuttle.model.CoinAmount;
import scuttle.model.Skins;

class SkinShelf extends JPanel {

    private static final int COLUMNS = 4;
    private static final Color NAME_COLOR = new Color(51, 50, 61);
    private static final Color BUTTON_COLOR = new Color(183, 115, 87);

    SkinShelf(Runnable onChange) {
        setOpaque(false);
        setLayout(new GridLayout(0, COLUMNS, 4, 4));
        for (Skins.Entry skin: Skins.ALL) {
            add(new SkinCard(skin, onChange));
        }
    }

    void refresh() {
        for (Component card: getComponents()) {
            ((SkinCard) card).refresh();
        }
    }

    void fit() {
        int rows = (getComponentCount() + COLUMNS - 1) / COLUMNS;
        int cellHeight = Math.max(1, getHeight() / Math.max(1, rows));
        int cellWidth = Math.max(1, getWidth() / COLUMNS);
        int portrait = Math.max(16, (int) (cellHeight * 0.38));
        int fontSize = Math.max(9, Math.min(14, cellWidth / 9));
        for (Component card: getComponents()) {
            ((SkinCard) card).fit(portrait, fontSize, Math.max(20, cellWidth - 8));
        }
    }

    private static final class SkinCard extends JPanel {

        private final Skins.Entry skin;
        private final Runnable onChange;
        private final JLabel portrait = new JLabel("", SwingConstants.CENTER);
        private final JLabel name = new JLabel("", SwingConstants.CENTER);
        private final JButton action = new JButton();

        private SkinCard(Skins.Entry skin, Runnable onChange) {
            this.skin = skin;
            this.onChange = onChange;
            setOpaque(false);
            setLayout(new BorderLayout(0, 0));

            name.setForeground(NAME_COLOR);
            action.setFocusable(false);
            action.setMargin(new Insets(0, 2, 0, 2));
            action.setBackground(BUTTON_COLOR);
            action.setForeground(NAME_COLOR);
            action.addActionListener(e -> choose());

            JPanel caption = new JPanel(new GridLayout(2, 1));
            caption.setOpaque(false);
            caption.add(name);
            caption.add(action);

            add(portrait, BorderLayout.CENTER);
            add(caption, BorderLayout.SOUTH);
            refresh();
        }

        private void choose() {
            if (!Skins.owns(skin)) {
                Skins.buy(skin);
            } else if (Skins.isEquipped(skin)) {
                if (!skin.isDefault()) {
                    Skins.unequip();
                }
            } else {
                Skins.equip(skin);
            }
            onChange.run();
        }

        private void refresh() {
            if (!Skins.owns(skin)) {
                action.setText(String.valueOf(Skins.PRICE));
                action.setEnabled(CoinAmount.getAmount() >= Skins.PRICE);
            } else if (Skins.isEquipped(skin)) {
                action.setText(skin.isDefault() ? "EQUIPPED" : "UNEQUIP");
                action.setEnabled(true);
            } else {
                action.setText("EQUIP");
                action.setEnabled(true);
            }
        }

        private void fit(int portraitSize, int fontSize, int nameWidth) {
            portrait.setIcon(new ImageIcon(GuiAssets.scaleNearest(
                    Sprites.preview(skin.id()), portraitSize, portraitSize)));
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
            name.setFont(font);
            name.setText("<html><body style='text-align:center; width:" + nameWidth
                    + "px'>" + skin.name() + "</body></html>");
            action.setFont(font);
        }
    }

}
