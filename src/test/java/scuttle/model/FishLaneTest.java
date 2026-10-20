package scuttle.model;

import org.junit.jupiter.api.BeforeEach;

public class FishLaneTest {

    private static final int IDLE_TICKS = 2;
    private static final int WARNING_TICKS = 2;
    private static final int FIRING_TICKS = 2;
    private static final int INITIAL_DELAY = 1;

    private Lane lane;

    @BeforeEach
    public void setup() {
        lane = new TorpedoLane(IDLE_TICKS, WARNING_TICKS, FIRING_TICKS, INITIAL_DELAY);
    }

}
