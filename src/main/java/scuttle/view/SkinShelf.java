package scuttle.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import scuttle.model.CoinAmount;
import scuttle.model.Skins;

/**
 * The grid of skin cards inside the shop. The last row is centred when it isn’t full. SkinCard is
 * one card: portrait, name, and a button showing BUY / EQUIP / UNEQUIP / locked.
 */
class SkinShelf extends JPanel {

    private static final int CELL_WIDTH = 54;
    private static final int CELL_HEIGHT = 62;

    private static final int COLUMNS = 4;
    private static final Color NAME_COLOR = new Color(247, 183, 106);
    private static final Color BUTTON_TEXT_COLOR = new Color(24, 22, 36);

    SkinShelf(Runnable onChange) {
        setOpaque(false);
        setLayout(null);
        for (Skins.Entry skin: Skins.ALL) {
            add(new SkinCard(skin, onChange));
        }
    }

    void refresh() {
        for (Component card: getComponents()) {
            ((SkinCard) card).refresh();
        }
    }

    void fit(double unit) {
        int total = getComponentCount();
        for (int i = 0; i < total; i++) {
            SkinCard card = (SkinCard) getComponent(i);
            int col = i % COLUMNS;
            int row = i / COLUMNS;
            int cardsInThisRow = Math.min(COLUMNS, total - row * COLUMNS);
            double shift = (COLUMNS - cardsInThisRow) * CELL_WIDTH / 2.0;
            int x = (int) Math.round((col * CELL_WIDTH + shift) * unit
            );
            int y = (int) Math.round(row * CELL_HEIGHT * unit);
            int width = (int) Math.round(CELL_WIDTH * unit);
            int height = (int) Math.round(CELL_HEIGHT * unit);
            card.setBounds(x, y, width, height);
            card.fit(unit);
        }

    }

    private static final class SkinCard extends JPanel {

        private static final int PORTRAIT_SIZE = 32;
        private static final int PORTRAIT_X = 11;
        private static final int PORTRAIT_Y = 2;
        private static final int NAME_Y = 36;
        private static final int NAME_HEIGHT = 8;
        private static final int BUTTON_WIDTH = 40;
        private static final int BUTTON_HEIGHT = 14;
        private static final int BUTTON_X = 7;
        private static final int BUTTON_Y = 46;

        private static final double NAME_TEXT_SIZE = 3.5;
        private static final double BUTTON_TEXT_SIZE = 5;

        private final Skins.Entry skin;
        private final Runnable onChange;
        private final JLabel portrait = new JLabel("", SwingConstants.CENTER);
        private final JLabel name = new JLabel("", SwingConstants.CENTER);
        private final ImageButton action;

        private SkinCard(Skins.Entry skin, Runnable onChange) {
            action = new ImageButton(Sprites.PLAIN_BUTTON[0], Sprites.PLAIN_BUTTON[1],
                    Sprites.PLAIN_BUTTON[2]);
            name.setText(skin.name());
            this.skin = skin;
            this.onChange = onChange;
            setOpaque(false);
            setLayout(null);
            add(portrait);
            add(name);
            add(action);
            name.setForeground(NAME_COLOR);
            action.setForeground(BUTTON_TEXT_COLOR);
            action.addActionListener(e -> choose());

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
                if (Skins.PRICE > CoinAmount.getAmount()) {
                    action.enableInputMethods(false);
                    action.setForeground(BUTTON_TEXT_COLOR);
                    action.setImages(Sprites.LOCKED_BUTTON, Sprites.LOCKED_BUTTON,
                            Sprites.LOCKED_BUTTON);
                } else {
                    action.enableInputMethods(true);
                    action.setForeground(BUTTON_TEXT_COLOR);
                    action.setImages(Sprites.BUY_BUTTON[0], Sprites.BUY_BUTTON[1],
                            Sprites.BUY_BUTTON[2]);
                }
                action.setText(String.valueOf(Skins.PRICE));
                action.setEnabled(CoinAmount.getAmount() >= Skins.PRICE);
            } else if (Skins.isEquipped(skin)) {
                action.setImages(Sprites.EQUIPPED_BUTTON, Sprites.EQUIPPED_BUTTON,
                        Sprites.EQUIPPED_BUTTON);
                action.setForeground(Color.WHITE);
                action.setText(skin.isDefault() ? "EQUIPPED" : "UNEQUIP");
                action.setEnabled(true);
            } else {
                action.setText("EQUIP");
                action.setForeground(BUTTON_TEXT_COLOR);
                action.setImages(Sprites.PLAIN_BUTTON[0], Sprites.PLAIN_BUTTON[1],
                        Sprites.PLAIN_BUTTON[2]);
                action.setEnabled(true);
            }
        }

        private void fit(double unit) {
            portrait.setIcon(new ImageIcon(GuiAssets.scaleNearest(
                    Sprites.preview(skin.id()), (int) Math.round(PORTRAIT_SIZE * unit),
                    (int) Math.round(PORTRAIT_SIZE
                            * unit))));
            portrait.setBounds((int) Math.round(PORTRAIT_X * unit), (int) Math.round(PORTRAIT_Y
                    * unit), (int) Math.round(PORTRAIT_SIZE * unit),
                    (int) Math.round(PORTRAIT_SIZE * unit));

            Font nameFont = GuiAssets.font((float) (NAME_TEXT_SIZE * unit));
            name.setFont(nameFont);
            name.setBounds(0, (int) Math.round(NAME_Y * unit), (int) Math.round(CELL_WIDTH
                    * unit), (int) Math.round(NAME_HEIGHT * unit));

            Font buttonFont = GuiAssets.font((float) (BUTTON_TEXT_SIZE * unit));

            action.applyScale(unit);
            action.setFont(buttonFont);
            action.setBounds((int) Math.round(BUTTON_X * unit), (int) Math.round(BUTTON_Y
                    * unit), (int) Math.round(BUTTON_WIDTH * unit),
                    (int) Math.round(BUTTON_HEIGHT * unit));
        }

    }

}
