package scuttle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;
import static scuttle.model.GameRules.DIFFICULTY_RAMP_LANES;
import static scuttle.model.GameRules.MAX_DANGER_STREAK;
import static scuttle.model.GameRules.SAFE_START_LANES;

public class LaneGeneratorTest {

    private static final int LANE_COUNT = 2000;
    private static final int LONG_RUN_LANE_COUNT = 2000;
    private static final long SEED = 42;
    private static final double DELTA = 1e-9;

    @Test
    public void sameSeedGivesSameLanes() {
        LaneGenerator generator1 = new LaneGenerator(new Random(SEED));
        LaneGenerator generator2 = new LaneGenerator(new Random(SEED));

        for (int i = 0; i < LANE_COUNT; i++) {
            assertEquals(generator1.next().getType(), generator2.next().getType());
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 42, 1234})
    public void generatedWorldIsAlwaysCrossable(long seed) {
        LaneGenerator generator = new LaneGenerator(new Random(seed));
        List<Lane> lanes = new ArrayList<>();

        for (int i = 0; i < LANE_COUNT; i++) {
            lanes.add(generator.next());
        }
        List<Position> starts = new ArrayList<>();
        for (int i = 0; i < VISIBLE_COLLUMNS; i++) {
            starts.add(new Position(i, 0));
        }

        boolean[] rowReached = new boolean[lanes.size()];

        Set<Position> reachable = Reachability.reachable(lanes, starts, 0, lanes.size());
        for (Position position: reachable) {
            rowReached[position.getRow()] = true;
        }
        for (int row = 0; row < rowReached.length; row++) {
            assertTrue(rowReached[row], "row " + row + " unreachable with seed " + seed);
        }
    }

    @Test
    public void firstLanesAreSafe() {
        LaneGenerator generator = new LaneGenerator(new Random(SEED));

        for (int i = 0; i < SAFE_START_LANES; i++) {
            Lane lane = generator.next();
            assertEquals(LaneType.GROUND, lane.getType());
            for (int j = 0; j < VISIBLE_COLLUMNS; j++) {
                assertTrue(lane.canStepOn(j), "lane " + i + " column " + j);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 42, 1234})
    public void dangerStreakNeverExceedsMax(long seed) {
        LaneGenerator generator = new LaneGenerator(new Random(seed));
        List<Lane> lanes = new ArrayList<>();

        for (int i = 0; i < LONG_RUN_LANE_COUNT; i++) {
            lanes.add(generator.next());
        }
        int counter = 0;
        for (int i = 0; i < LONG_RUN_LANE_COUNT; i++) {
            if (lanes.get(i).getType() == LaneType.GROUND) {
                counter = 0;
            } else {
                counter++;
            }
            assertTrue(counter <= MAX_DANGER_STREAK, "streak too long at lane " + i);
        }
    }

    @Test
    public void neighbouringWaterLanesMoveOpposite() {
        LaneGenerator generator = new LaneGenerator(new Random(SEED));
        int previousDirection = 0;

        for (int i = 0; i < LANE_COUNT; i++) {
            int currentDirection = generator.next().getDirection();
            if (previousDirection != 0 && currentDirection != 0) {
                assertNotEquals(previousDirection, currentDirection, "lanes " + (i - 1) + " and "
                        + i);
            }
            previousDirection = currentDirection;
        }
    }

    @Test
    public void difficultyRampsUpAndIsCapped() {
        LaneGenerator generator = new LaneGenerator(new Random(SEED));
        assertEquals(0.0, generator.difficulty(), DELTA);
        for (int i = 0; i < DIFFICULTY_RAMP_LANES; i++) {
            generator.next();
        }
        assertEquals(1.0, generator.difficulty(), DELTA);
        for (int i = 0; i < 100; i++) {
            generator.next();
        }
        assertEquals(1.0, generator.difficulty(), DELTA);

    }

    @Test
    public void difficultyIsHalfwayAtHalfTheRamp() {
        LaneGenerator generator = new LaneGenerator(new Random(SEED));
        for (int i = 0; i < DIFFICULTY_RAMP_LANES / 2; i++) {
            generator.next();
        }
        assertEquals(0.5, generator.difficulty(), DELTA);
    }

}
