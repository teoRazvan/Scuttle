package scuttle.view;

import scuttle.model.Direction;

public interface GameInput {

    void move(Direction direction);

    void restart();

}
