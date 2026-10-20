package scuttle.model;

import java.util.prefs.Preferences;

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

}
