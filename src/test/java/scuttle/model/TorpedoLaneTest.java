package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public class TorpedoLaneTest {

    private static final int IDLE_TICKS = 2;
    private static final int WARNING_TICKS = 2;
    private static final int FIRING_TICKS = 2;
    private static final int INITIAL_DELAY = 1;

    private TorpedoLane lane;

    @BeforeEach
    public void setup() {
        lane = new TorpedoLane(IDLE_TICKS, WARNING_TICKS, FIRING_TICKS, INITIAL_DELAY);
    }

    private void advance(int ticks) {
        for (int i = 0; i < ticks; i++) {
            lane.update(i);
        }
    }

    @Test
    public void launcherColumnIsBlocked() {
        assertFalse(lane.canStepOn(TorpedoLane.LAUNCHER_COLUMN));
    }

    @Test
    public void otherColumnsAreWalkable() {
        for (int i = TorpedoLane.LAUNCHER_COLUMN + 2; i < VISIBLE_COLLUMNS; i++) {
            assertTrue(lane.canStepOn(i), "column " + i);
        }
    }

    @Test
    public void startsIdleAndHarmless() {
        assertAll("is safe at start",
                () -> assertFalse(lane.isDeadly(0)),
                () -> assertFalse(lane.isWarning()),
                () -> assertFalse(lane.isFiring())
        );
    }

    @Test
    public void warnsAfterInitialDelay() {
        advance(INITIAL_DELAY);
        assertTrue(lane.isWarning());
    }

    @Test
    public void firesAfterWarning() {
        advance(INITIAL_DELAY + WARNING_TICKS);
        assertTrue(lane.isFiring());
    }

    @Test
    public void deadlyEverywhereWhileFiring() {
        advance(INITIAL_DELAY + WARNING_TICKS);
        for (int i = TorpedoLane.LAUNCHER_COLUMN + 1; i < VISIBLE_COLLUMNS; i++) {
            assertTrue(lane.isDeadly(i), "column " + i);
        }
    }

    @Test
    public void returnsToIdleAfterFiring() {
        advance(INITIAL_DELAY + WARNING_TICKS + FIRING_TICKS);
        assertAll("is safe after firing",
                () -> assertFalse(lane.isDeadly(0)),
                () -> assertFalse(lane.isWarning()),
                () -> assertFalse(lane.isFiring())
        );
    }

    @Test
    public void rejectsNonPositiveTicks() {
        assertThrows(IllegalArgumentException.class, () -> new TorpedoLane(0, WARNING_TICKS,
                FIRING_TICKS, INITIAL_DELAY));
    }

}
