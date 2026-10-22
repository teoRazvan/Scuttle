package scuttle.model;

import java.util.Objects;

/**
 * An immutable (column, row) cell on the grid. Row 0 is the bottom, and rows grow upward. It
 * overrides equals/hashCode so it can be used in sets (the BFS and the coins).
 */
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
                new Position(column, row - 1);
            case UP ->
                new Position(column, row + 1);
            case LEFT ->
                new Position(column - 1, row);
            case RIGHT ->
                new Position(column + 1, row);
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
        } else if (!(o instanceof Position position)) {
            return false;
        } else {
            return this.column == position.getColumn() && this.row == position.getRow();
        }
    }

    @Override
    public String toString() {
        return "Position[column=" + this.column + ", row=" + this.row + "]";
    }

}
