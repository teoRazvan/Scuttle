package scuttle.model;

import java.util.Objects;

public final class Position {

    private final int column;
    private final int row;

    public Position(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public Position step(Direction direction) {
        return switch (direction) {
            case DOWN ->
                new Position(this.getColumn(),
                this.getRow() - 1);
            case UP ->
                new Position(this.getColumn(),
                this.getRow() + 1);
            case LEFT ->
                new Position(this.getColumn() - 1,
                this.getRow());
            case RIGHT ->
                new Position(this.getColumn() + 1,
                this.getRow());
        };
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.column, this.row);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Position)) {
            return false;
        }

        Position p = (Position) o;

        return this.column == p.getColumn() && this.row == p.getRow();
    }

    @Override
    public String toString() {
        return "Position[column=" + this.column + ", row=" + this.row + "]";
    }

}
