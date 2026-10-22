package scuttle.model;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Data saved with Java preferences so that they are kept between sessions.The list of all skins and
 * which ones the player owns and has equipped. Owned skins are saved as one comma-separated string
 * of ids. The default skin is always owned.
 */
public final class Skins {

    public static final int PRICE = 200; // change this to 0 for fraud :)
    public static final String DEFAULT_ID = "default";

    private static final Preferences PREFS = Preferences.userNodeForPackage(Skins.class);
    private static final String OWNED_KEY = "owned";
    private static final String EQUIPPED_KEY = "equipped";

    public static final List<Entry> ALL = List.of(
            new Entry(DEFAULT_ID, "Default"),
            new Entry("bjorn_snapson", "Bjorn Snapson"),
            new Entry("captain_pinch", "Captain Pinch"),
            new Entry("chef_bisque", "Chef Bisque"),
            new Entry("cool_claws", "Cool Claws"),
            new Entry("ghostie", "Ghostie"),
            new Entry("goldie", "Goldie"),
            new Entry("merlin_claw", "Merlin Claw"),
            new Entry("princess_pearl", "Princess Pearl"),
            new Entry("sir_clawsworth", "Sir Clawsworth"),
            new Entry("snorkel_sam", "Snorkel Sam"));

    private Skins() {
    }

    public static boolean owns(Entry skin) {
        if (skin.isDefault()) {
            return true;
        }
        for (String id: ownedIds()) {
            if (skin.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static String equippedId() {
        String id = PREFS.get(EQUIPPED_KEY, DEFAULT_ID);
        for (Entry skin: ALL) {
            if (skin.id.equals(id) && owns(skin)) {
                return id;
            }
        }
        return DEFAULT_ID;
    }

    public static boolean isEquipped(Entry skin) {
        return skin.id.equals(equippedId());
    }

    public static boolean buy(Entry skin) {
        if (skin.isDefault() || owns(skin) || !CoinAmount.spend(PRICE)) {
            return false;
        }
        String owned = PREFS.get(OWNED_KEY, "");
        PREFS.put(OWNED_KEY, owned.isEmpty() ? skin.id : owned + "," + skin.id);
        return true;
    }

    public static void equip(Entry skin) {
        if (owns(skin)) {
            PREFS.put(EQUIPPED_KEY, skin.id);
        }
    }

    public static void unequip() {
        PREFS.put(EQUIPPED_KEY, DEFAULT_ID);
    }

    private static String[] ownedIds() {
        String owned = PREFS.get(OWNED_KEY, "");
        if (owned.isEmpty()) {
            return new String[0];
        }
        return owned.split(",");
    }

    public static final class Entry {

        private final String id;
        private final String name;

        private Entry(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String id() {
            return id;
        }

        public String name() {
            return name;
        }

        public boolean isDefault() {
            return DEFAULT_ID.equals(id);
        }

    }

}
