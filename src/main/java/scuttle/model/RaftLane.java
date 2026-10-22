package scuttle.model;

import java.util.List;
import static scuttle.model.Constants.VISIBLE_COLUMNS;

/**
 * The trench with jellyfish. Only the jellyfish are safe. The player drifts with them, and leaving
 * the screen edge is deadly. In code they are called rafts.
 */
public class RaftLane extends MoverLane {

    public RaftLane(List<Mover> movers, int ticksPerStep, int direction) {
        super(LaneType.RAFT, movers, ticksPerStep, direction);
    }

    @Override
    public boolean isDeadly(int column) {
        if (column < 0 || column >= VISIBLE_COLUMNS) {
            return true;
        }
        return !isCoveredByMover(column);
    }

    @Override
    public int drift(int tick) {
        if (isMoveTick(tick)) {
            return getDirection();
        }
        return 0;
    }

}
