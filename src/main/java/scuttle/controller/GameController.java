package scuttle.controller;

import scuttle.model.CoinAmount;
import scuttle.model.Direction;
import scuttle.model.HighScore;
import scuttle.model.World;

/**
 * Connects the input and the game loop to the model. It owns the current World, handles pausing and
 * restarting, and saves the score and coins once a game ends. It has no Swing code, so it can be
 * tested without a window.
 */
public class GameController implements GameInput {

    private World world;
    private boolean paused;
    // Ticks while not paused, helps with animation
    private int frameCounter;
    // True once this run's score and coins were saved, so they're saved only once
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
            reset();
        }
    }

    /* Starts a new game straight away, whatever the current state. Used by restart()
    and when returning to a fresh game from the menu.
     */
    public void reset() {
        world = new World();
        paused = false;
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
