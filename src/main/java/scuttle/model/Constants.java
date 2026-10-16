package scuttle.model;

public final class Constants {

    private Constants() {
    }

    public static final int VISIBLE_COLUMNS = 28;//Number of columns in the game grid TBD
    public static final int VISIBLE_ROWS = 18;//Number of rows in the game grid TBD
    //collumns hidden on each side must be greater then or equals to the longest mover
    public static final int HIDDEN_MARGIN = 4;
    // Number of cells a lane has, slightly more than what is displayed to help with rendering
    public static final int LANE_WIDTH = VISIBLE_COLUMNS + 2 * HIDDEN_MARGIN;
    //The number of lanes kept behind the screen to for the player to be able to backtrack
    public static final int LANES_BEHIND = 10;
    //The number of lanes ahead of the screen to account for lag in generation
    public static final int LANES_AHEAD = 24;
    //Updates happen once after each delay to ensure a stable FPS
    public static final int TICK_DELAY = 16;
    // Chance that a newly generated ground lane gets a coin
    public static final int COIN_GROUND_CHANCE = 50;

    public static final int CAMERA_OFFSET = 4;

}
