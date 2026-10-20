package scuttle.model;

import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;

public final class Mover {

    private int startCell;
    private final int length;

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

    private static int wrapHelper(int column) {
        return Math.floorMod(column + HIDDEN_MARGIN, LANE_WIDTH) - HIDDEN_MARGIN;
    }

    public void shift(int amount) {
        startCell = wrapHelper(startCell + amount);
    }

    public boolean covers(int column) {
        for (int i = 0; i < length; i++) {
            if (wrapHelper(startCell + i) == column) {
                return true;
            }
        }
        return false;
    }

}
