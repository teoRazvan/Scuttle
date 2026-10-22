package scuttle.model;

import java.util.prefs.Preferences;

/**
 * Data saved with Java preferences so that they are kept between sessions.
 *
 * It remembers the highest score, the furthest row a player has ever reached
 *
 */
public final class HighScore {

    private static final Preferences PREFS = Preferences.userNodeForPackage(HighScore.class);
    private static final String HIGH_SCORE_KEY = "best";

    private HighScore() {

    }

    public static int getBest() {
        return PREFS.getInt(HIGH_SCORE_KEY, 0);
    }

    public static void submit(int score) {
        if (score > getBest()) {
            PREFS.putInt(HIGH_SCORE_KEY, score);
        }
    }

}
