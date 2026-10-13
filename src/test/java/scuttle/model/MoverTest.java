package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;

public class MoverTest {

    @Test
    public void coversItsOwnCells() {
        int length = 3;
        int start = 5;
        Mover mover = new Mover(start, length);
        assertTrue(mover.covers(5) && mover.covers(6) && mover.covers(7));
    }

    @Test
    public void doesNotCoverNeighbouringCells() {
        int length = 3;
        int start = 5;
        Mover mover = new Mover(start, length);
        assertFalse(mover.covers(4) || mover.covers(8));
    }

    @Test
    public void shiftingMoves() {
        int length = 3;
        int start = 5;
        Mover mover = new Mover(start, length);
        mover.shift(1);
        assertFalse(mover.covers(5));
        assertTrue(mover.covers(6) && mover.covers(7) && mover.covers(8));
    }

    @Test
    public void wrapsAcrossTheEdge() {
        int length = 2;
        int start = LANE_WIDTH - HIDDEN_MARGIN - 1;
        Mover mover = new Mover(start, length);

        assertTrue(mover.covers(LANE_WIDTH - HIDDEN_MARGIN - 1) && mover.covers(-HIDDEN_MARGIN));
    }

    @Test
    public void shiftingToTheRightWraps() {
        int length = 1;
        int start = LANE_WIDTH - HIDDEN_MARGIN - 1;
        Mover mover = new Mover(start, length);
        mover.shift(1);

        assertTrue(mover.covers(-HIDDEN_MARGIN));
    }

    @Test
    public void shiftingToTheLeftWraps() {
        int length = 1;
        int start = -HIDDEN_MARGIN;
        Mover mover = new Mover(start, length);
        mover.shift(-1);

        assertTrue(mover.covers(LANE_WIDTH - HIDDEN_MARGIN - 1));
    }

}
