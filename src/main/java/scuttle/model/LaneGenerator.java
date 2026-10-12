package scuttle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static scuttle.model.Constants.HIDDEN_MARGIN;
import static scuttle.model.Constants.LANE_WIDTH;
import static scuttle.model.Constants.VISIBLE_COLUMNS;

/**
 *
 * @author teo03
 */
public class LaneGenerator {

    private static final int CORAL_CHANCE = 15;
    private static final int MAX_DANGER_STREAK = 3;

    private final Random random;
    private int lanesMade;
    private LaneType previousType;
    private boolean previousHadCoral = false;
    private int previousDirection = 1;
    private int dangerStreak;

    public LaneGenerator() {
        this.lanesMade = 0;
        this.random = new Random();
        this.dangerStreak = 0;
    }

    public Lane next() {
        Lane lane = null;
        lanesMade++;
        if (lanesMade < 5) {
            CellType[] cellType = new CellType[VISIBLE_COLUMNS];
            for (int i = 0; i < cellType.length; i++) {
                cellType[i] = CellType.FREE;
            }
            lane = new GroundLane(cellType);
        } else if (dangerStreak >= MAX_DANGER_STREAK) {
            lane = makeGround();
        } else {
            switch (pickType()) {
                case GROUND:
                    lane = makeGround();
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

    private Lane makeGround() {
        Lane groundLane;
        CellType[] cellType = new CellType[VISIBLE_COLUMNS];
        if (previousHadCoral) {
            for (int i = 0; i < cellType.length; i++) {
                cellType[i] = CellType.FREE;
            }
            previousHadCoral = false;
        } else {
            previousHadCoral = true;
            for (int i = 0; i < cellType.length; i++) {
                int randomSeed = random.nextInt(100);
                if (randomSeed < CORAL_CHANCE) {
                    cellType[i] = CellType.CORAL;
                } else {
                    cellType[i] = CellType.FREE;
                }
            }
        }
        groundLane = new GroundLane(cellType);
        return groundLane;
    }

    private Lane makeFish() {
        Lane fishLane;
        fishLane = new FishLane(placeMoovers(randomBetween(2, 4), 1, 2), randomBetween(10, 30),
                nextDirection());
        return fishLane;
    }

    private Lane makeRaft() {
        Lane raftLane;
        raftLane = new RaftLane(placeMoovers(randomBetween(3, 4), 2, 4), randomBetween(15, 30),
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
