package scuttle.model;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.VISIBLE_COLUMNS;

public class RaftLaneTest {

    private static final int TICKS_PER_STEP = 3;
    private static final int RAFT_START = 5;
    private static final int RAFT_LENGTH = 2;
    RaftLane lane;

    @BeforeEach
    public void setup() {
        lane = new RaftLane(List.of(new Mover(RAFT_START, RAFT_LENGTH)),
                TICKS_PER_STEP, 1);
    }

    @Test
    public void raftIsSafe() {
        assertAll("Lane is safe where raft is",
                () -> assertFalse(lane.isDeadly(RAFT_START)),
                () -> assertFalse(lane.isDeadly(RAFT_START + RAFT_LENGTH - 1))
        );
    }

    @Test
    public void waterIsDeadly() {
        assertAll("Lane is deadly around the ",
                () -> assertTrue(lane.isDeadly(RAFT_START - 1)),
                () -> assertTrue(lane.isDeadly(RAFT_START + RAFT_LENGTH))
        );
    }

    @Test
    public void offScreenIsDeadly() {
        assertAll("Lane is safe where raft is",
                () -> assertTrue(lane.isDeadly(-1)),
                () -> assertTrue(lane.isDeadly(VISIBLE_COLUMNS))
        );
    }

    @Test
    public void driftsOnlyOnStepTicks() {
        assertEquals(0, lane.drift(1));
        assertEquals(1, lane.drift(TICKS_PER_STEP));
    }

    @Test
    public void driftFollowsDirection() {
        RaftLane lane2 = new RaftLane(List.of(new Mover(RAFT_START, RAFT_LENGTH)),
                TICKS_PER_STEP, -1);
        assertNotEquals(1, lane2.drift(TICKS_PER_STEP));
        assertEquals(-1, lane2.drift(TICKS_PER_STEP));
    }

}
