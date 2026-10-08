package scuttle.model;

import java.util.Collections;
import java.util.List;

public class FishLane extends Lane {

    private final List<Mover> fishes;
    private final int ticksPerStep;//ticks in between each update
    private final int direction;// 1 || -1 determines in which direction a fish moves

    public FishLane(List<Mover> movers, int updateTime, int direction) {
        super(LaneType.FISH);
        this.direction = direction;
        this.ticksPerStep = updateTime;
        this.fishes = movers;
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

}
