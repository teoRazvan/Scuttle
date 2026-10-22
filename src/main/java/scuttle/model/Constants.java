package scuttle.model;

/**
 * Fixed sizes for the grid and the timing, shared by the model and the view.
 */
public final class Constants {

    public static final int VISIBLE_COLUMNS = 28;// Number of columns in the game grid
    public static final int VISIBLE_ROWS = 18;// Number of rows in the game grid
    // Collumns hidden on each side, 2x must be greater than or equals to the longest mover
    public static final int HIDDEN_MARGIN = 4;
    // Number of cells a lane has, slightly more than what is displayed to help with rendering
    public static final int LANE_WIDTH = VISIBLE_COLUMNS + 2 * HIDDEN_MARGIN;
    // The number of lanes ahead of the screen to account for lag in generation
    public static final int LANES_AHEAD = 24;
    // Updates happen once after each delay to ensure a stable FPS
    public static final int TICK_DELAY = 16;
    // How many rows below the crab the camera’s bottom edge stays
    public static final int CAMERA_OFFSET = 4;

    private Constants() {
    }

}
