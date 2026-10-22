package scuttle.model;

/**
 * Sand with coral. Coral can’t be stepped on, and nothing here is deadly.
 */
public final class GroundLane extends Lane {

    private final CellType[] cells;

    public GroundLane(CellType[] cellArray) {
        super(LaneType.GROUND);
        this.cells = cellArray.clone();
    }

    @Override
    public boolean canStepOn(int column) {
        return cells[column] == CellType.FREE;
    }

    @Override
    public boolean isDeadly(int column) {
        return false;
    }

}
