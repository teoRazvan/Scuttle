package scuttle.model;

public class GroundLane extends Lane {

    private final CellType[] cellArray;

    public GroundLane(CellType[] cellArray) {
        super(LaneType.GROUND);
        this.cellArray = cellArray.clone();
    }

    @Override
    public boolean canStepOn(int column) {
        return cellArray[column] == CellType.FREE;
    }

    public CellType getCell(int column) {
        return cellArray[column];
    }

    @Override
    public boolean isDeadly(int column) {
        return false;
    }

}
