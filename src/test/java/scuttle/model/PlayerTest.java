package scuttle.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private static final Position START = new Position(4, 2);

    @Test
    public void startsAtGivenPositionFacingUp() {
        Player player = new Player(START);
        assertAll("Player at the correct possition and with correct facing",
                () -> assertEquals(Direction.UP, player.getFacing()),
                () -> assertEquals(START, player.getPosition()));
    }

    @Test
    public void moveUpdatesPositionAndFacing() {
        Player player = new Player(START);
        player.move(START.step(Direction.LEFT), Direction.LEFT);
        assertAll(() -> assertEquals(Direction.LEFT, player.getFacing()),
                () -> assertEquals(START.step(Direction.LEFT), player.getPosition()));
    }

}
