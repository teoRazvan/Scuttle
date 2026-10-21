package scuttle.model;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public class FishLaneTest {

    private static final int TICKS_PER_STEP = 3;
    private static final int FISH_START = 5;
    private static final int FISH_LENGTH = 2;
    FishLane lane;

    @BeforeEach
    public void setup() {
        lane = new FishLane(List.of(new Mover(FISH_START, FISH_LENGTH)),
                TICKS_PER_STEP, 1);
    }

    @Test
    public void deadlyWhereTheFishIs() {
        assertAll("Lane is deadly where fish is",
                () -> assertTrue(lane.isDeadly(FISH_START)),
                () -> assertTrue(lane.isDeadly(FISH_START + FISH_LENGTH - 1))
        );
    }

    @Test
    public void safeNextToTheFish() {
        assertAll("Lane is not deadly around the fish",
                () -> assertFalse(lane.isDeadly(FISH_START - 1)),
                () -> assertFalse(lane.isDeadly(FISH_START + FISH_LENGTH))
        );
    }

    @Test
    public void canAlwaysStepOn() {
        for (int i = 0; i < VISIBLE_COLLUMNS; i++) {
            assertTrue(lane.canStepOn(i), "column " + i);
        }
    }

    @Test
    public void fishMovesOnlyOnStepTicks() {
        lane.update(1);
        assertTrue(lane.isDeadly(FISH_START));
        lane.update(TICKS_PER_STEP);
        assertFalse(lane.isDeadly(FISH_START));
        assertTrue(lane.isDeadly(FISH_START + FISH_LENGTH));
    }

    @Test
    public void directionMustBeOneOrMinusOne() {
        assertAll("Using invalid direction throws exception",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new FishLane(List.of(new Mover(FISH_START, FISH_LENGTH)),
                                TICKS_PER_STEP, 0)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new FishLane(List.of(new Mover(FISH_START, FISH_LENGTH)),
                                TICKS_PER_STEP, 2))
        );
    }

    @Test
    public void moverListIsCopied() {
        List<Mover> moverList = new ArrayList<>();
        moverList.add(new Mover(FISH_START, FISH_LENGTH));
        FishLane lane2 = new FishLane(moverList, TICKS_PER_STEP, 1);
        moverList.add(new Mover(20, FISH_LENGTH));
        assertFalse(lane2.isDeadly(20));
    }

    @Test
    public void getMoversCannotBeChanged() {
        assertThrows(UnsupportedOperationException.class, () -> lane.getMovers().add(new Mover(20,
                FISH_LENGTH)));
    }

}
