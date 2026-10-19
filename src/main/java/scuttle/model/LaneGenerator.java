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

public class LaneGenerator {

    private Lane previousLane = null;
    private Set<Position> frontier = new HashSet<>();
    private final Random random;
    private int lanesMade;
    private LaneType previousType;
    private int previousDirection = 1;
    private int dangerStreak;

    public LaneGenerator() {
        this(new Random());
    }

    public LaneGenerator(Random random) {
        this.random = random;
        this.lanesMade = 0;
        this.dangerStreak = 0;
    }

    double difficulty() {
        return Math.min(1.0, lanesMade / (double) DIFFICULTY_RAMP_LANES);
    }

    int scaled(int easy, int hard) {
        return (int) Math.round(easy + (hard - easy) * difficulty());
    }

    private Set<Position> computeFrontier(Lane newLane) {
        if (previousLane == null) {
            List<Position> start = new ArrayList<>();
            for (int i = 0; i < VISIBLE_COLUMNS; i++) {
                start.add(new Position(i, 0));
            }
            return Reachability.reachable(List.of(newLane), start, 0, 1);
        }
        Set<Position> reachable
                = Reachability.reachable(List.of(previousLane, newLane), frontier, 0, 2);
        Set<Position> newFrontier = new HashSet<>();
        for (Position p: reachable) {
            if (p.getRow() == 1) {
                newFrontier.add(new Position(p.getColumn(), 0));
            }
        }
        return newFrontier;
    }

    private GroundLane makePlainCround() {
        CellType[] cellType = new CellType[VISIBLE_COLUMNS];
        for (int i = 0; i < cellType.length; i++) {
            cellType[i] = CellType.FREE;
        }
        return new GroundLane(cellType);
    }

    public Lane next() {
        Lane lane = null;
        lanesMade++;
        if (lanesMade <= SAFE_START_LANES) {

            lane = makePlainCround();
        } else if (dangerStreak >= MAX_DANGER_STREAK) {
            lane = makeReachableGround();
        } else {
            switch (pickType()) {
                case GROUND:
                    lane = makeReachableGround();
                    break;
                case FISH:
                    lane = makeFish();
                    break;
                case RAFT:
                    lane = makeRaft();
                    break;
                case TORPEDO:
                    lane = makeTorpedo();
                    break;
            }
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
        int randomSeed = random.nextInt(totalWeight);

        if (randomSeed < groundWeight) {
            return LaneType.GROUND;
        } else if (randomSeed < fishWeight + groundWeight) {
            return LaneType.FISH;
        } else if (randomSeed < raftWeight + fishWeight + groundWeight) {
            return LaneType.RAFT;
        } else {
            return LaneType.TORPEDO;
        }
    }

    private List<Mover> placeMoovers(int count, int minLength, int maxLength) {
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
        Lane candidate;
        for (int i = 0; i < MAX_GROUND_ATTEMPTS; i++) {
            candidate = makeGround();
            if (!computeFrontier(candidate).isEmpty()) {
                return (GroundLane) candidate;
            }
        }
        candidate = makePlainCround();
        return (GroundLane) candidate;
    }

    private Lane makeGround() {
        Lane groundLane;
        CellType[] cellType = new CellType[VISIBLE_COLUMNS];

        for (int i = 0; i < cellType.length; i++) {
            int randomSeed = random.nextInt(100);
            if (randomSeed < scaled(CORAL_CHANCE_EASY, CORAL_CHANCE_HARD)) {
                cellType[i] = CellType.CORAL;
            } else {
                cellType[i] = CellType.FREE;
            }
        }
        groundLane = new GroundLane(cellType);
        return groundLane;
    }

    private Lane makeFish() {
        Lane fishLane;
        int minFishCount = scaled(MIN_FISH_COUNT_EASY, MIN_FISH_COUNT_HARD);
        int maxFishCount = scaled(MAX_FISH_COUNT_EASY, MAX_FISH_COUNT_HARD);
        int minFishLength = scaled(MIN_FISH_LENGTH_EASY, MIN_FISH_LENGTH_HARD);
        int maxFishLength = scaled(MAX_FISH_LENGTH_EASY, MAX_FISH_LENGTH_HARD);
        int minFishTicks = scaled(MIN_FISH_TICKS_EASY, MIN_FISH_TICKS_HARD);
        int maxFishTicks = scaled(MAX_FISH_TICKS_EASY, MAX_FISH_TICKS_HARD);
        fishLane = new FishLane(placeMoovers(randomBetween(minFishCount, maxFishCount),
                minFishLength, maxFishLength), randomBetween(minFishTicks, maxFishTicks),
                nextDirection());
        return fishLane;
    }

    private Lane makeRaft() {
        Lane raftLane;
        int minRaftCount = scaled(MIN_RAFT_COUNT_EASY, MIN_RAFT_COUNT_HARD);
        int maxRaftCount = scaled(MAX_RAFT_COUNT_EASY, MAX_RAFT_COUNT_HARD);
        int minRaftLength = scaled(MIN_RAFT_LENGTH_EASY, MIN_RAFT_LENGTH_HARD);
        int maxRaftLength = scaled(MAX_RAFT_LENGTH_EASY, MAX_RAFT_LENGTH_HARD);
        int minRaftTicks = scaled(MIN_RAFT_TICKS_EASY, MIN_RAFT_TICKS_HARD);
        int maxRaftTicks = scaled(MAX_RAFT_TICKS_EASY, MAX_RAFT_TICKS_HARD);
        raftLane = new RaftLane(
                placeMoovers(randomBetween(minRaftCount, maxRaftCount),
                        minRaftLength, maxRaftLength), randomBetween(
                        minRaftTicks, maxRaftTicks),
                nextDirection());
        return raftLane;
    }

    private Lane makeTorpedo() {
        Lane torpedoLane;
        int minIdleTicks = scaled(MIN_TORPEDO_IDLE_TICKS_EASY, MIN_TORPEDO_IDLE_TICKS_HARD);
        int maxIdleTicks = scaled(MAX_TORPEDO_IDLE_TICKS_EASY, MAX_TORPEDO_IDLE_TICKS_HARD);
        int idle = randomBetween(minIdleTicks, maxIdleTicks);
        torpedoLane = new TorpedoLane(idle, TORPEDO_WARNING_TICKS, TORPEDO_FIRING_TICKS,
                randomBetween(1, idle));
        return torpedoLane;
    }

}
