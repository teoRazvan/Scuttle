package scuttle.model;

import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;

/**
 * It covers length cells starting at startCell, and it wraps around the lane so it comes back on
 * the other side.
 */
public final class Mover {

    private int startCell;
    private final int length;

    /**
     * @param startCell
     * @param length
     * @throws IllegalArgumentException if the length is below 1 or not smaller than 2 *
     * HIDDEN_MARGIN
     */
    public Mover(int startCell, int length) {
        if (length >= HIDDEN_MARGIN * 2) {
            throw new IllegalArgumentException("length must be less than 2 * HIDDEN_MARGIN");
        }
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least 1");

        }
        this.startCell = wrapHelper(startCell);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public int getStartCell() {
        return startCell;
    }

    /* Lane columns run from -HIDDEN_MARGIN to LANE_WIDTH - HIDDEN_MARGIN - 1, so there are hidden
    cells on both sides of the screen.
    We add the margin to make the range start at 0, wrap it with floorMod,
    and then subtract the margin again.
    floorMod is used instead of % because it never returns a negative number.
    This way, a mover that leaves one side of the lane comes back on the other side, off-screen.
     */
    private static int wrapHelper(int column) {
        return Math.floorMod(column + HIDDEN_MARGIN, LANE_WIDTH) - HIDDEN_MARGIN;
    }

    public void shift(int amount) {
        startCell = wrapHelper(startCell + amount);
    }

    /**
     * Whether any part of the mover is on that column.
     *
     * @param column
     * @return
     */
    public boolean covers(int column) {
        for (int i = 0; i < length; i++) {
            if (wrapHelper(startCell + i) == column) {
                return true;
            }
        }
        return false;
    }

}
