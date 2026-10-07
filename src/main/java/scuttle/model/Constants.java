package scuttle.model;

public class Constants {

    public static final int VISIBLE_COLUMNS = 16;//Number of collumns in the game grid TBD
    public static final int VISIBLE_ROWS = 12;//Number of rows in the game grid TBD
    public static final int HIDDEN_MARGIN = 2;
    // Number of cells a lane has, slightly more then what is displayed to help with rendering
    public static final int LANE_WIDTH = VISIBLE_COLUMNS + 2 * HIDDEN_MARGIN;
    //The number of lanes kept behind the screen to for the player to be able to backtrack
    public static final int LANES_BEHIND = 10;
    //The number of lanes ahead of the screen to account for lag in generation
    public static final int LANES_AHEAD = 24;
    //Updates happenm once after each delay to ensure a stable FPS
    public static final int TICK_DELAY = 16;

}
