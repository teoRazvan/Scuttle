package scuttle.model;

import java.util.List;

public class FishLane extends MoverLane {

    public FishLane(List<Mover> movers, int ticksPerStep, int direction) {
        super(LaneType.FISH, movers, ticksPerStep, direction);
    }

    @Override
    public boolean isDeadly(int column) {
        return isCoveredByMover(column);
    }

}
