package scuttle.model;

import java.util.prefs.Preferences;

public class HighScore {

    private static final Preferences PREFS = Preferences.userNodeForPackage(HighScore.class);

    private HighScore() {

    }

    public static int getBest() {
        return PREFS.getInt("best", 0);
    }

    public static void submit(int score) {
        if (score > getBest()) {
            PREFS.putInt("best", score);
        }
    }
}
