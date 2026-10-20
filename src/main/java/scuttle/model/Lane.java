package scuttle.model;

public abstract class Lane {

    private final LaneType laneType;

    protected Lane(LaneType laneType) {
        this.laneType = laneType;
    }

    public LaneType getType() {
        return laneType;
    }

    public abstract boolean canStepOn(int column);

    public abstract boolean isDeadly(int column);

    //Lanes that don't move do nothing each tick
    public void update(int tick) {

    }

    public int getDirection() {
        return 0;
    }

    public int drift(int tick) {
        return 0;
    }

}
