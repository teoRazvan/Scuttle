package scuttle.controller;

import scuttle.model.CoinAmount;
import scuttle.model.Direction;
import scuttle.model.HighScore;
import scuttle.model.World;

/**
 *
 * @author teo03
 */
public class GameController implements GameInput {

    private World world;
    private boolean paused;
    private int frameCounter;
    private boolean resultsSaved;

    public GameController() {
        world = new World();
    }

    public World getWorld() {
        return world;
    }

    public int getFrameCounter() {
        return frameCounter;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return world.isGameOver();
    }

    @Override
    public void move(Direction direction) {
        if (paused) {
            return;
        }
        world.movePlayer(direction);
    }

    @Override
    public void restart() {
        if (world.isGameOver()) {
            world = new World();
            paused = false;
            resultsSaved = false;
        }
    }

    public void reset() {
        paused = false;
        world = new World();
        resultsSaved = false;
    }

    public void tick() {
        if (!paused) {
            frameCounter++;
            world.update();
        }
        if (isGameOver() && !resultsSaved) {
            HighScore.submit(world.getScore());
            CoinAmount.add(world.getCoinAmount());
            resultsSaved = true;
        }
    }

    @Override
    public void togglePause() {
        if (world.isGameOver()) {
            return;
        }
        paused = !paused;

    }

}
