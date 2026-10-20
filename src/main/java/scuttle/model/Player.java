package scuttle.model;

public final class Player {

    private Position position;
    private Direction facing;

    public Player(Position startingPosition) {
        this.position = startingPosition;
        this.facing = Direction.UP;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getFacing() {
        return facing;
    }

    public void move(Position nextPosition, Direction facing) {
        this.facing = facing;
        this.position = nextPosition;
    }

}
