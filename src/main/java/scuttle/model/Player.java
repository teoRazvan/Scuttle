package scuttle.model;

public class Player {

    private Position position;
    private Direction facing;

    public Player(Position startingPosition) {
        this.position = startingPosition;
        this.facing = Direction.UP;
    }

    public Position getPosition() {
        return this.position;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public void move(Position nextPosition, Direction facing) {
        this.facing = facing;
        this.position = nextPosition;
    }

}
