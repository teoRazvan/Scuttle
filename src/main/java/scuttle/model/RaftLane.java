package scuttle.model;

import java.util.List;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public class RaftLane extends MoverLane {

    public RaftLane(List<Mover> movers, int ticksPerStep, int direction) {
        super(LaneType.RAFT, movers, ticksPerStep, direction);
    }

    @Override
    public boolean isDeadly(int column) {
        if (column < 0 || column >= VISIBLE_COLLUMNS) {
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
