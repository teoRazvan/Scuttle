package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;

public class MoverTest {

    private static final int START = 5;
    private static final int LENGTH = 3;
    private static final int LAST_CELL = LANE_WIDTH - HIDDEN_MARGIN - 1;
    private static final int FIRST_CELL = -HIDDEN_MARGIN;

    @Test
    public void coversItsOwnCells() {

        Mover mover = new Mover(START, LENGTH);
        assertAll("Covers",
                () -> assertTrue(mover.covers(START), "Start cell"),
                () -> assertTrue(mover.covers(START + 1), "Midle cell"),
                () -> assertTrue(mover.covers(START + LENGTH - 1), "Last cell")
        );
    }

    @Test
    public void doesNotCoverNeighbouringCells() {
        Mover mover = new Mover(START, LENGTH);
        assertAll("Does not cover",
                () -> assertFalse(mover.covers(START - 1), "Cell just before"),
                () -> assertFalse(mover.covers(START + LENGTH), "Cell just after")
        );
    }

    @Test
    public void shiftingMoves() {
        Mover mover = new Mover(START, LENGTH);
        mover.shift(1);
        assertFalse(mover.covers(5), "Does not cover old start cell");
        assertAll("Covers",
                () -> assertTrue(mover.covers(START + 1), "New start cell"),
                () -> assertTrue(mover.covers(START + 2), "New midle cell"),
                () -> assertTrue(mover.covers(START + LENGTH), "New last cell")
        );
    }

    @Test
    public void wrapsAcrossTheEdge() {
        int length = 2;
        int start = LAST_CELL;
        Mover mover = new Mover(start, length);
        assertAll("Wrapped",
                () -> assertTrue(mover.covers(LAST_CELL), "Right cell"),
                () -> assertTrue(mover.covers(FIRST_CELL), "Left wrap cell")
        );
    }

    @Test
    public void shiftingToTheRightWraps() {
        int length = 1;
        int start = LAST_CELL;
        Mover mover = new Mover(start, length);
        mover.shift(1);

        assertTrue(mover.covers(FIRST_CELL));
    }

    @Test
    public void shiftingToTheLeftWraps() {
        int length = 1;
        int start = FIRST_CELL;
        Mover mover = new Mover(start, length);
        mover.shift(-1);

        assertTrue(mover.covers(LAST_CELL));
    }

}
