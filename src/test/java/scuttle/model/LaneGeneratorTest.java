package scuttle.model;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LaneGeneratorTest {

    private static int LANE_COUNT = 200;
    private static int SEED = 42;

    @Test
    public void sameSeedGivesSameLanes() {

        LaneGenerator generator1 = new LaneGenerator(new Random(SEED));
        LaneGenerator generator2 = new LaneGenerator(new Random(SEED));

        for (int i = 0; i < LANE_COUNT; i++) {
            assertEquals(generator1.next().getType(), generator2.next().getType());
        }
    }

}
