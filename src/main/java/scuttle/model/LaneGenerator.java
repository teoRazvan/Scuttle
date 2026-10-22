package scuttle.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.GameRules.*;

/**
 * It creates the endless stream of lanes, one at a time. Each new lane is random, but it’s
 * guaranteed to be crossable: it remembers which cells of the previous lane the player can reach
 * (the frontier) and checks the new lane against them with BFS. Lanes get harder as more are made.
 *
 */
public class LaneGenerator {

    private Lane previousLane;
    private Set<Position> frontier = new HashSet<>();
    private final Random random;
    private int lanesMade;
    private LaneType previousType;
    // The first mover lane has a random direction either way, 1 is an arbitrary starting value
    private int previousDirection = 1;
    private int dangerStreak;

    public LaneGenerator() {
        this(new Random());
    }

    /**
     * Creates a generator that uses the given random source.
     *
     * Passing a Random with a fixed seed gives the same map every time so we can test behavior.
     *
     * @param random a known Random.
     */
    public LaneGenerator(Random random) {
        this.random = random;
        this.lanesMade = 0;
        this.dangerStreak = 0;
    }

    /**
     * Returns how far the difficulty has ramped up, as a number from 0 to 1. It’s 0 at the start,
     * grows evenly with each lane made, and stays at 1 after DIFFICULTY_RAMP_LANES lanes.
     *
     * @return
     */
    double difficulty() {
        return Math.min(1.0, lanesMade / (double) DIFFICULTY_RAMP_LANES);
    }

    /**
     * Returns a game-rule value for the current difficulty, by going from the easy value to the
     * hard value in a straight line, rounded to an int.
     *
     * @param easy the value at difficulty 0.
     * @param hard the value at difficulty 1.
     * @return the value at the current difficulty.
     */
    int scaled(int easy, int hard) {
        return (int) Math.round(easy + (hard - easy) * difficulty());
    }

    /* Runs BFS over two lanes the previous one (row 0) and the new one (row 1).
    An empty result means a bllocked lane
     */
    private Set<Position> computeFrontier(Lane newLane) {
        if (previousLane == null) {
            List<Position> firstRow = new ArrayList<>();
            for (int i = 0; i < VISIBLE_COLUMNS; i++) {
                firstRow.add(new Position(i, 0));
            }
            return Reachability.reachable(List.of(newLane), firstRow, 0, 1);
        }
        Set<Position> reachable
                = Reachability.reachable(List.of(previousLane, newLane), frontier, 0, 2);
        Set<Position> newFrontier = new HashSet<>();
        for (Position position: reachable) {
            if (position.getRow() == 1) {
                newFrontier.add(new Position(position.getColumn(), 0));
            }
        }
        return newFrontier;
    }

    private GroundLane makePlainGround() {
        CellType[] cellType = new CellType[VISIBLE_COLUMNS];
        for (int i = 0; i < cellType.length; i++) {
            cellType[i] = CellType.FREE;
        }
        return new GroundLane(cellType);
    }

    public Lane next() {
        Lane lane;
        lanesMade++;
        if (lanesMade <= SAFE_START_LANES) {
            lane = makePlainGround();
        } else if (dangerStreak >= MAX_DANGER_STREAK) {
            lane = makeReachableGround();
        } else {
            lane = switch (pickType()) {
                case GROUND ->
                    makeReachableGround();
                case FISH ->
                    makeFish();
                case RAFT ->
                    makeRaft();
                case TORPEDO ->
                    makeTorpedo();
            };
        }
        previousType = lane.getType();
        if (previousType == LaneType.GROUND) {
            dangerStreak = 0;
        } else {
            dangerStreak++;
        }

        Set<Position> newFrontier = computeFrontier(lane);
        previousLane = lane;
        frontier = newFrontier;
        return lane;

    }

    // Adjecent MoverLanes always have opposite directions so the game is fair
    private int nextDirection() {
        if (previousType == LaneType.FISH || previousType == LaneType.RAFT) {
            previousDirection = -previousDirection;
        } else {
            previousDirection = randomDirection();
        }
        return previousDirection;
    }

    private int randomBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private int randomDirection() {
        return random.nextBoolean() ? 1 : -1;
    }

    private LaneType pickType() {
        int groundWeight = scaled(GROUND_WEIGHT_EASY, GROUND_WEIGHT_HARD);
        int fishWeight = scaled(FISH_WEIGHT_EASY, FISH_WEIGHT_HARD);
        int raftWeight = scaled(RAFT_WEIGHT_EASY, RAFT_WEIGHT_HARD);
        int torpedoWeight = scaled(TORPEDO_WEIGHT_EASY, TORPEDO_WEIGHT_HARD);
        int totalWeight = groundWeight + fishWeight + raftWeight + torpedoWeight;
        int roll = random.nextInt(totalWeight);

        if (roll < groundWeight) {
            return LaneType.GROUND;
        } else if (roll < fishWeight + groundWeight) {
            return LaneType.FISH;
        } else if (roll < raftWeight + fishWeight + groundWeight) {
            return LaneType.RAFT;
        } else {
            return LaneType.TORPEDO;
        }
    }

