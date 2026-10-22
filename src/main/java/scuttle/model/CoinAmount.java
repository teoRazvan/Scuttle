package scuttle.model;

import java.util.prefs.Preferences;

/**
 * Data saved with Java preferences so that they are kept between sessions.
 *
 * It remembers the amount of coins the player has.
 */
public final class CoinAmount {

    private static final Preferences PREFS = Preferences.userNodeForPackage(CoinAmount.class);
    private static final String COIN_KEY = "coins";

    private CoinAmount() {
    }

    public static int getAmount() {
        return PREFS.getInt(COIN_KEY, 0);
    }

    public static void add(int value) {
        PREFS.putInt(COIN_KEY, getAmount() + value);
    }

    public static boolean spend(int value) {
        if (value <= 0 || getAmount() < value) {
            return false;
        }
        PREFS.putInt(COIN_KEY, getAmount() - value);
        return true;
    }

}
