package scuttle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MoverLane extends Lane {

    private final List<Mover> movers;
    private final int ticksPerStep;//ticks in between each update
    private final int direction;// 1 || -1 determines in which direction a fish moves

    protected MoverLane(LaneType type, List<Mover> movers, int ticksPerStep, int direction) {
        super(type);
        if (ticksPerStep <= 0) {
            throw new IllegalArgumentException("ticksPerStep must be greater than 0");
        }
        if (direction != 1 && direction != -1) {
            throw new IllegalArgumentException("direction must be 1 or -1");
        }
        this.direction = direction;
        this.ticksPerStep = ticksPerStep;
        this.movers = new ArrayList<>(movers);
    }

    boolean isCoveredByMover(int column) {
        for (Mover mover: movers) {
            if (mover.covers(column)) {
                return true;
            }
        }
        return false;
    }

    boolean isMoveTick(int tick) {
        return tick % ticksPerStep == 0;
    }

    public List<Mover> getMovers() {
        return Collections.unmodifiableList(movers);
    }

    @Override
    public boolean canStepOn(int column) {
        return true;
    }

    @Override
    public int getDirection() {
        return direction;
    }

    @Override
    public void update(int tick) {
        if (isMoveTick(tick)) {
            for (Mover mover: movers) {
                mover.shift(direction);
            }
        }
    }

}
