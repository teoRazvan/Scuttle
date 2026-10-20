package scuttle.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public class ReachabilityTest {

    private static GroundLane openSand() {
        CellType[] cellArray = new CellType[VISIBLE_COLLUMNS];
        Arrays.fill(cellArray, CellType.FREE);
        return new GroundLane(cellArray);
    }

    private static GroundLane wallExcept(int... gaps) {
        CellType[] cellArray = new CellType[VISIBLE_COLLUMNS];
        Arrays.fill(cellArray, CellType.CORAL);
        for (int i: gaps) {
            cellArray[i] = CellType.FREE;
        }
        return new GroundLane(cellArray);
    }

    private static FishLane water() {
        return new FishLane(List.of(), 1, 1);
    }

    @Test
    public void openGridFullyReachable() {
        List<Lane> lanes = List.of(openSand(), openSand(), openSand());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(0, 0)), 0, 3);

        assertEquals(3 * VISIBLE_COLLUMNS, reachable.size());
    }

    @Test
    public void gapLetsYouThrough() {
        List<Lane> lanes = List.of(openSand(), wallExcept(5), openSand());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(0, 0)), 0, 3);

        assertTrue(reachable.contains(new Position(VISIBLE_COLLUMNS - 1, 2)));
        assertTrue(reachable.contains(new Position(5, 1)));
        assertFalse(reachable.contains(new Position(4, 1)));
    }

    @Test
    public void closedWallBlocks() {
        List<Lane> lanes = List.of(openSand(), wallExcept(), openSand());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(0, 0)), 0, 3);

        assertEquals(VISIBLE_COLLUMNS, reachable.size());
    }

    @Test
    public void waterRowIsOpen() {
        List<Lane> lanes = List.of(openSand(), wallExcept(0), water(), wallExcept(VISIBLE_COLLUMNS
                - 1), openSand());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(14, 0)), 0, 5);

        assertTrue(reachable.contains(new Position(20, 4)));
    }

    @Test
    public void pathCanGoDownAndBackUp() {
        List<Lane> lanes = List.of(openSand(), wallExcept(2, 20), wallExcept(20));
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(2, 1)), 0, 3);

        assertTrue(reachable.contains(new Position(20, 2)));
    }

    @Test
    public void rowLimitsAreRespected() {
        List<Lane> lanes = List.of(openSand(), openSand(), openSand());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(0, 1)), 1, 2);

        assertEquals(VISIBLE_COLLUMNS, reachable.size());
        assertFalse(reachable.contains(new Position(0, 2)));
        assertFalse(reachable.contains(new Position(0, 0)));
    }

    @Test
    public void startOnCoralIsIgnored() {
        List<Lane> lanes = List.of(wallExcept());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(3, 0)), 0, 1);

        assertTrue(reachable.isEmpty());
    }

    @Test
    public void startOutsideBoardIsIgnored() {
        List<Lane> lanes = List.of(openSand());
        Set<Position> reachable = Reachability.reachable(lanes, List.of(new Position(-1, 0)), 0, 1);

        assertTrue(reachable.isEmpty());
    }

}
