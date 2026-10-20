package scuttle.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuttle.model.Direction;
import scuttle.model.Position;
import scuttle.model.World;

public class GameControllerTest {

    GameController gameController;

    @BeforeEach
    public void setup() {
        gameController = new GameController();
    }

    @Test
    public void startsUnpausedAndAlive() {
        assertAll("not paused, not gameover",
                () -> assertFalse(gameController.isPaused()),
                () -> assertFalse(gameController.isGameOver()));
    }

    @Test
    public void togglePauseTwiceUnpauses() {
        gameController.togglePause();
        assertTrue(gameController.isPaused());
        gameController.togglePause();
        assertFalse(gameController.isPaused());
    }

    @Test
    public void moveWorksWhenNotPaused() {
        Position start = gameController.getWorld().getPlayer().getPosition();
        gameController.move(Direction.UP);
        assertEquals(start.step(Direction.UP), gameController.getWorld().getPlayer().getPosition());
    }

    @Test
    public void moveIsIgnoredWhilePaused() {
        Position start = gameController.getWorld().getPlayer().getPosition();
        gameController.togglePause();
        gameController.move(Direction.UP);
        assertEquals(start, gameController.getWorld().getPlayer().getPosition());
    }

    @Test
    public void tickCountsFramesOnlyWhenNotPaused() {
        gameController.tick();
        assertEquals(1, gameController.getFrameCounter());
        gameController.togglePause();
        gameController.tick();
        assertEquals(1, gameController.getFrameCounter());
    }

    @Test
    public void restartIsIgnoredWhileAlive() {
        World world = gameController.getWorld();
        gameController.togglePause();
        gameController.restart();
        assertSame(world, gameController.getWorld());
    }

    @Test
    public void resetGivesFreshUnpausedGame() {
        World world = gameController.getWorld();
        gameController.togglePause();
        gameController.reset();
        assertNotSame(world, gameController.getWorld());
    }

}
