package scuttle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FishLane extends Lane {

    private final List<Mover> fishes;
    private final int ticksPerStep;//ticks in between each update
    private final int direction;// 1 || -1 determines in which direction a fish moves

    public FishLane(List<Mover> movers, int ticksPerStep, int direction) {
        super(LaneType.FISH);
        if (ticksPerStep <= 0) {
            throw new IllegalArgumentException("ticksPerStep must be greater then 0");
        }
        if (direction != 1 && direction != -1) {
            throw new IllegalArgumentException("direction must be 1 or -1");
        }
        this.direction = direction;
        this.ticksPerStep = ticksPerStep;
        this.fishes = new ArrayList<>(movers);
    }

    @Override
    public boolean canStepOn(int column) {
        return true;
    }

    @Override
    public boolean isDeadly(int column) {
        for (Mover m: fishes) {
            if (m.covers(column)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(int tick) {
        if (tick % this.ticksPerStep == 0) {
            for (Mover m: fishes) {
                m.shift(direction);
            }
        }
    }

    public List<Mover> getFishes() {
        return Collections.unmodifiableList(fishes);
    }

    @Override
    public int getDirection() {
        return direction;
    }

}
