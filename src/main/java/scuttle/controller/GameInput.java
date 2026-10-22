package scuttle.controller;

import scuttle.model.Direction;

/**
 * The actions the player can trigger: moving, restarting and pausing. The view only talks to the
 * game through this interface, so it never changes the model directly.
 */
public interface GameInput {

    void move(Direction direction);

    void restart();

    void togglePause();

}
