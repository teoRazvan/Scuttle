package scuttle.controller;

import scuttle.model.Direction;

public interface GameInput {

    void move(Direction direction);

    void restart();

    void togglePause();

}
