package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    public void gettersReturnConstructorValues() {
        int column = 4;
        int row = 2;
        Position position = new Position(column, row);
        assertEquals(column, position.getColumn());
        assertEquals(row, position.getRow());
    }

    @Test
    public void samePositionsAreEqual() {
        int column = 4;
        int row = 2;
        Position position1 = new Position(column, row);
        Position position2 = new Position(column, row);
        assertEquals(position1, position2);
    }

    @Test
    public void differentColumnIsNotEquals() {
        int column1 = 4;
        int column2 = 5;
        int row = 2;
        Position position1 = new Position(column1, row);
        Position position2 = new Position(column2, row);
        assertNotEquals(position1, position2);
    }

    @Test
    public void differentRowIsNotEqual() {
        int column = 4;
        int row1 = 5;
        int row2 = 2;
        Position position1 = new Position(column, row1);
        Position position2 = new Position(column, row2);
        assertNotEquals(position1, position2);
    }

    @Test
    public void equalPositionsHaveSameHashCode() {
        int column = 4;
        int row = 2;
        Position position1 = new Position(column, row);
        Position position2 = new Position(column, row);
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    public void positionIsNotEqualToNull() {
        int column = 4;
        int row = 5;
        Position position = new Position(column, row);
        assertFalse(position.equals(null));
    }

    @Test
    public void stepWorks() {
        int column = 4;
        int row = 5;
        Position position = new Position(column, row);

        Position up = new Position(column, row + 1);
        Position down = new Position(column, row - 1);
        Position left = new Position(column - 1, row);
        Position right = new Position(column + 1, row);

        assertEquals(position.step(Direction.DOWN), down);
        assertEquals(position.step(Direction.UP), up);
        assertEquals(position.step(Direction.LEFT), left);
        assertEquals(position.step(Direction.RIGHT), right);
    }

}
