package scuttle.model;

import java.util.prefs.Preferences;

public class CoinAmount {

    private static final Preferences COINS = Preferences.userNodeForPackage(CoinAmount.class);
    
    private CoinAmount(){
        
    }

    public static int getAmount() {
        return COINS.getInt("coins", 0);
    }

    public static void add(int value) {
        COINS.putInt("coins", getAmount() + value);
    }
}
