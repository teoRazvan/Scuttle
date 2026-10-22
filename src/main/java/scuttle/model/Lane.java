package scuttle.model;

/**
 * It’s the base class for one row of the world. Each kind of lane decides for itself which cells
 * can be stepped on and which kill the player, so the rest of the game (the BFS, World) never needs
 * to know the lane type.
 */
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

    /**
     * Advances the lane by one tick. By default it does nothing (for lanes that don’t move).
     *
     * @paramtick: the current tick count.
     */
    public void update(int tick) {

    }

    public int getDirection() {
        return 0;
    }

    public int drift(int tick) {
        return 0;
    }

}
