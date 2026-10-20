package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public class GroundLaneTest {

    private static final int FREE_COLUMN = 1;
    private static final int CORAL_COLUMN = 3;
    private static final int CHANGED_COLUMN = 5;

    private Lane groundLane;
    private CellType[] cellArray;

    @BeforeEach
    public void setup() {
        cellArray = new CellType[VISIBLE_COLLUMNS];
        for (int i = 0; i < VISIBLE_COLLUMNS; i++) {
            cellArray[i] = CellType.FREE;
        }
        cellArray[CORAL_COLUMN] = CellType.CORAL;
        groundLane = new GroundLane(cellArray);
    }

    @Test
    public void canStepOnFree() {
        assertTrue(groundLane.canStepOn(FREE_COLUMN));
    }

    @Test
    public void canNotStepOnCoral() {
        assertFalse(groundLane.canStepOn(CORAL_COLUMN));
    }

    @Test
    public void isNeverDeadly() {
        for (int i = 0; i < VISIBLE_COLLUMNS; i++) {
            assertFalse(groundLane.isDeadly(i), "Column " + i);
        }
    }

    @Test
    public void typeIsGround() {
        assertEquals(LaneType.GROUND, groundLane.getType());
    }

    @Test
    public void laneDoesNotChangeWithArray() {
        cellArray[CHANGED_COLUMN] = CellType.CORAL;

        assertTrue(groundLane.canStepOn(CHANGED_COLUMN));
    }

    @Test
    public void driftIsZero() {
        assertEquals(0, groundLane.drift(1));
    }

}
