package scuttle.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import static scuttle.view.ViewConstants.SCREEN_HEIGHT;
import static scuttle.view.ViewConstants.SCREEN_WIDTH;

public final class HudRenderer {

    private static final int HUD_SCALE = 4;
    private static final int HUD_X = 8;
    private static final int HUD_Y = 30;
    private static final int PANEL_WIDTH = 66;
    private static final int PANEL_HEIGHT = 30;
    private static final int TEXT_COLUMN = 17;
    private static final int SCORE_BASELINE = 12;
    private static final int COINS_BASELINE = 23;
    private static final int SHADOW_OFFSET = 2;
    private static final int SCORE_LINE_OFFSET = 40;

    private static final Color SHADOW_COLOR = new Color(51, 50, 61);
    private static final Color SCORE_COLOR = new Color(240, 192, 136);
    private static final Color COINS_COLOR = new Color(255, 214, 90);
    private static final Color GAME_OVER_SHADE = new Color(0, 0, 0, 150);
    private static final Color PAUSE_SHADE = new Color(0, 0, 0, 90);
    private static final Color GAME_OVER_COLOR = new Color(255, 0, 0, 255);

    private static final Font HUD_FONT = GuiAssets.font(4 * HUD_SCALE);
    private static final Font TITLE_FONT = GuiAssets.font(48);
    private static final Font SUBTITLE_FONT = GuiAssets.font(24);

    private static final String GAME_OVER = "GAME OVER";
    private static final String PAUSED = "PAUSED";

    private void drawCentered(Graphics2D g, String text, int y) {
        g.drawString(text, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(text)) / 2, y);
    }

    void drawGameOver(Graphics2D g, int score) {

        g.setColor(GAME_OVER_SHADE);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setFont(TITLE_FONT);
        g.setColor(GAME_OVER_COLOR);
        drawCentered(g, GAME_OVER, (SCREEN_HEIGHT - g.getFontMetrics().getHeight()) / 2
                + g.getFontMetrics().getAscent());

        String scoreText = "Score: " + score;
        int y = SCREEN_HEIGHT / 2 + SCORE_LINE_OFFSET;

        g.setFont(SUBTITLE_FONT);
        g.setColor(Color.WHITE);
        drawCentered(g, scoreText, y);
    }

    void drawPaused(Graphics2D g) {
        g.setColor(PAUSE_SHADE);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setFont(TITLE_FONT);
        g.setColor(Color.WHITE);
        drawCentered(g, PAUSED, SCREEN_HEIGHT / 2 + g.getFontMetrics().getAscent() / 2);
    }

    private void drawShadowedText(Graphics2D g, String text, int x, int y, Color color) {
        g.setFont(HUD_FONT);
        g.setColor(SHADOW_COLOR);
        g.drawString(text, x + SHADOW_OFFSET, y + SHADOW_OFFSET);
        g.setColor(color);
        g.drawString(text, x, y);

    }

    void drawScore(Graphics2D g, int score, int coins) {
        drawBanner(g, "Score: " + score, "Coins: " + coins);
    }

    void drawBest(Graphics2D g, int best, int coins) {
        drawBanner(g, "Best: " + best, "Coins: " + coins);
    }

    private void drawBanner(Graphics2D g, String top, String bottom) {
        g.drawImage(Sprites.HUD_PANEL, HUD_X, HUD_Y, PANEL_WIDTH * HUD_SCALE, PANEL_HEIGHT
                * HUD_SCALE, null);

        drawShadowedText(g, top, HUD_X + TEXT_COLUMN * HUD_SCALE, HUD_Y
                + SCORE_BASELINE * HUD_SCALE, SCORE_COLOR);
        drawShadowedText(g, bottom, HUD_X + TEXT_COLUMN * HUD_SCALE, HUD_Y
                + COINS_BASELINE * HUD_SCALE, COINS_COLOR);
    }

}
