package scuttle.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;
import static scuttle.model.Constants.VISIBLE_COLUMNS;

public class LaneGenerator {

    private static final int CORAL_CHANCE = 30;
    static final int MAX_DANGER_STREAK = 4;
    private static final int MAX_GORUND_ATTEMPTS = 10;
    static final int SAFE_START_LANES = 4;

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
        int randomSeed = random.nextInt(100);
        if (randomSeed < 40) {
            return LaneType.GROUND;
        } else if (randomSeed < 70) {
            return LaneType.FISH;
        } else if (randomSeed < 90) {
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
        for (int i = 0; i < MAX_GORUND_ATTEMPTS; i++) {
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
            if (randomSeed < CORAL_CHANCE) {
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
        fishLane = new FishLane(placeMoovers(randomBetween(2, 5), 1, 2), randomBetween(10, 30),
                nextDirection());
        return fishLane;
    }

    private Lane makeRaft() {
        Lane raftLane;
        raftLane = new RaftLane(placeMoovers(randomBetween(3, 5), 2, 4), randomBetween(15, 30),
                nextDirection());
        return raftLane;
    }

    private Lane makeTorpedo() {
        Lane torpedoLane;
        int idle = randomBetween(180, 360);
        torpedoLane = new TorpedoLane(idle, 60, 20, randomBetween(1, idle));
        return torpedoLane;
    }

}
