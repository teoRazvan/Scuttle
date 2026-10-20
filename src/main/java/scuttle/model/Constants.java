package scuttle.model;

public final class Constants {

    public static final int VISIBLE_COLLUMNS = 28;// Number of collumns in the game grid
    public static final int VISIBLE_ROWS = 18;// Number of rows in the game grid
    // Collumns hidden on each side must be greater than or equals to the longest mover
    public static final int HIDDEN_MARGIN = 4;
    // Number of cells a lane has, slightly more than what is displayed to help with rendering
    public static final int LANE_WIDTH = VISIBLE_COLLUMNS + 2 * HIDDEN_MARGIN;
    // The number of lanes ahead of the screen to account for lag in generation
    public static final int LANES_AHEAD = 24;
    // Updates happen once after each delay to ensure a stable FPS
    public static final int TICK_DELAY = 16;
    // How many rows below the crab the camera’s bottom edge stays
    public static final int CAMERA_OFFSET = 4;

    private Constants() {
    }

}