    private List<Mover> placeMovers(int count, int minLength, int maxLength) {
        List<Mover> moverList = new ArrayList<>();
        int slotSize = LANE_WIDTH / count;
        for (int i = 0; i < count; i++) {
            int length = randomBetween(minLength, maxLength);
            int offset = randomBetween(0, slotSize - length - 1);
            int start = i * slotSize + offset - HIDDEN_MARGIN;
            moverList.add(new Mover(start, length));
        }
        return moverList;
    }

    private GroundLane makeReachableGround() {
        for (int i = 0; i < MAX_GROUND_ATTEMPTS; i++) {
            GroundLane candidate;
            candidate = makeGround();
            if (!computeFrontier(candidate).isEmpty()) {
                return candidate;
            }
        }
        return makePlainGround();
    }

    private GroundLane makeGround() {
        GroundLane groundLane;
        CellType[] cellType = new CellType[VISIBLE_COLUMNS];
        int coralChance = scaled(CORAL_CHANCE_EASY, CORAL_CHANCE_HARD);
        for (int i = 0; i < cellType.length; i++) {
            int roll = random.nextInt(100);
            if (roll < coralChance) {
                cellType[i] = CellType.CORAL;
            } else {
                cellType[i] = CellType.FREE;
            }
        }
        groundLane = new GroundLane(cellType);
        return groundLane;
    }

    private Lane makeFish() {
        int minFishCount = scaled(MIN_FISH_COUNT_EASY, MIN_FISH_COUNT_HARD);
        int maxFishCount = scaled(MAX_FISH_COUNT_EASY, MAX_FISH_COUNT_HARD);
        int minFishLength = scaled(MIN_FISH_LENGTH_EASY, MIN_FISH_LENGTH_HARD);
        int maxFishLength = scaled(MAX_FISH_LENGTH_EASY, MAX_FISH_LENGTH_HARD);
        int minFishTicks = scaled(MIN_FISH_TICKS_EASY, MIN_FISH_TICKS_HARD);
        int maxFishTicks = scaled(MAX_FISH_TICKS_EASY, MAX_FISH_TICKS_HARD);

        int count = randomBetween(minFishCount, maxFishCount);
        int ticksPerStep = randomBetween(minFishTicks, maxFishTicks);

        return new FishLane(placeMovers(count, minFishLength, maxFishLength), ticksPerStep,
                nextDirection());
    }

    private Lane makeRaft() {
        int minRaftCount = scaled(MIN_RAFT_COUNT_EASY, MIN_RAFT_COUNT_HARD);
        int maxRaftCount = scaled(MAX_RAFT_COUNT_EASY, MAX_RAFT_COUNT_HARD);
        int minRaftLength = scaled(MIN_RAFT_LENGTH_EASY, MIN_RAFT_LENGTH_HARD);
        int maxRaftLength = scaled(MAX_RAFT_LENGTH_EASY, MAX_RAFT_LENGTH_HARD);
        int minRaftTicks = scaled(MIN_RAFT_TICKS_EASY, MIN_RAFT_TICKS_HARD);
        int maxRaftTicks = scaled(MAX_RAFT_TICKS_EASY, MAX_RAFT_TICKS_HARD);

        int count = randomBetween(minRaftCount, maxRaftCount);
        int ticksPerStep = randomBetween(minRaftTicks, maxRaftTicks);

        return new RaftLane(placeMovers(count, minRaftLength, maxRaftLength), ticksPerStep,
                nextDirection());
    }

    private Lane makeTorpedo() {
        int minIdleTicks = scaled(MIN_TORPEDO_IDLE_TICKS_EASY, MIN_TORPEDO_IDLE_TICKS_HARD);
        int maxIdleTicks = scaled(MAX_TORPEDO_IDLE_TICKS_EASY, MAX_TORPEDO_IDLE_TICKS_HARD);

        int idle = randomBetween(minIdleTicks, maxIdleTicks);
        /* The last argument, randomBetween(1, idle), is the starting delay. It’s random so that
        torpedo lanes don’t all fire at the same moment.
         */
        return new TorpedoLane(idle, TORPEDO_WARNING_TICKS, TORPEDO_FIRING_TICKS,
                randomBetween(1, idle));
    }

}
