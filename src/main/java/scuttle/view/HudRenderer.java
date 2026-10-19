package scuttle.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import static scuttle.view.ViewConstants.SCREEN_HEIGHT;
import static scuttle.view.ViewConstants.SCREEN_WIDTH;

public final class HudRenderer {

    void drawGameOver(Graphics2D g, int score) {
        final String gameOverString = "GAME OVER";

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        g.setColor(new Color(255, 0, 0, 255));
        g.drawString(gameOverString, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(gameOverString))
                / 2, (SCREEN_HEIGHT - g.getFontMetrics().getHeight()) / 2
                + g.getFontMetrics().getAscent());

        String scoreText = "Score: " + score;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        g.setColor(Color.WHITE);
        int x = (SCREEN_WIDTH - g.getFontMetrics().stringWidth(scoreText)) / 2;
        int y = SCREEN_HEIGHT / 2 + 40;
        g.drawString(scoreText, x, y);
    }

    void drawPaused(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        g.setColor(Color.WHITE);
        final String pausedString = "PAUSED";
        g.drawString(pausedString, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(pausedString)) / 2,
                SCREEN_HEIGHT / 2 + g.getFontMetrics().getAscent() / 2);
    }

    void drawScore(Graphics2D g, int score, int coins) {
        final int HUD_SCALE = 4;
        g.drawImage(Sprites.HUD_PANEL, 8, 8, 66 * HUD_SCALE, 30 * HUD_SCALE, null);

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 6 * HUD_SCALE));
        g.setColor(new Color(51, 50, 61));
        g.drawString("Score: " + score, 8 + 17 * HUD_SCALE, 6 + 12 * HUD_SCALE);
        g.setColor(new Color(240, 192, 136));
        g.drawString("Score: " + score, 10 + 17 * HUD_SCALE, 8 + 12 * HUD_SCALE);

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 6 * HUD_SCALE));
        g.setColor(new Color(51, 50, 61));
        g.drawString("Coins: " + coins, 8 + 17 * HUD_SCALE, 23 * HUD_SCALE);
        g.setColor(new Color(255, 214, 90));
        g.drawString("Coins: " + coins, 10 + 17 * HUD_SCALE, 2 + 23 * HUD_SCALE);
    }

}
