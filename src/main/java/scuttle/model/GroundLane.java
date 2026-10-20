package scuttle.model;

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
