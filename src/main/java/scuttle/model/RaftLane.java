package scuttle.model;

import java.util.ArrayList;
import java.util.List;
import static scuttle.model.Constants.VISIBLE_COLUMNS;

public class RaftLane extends Lane {

    private final List<Mover> rafts;
    private final int ticksPerStep;//ticks in between each update
    private final int direction;// 1 || -1 determines in which direction a fish moves

    public RaftLane(List<Mover> movers, int ticksPerStep, int direction) {
        super(LaneType.RAFT);
        if (ticksPerStep <= 0) {
            throw new IllegalArgumentException("ticksPerStep must be greater then 0");
        }
        if (direction != 1 && direction != -1) {
            throw new IllegalArgumentException("direction must be 1 or -1");
        }
        this.direction = direction;
        this.ticksPerStep = ticksPerStep;
        this.rafts = new ArrayList<>(movers);
    }

    @Override
    public boolean canStepOn(int column) {
        return true;
    }

    @Override
    public boolean isDeadly(int column) {
        if (column < 0 || column >= VISIBLE_COLUMNS) {
            return true;
        }
        for (Mover m: rafts) {
            if (m.covers(column)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(int tick) {
        if (tick % this.ticksPerStep == 0) {
            for (Mover m: rafts) {
                m.shift(direction);
            }
        }
    }

    @Override
    public int drift(int tick) {
        if (tick % this.ticksPerStep == 0) {
            return direction;
        }
        return 0;
    }

}
