package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public class GroundLaneTest {

    private Lane groundLane;
    private CellType[] cellArray;

    @BeforeEach
    public void setup() {
        cellArray = new CellType[VISIBLE_COLLUMNS];
        for (int i = 0; i < VISIBLE_COLLUMNS; i++) {
            cellArray[i] = CellType.FREE;
        }
        cellArray[3] = CellType.CORAL;
        groundLane = new GroundLane(cellArray);
    }

    @Test
    public void canStepOnFree() {
        assertTrue(groundLane.canStepOn(1));
    }

    @Test
    public void canNotStepOnCoral() {
        assertFalse(groundLane.canStepOn(3));

    }

    @Test
    public void isNeverDeadly() {
        for (int i = 0; i < VISIBLE_COLLUMNS; i++) {
            assertFalse(groundLane.isDeadly(i));
        }
    }

    @Test
    public void typeIsGround() {
        assertEquals(LaneType.GROUND, groundLane.getType());
    }

    @Test
    public void laneDoesNotChangeWithArray() {
        cellArray[5] = CellType.CORAL;

        assertTrue(groundLane.canStepOn(5));
    }

    @Test
    public void driftIsZero() {
        assertEquals(0, groundLane.drift(1));
    }

}
