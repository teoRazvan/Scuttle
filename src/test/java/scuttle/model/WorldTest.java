package scuttle.model;

import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;

public class WorldTest {

    @RepeatedTest(100)
    public void coinsOnlyOnReachableSand() {
        World world = new World();
        Set<Position> reachableCells = Reachability.reachable(world.getLanes(), List.of(
                world.getPlayer().getPosition()), world.getCameraRow(), world.getLanes().size());
        for (Position coin: world.getCoins()) {
            assertAll("coin " + coin.toString(),
                    () -> assertEquals(LaneType.GROUND,
                            world.getLanes().get(coin.getRow()).getType()),
                    () -> assertTrue(world.getLanes().get(coin.getRow())
                            .canStepOn(coin.getColumn())),
                    () -> assertTrue(reachableCells.contains(coin)),
                    () -> assertFalse(coin.equals(world.getPlayer().getPosition()))
            );
        }
    }

}
