package scuttle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;

public class World {

    private final Player player;
    private final List<Lane> lanes;
    private int tickCounter;
    private boolean gameOver;

    public World() {
        this.player = new Player(new Position(VISIBLE_COLUMNS / 2, VISIBLE_ROWS / 2));
        this.lanes = new ArrayList<>();
        this.gameOver = false;
        this.tickCounter = 0;

        //TEMP HardCoded Lanes
        String generationCode = "............................\n" + "............................\n"
                + "...C....CC......C.....CC..C.\n" + "............................\n"
                + ".C....C.C....CC.....C....C..\n" + "............................\n"
                + "............................\n" + ".....C.........C..C.......C.\n"
                + "CC...C...CCC....C....CC..C.C\n" + "............................\n"
                + "............................\n" + "...C.....C.....C......C...C.\n"
                + "CCCC..CCCCCC..CCCC..CCCC..CC\n" + "............................\n"
                + "..C....CC.....C....C.C...C..\n" + "............................\n"
                + "............................\n" + "............................";
        for (int i = 0; i < VISIBLE_ROWS; i++) {
            CellType[] cellArray = new CellType[VISIBLE_COLUMNS];
            for (int j = 0; j < VISIBLE_COLUMNS; j++) {
                cellArray[j] = generationCode.charAt(i * VISIBLE_COLUMNS + j) == '.' ? CellType.FREE
                        : CellType.CORAL;
            }
            lanes.addLast(new GroundLane(cellArray));
        }
        lanes.set(7, new FishLane(new ArrayList<Mover>(List.of(new Mover(1, 2), new Mover(-2, 1),
                new Mover(5, 3))), 30, 1));
        lanes.set(8, new FishLane(new ArrayList<Mover>(List.of(new Mover(17, 2), new Mover(10, 1),
                new Mover(5, 3))), 10, -1));
        lanes.set(3, new RaftLane(new ArrayList<Mover>(List.of(new Mover(20, 4), new Mover(10, 5),
                new Mover(5, 3))), 10, -1));

    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void checkDeath() {
        gameOver = lanes.get(player.getPosition().getRow()).isDeadly(
                player.getPosition().getColumn());
    }

    private boolean canMoveTo(Position p) {
        //TODO Helper method
        return (p.getColumn() >= 0 && p.getColumn() < VISIBLE_COLUMNS) && (p.getRow() >= 0
                && p.getRow() < VISIBLE_ROWS)
                && lanes.get(p.getRow()).canStepOn(p.getColumn());
    }

    public List<Lane> getLanes() {
        return Collections.unmodifiableList(lanes);
    }

    public Player getPlayer() {
        return player;
    }

    public void movePlayer(Direction movementDirection) {
        if (gameOver) {
            return;
        }
        Position newPosition = switch (movementDirection) {
            case DOWN ->
                new Position(player.getPosition().getColumn(),
                player.getPosition().getRow() - 1);
            case UP ->
                new Position(player.getPosition().getColumn(),
                player.getPosition().getRow() + 1);
            case LEFT ->
                new Position(player.getPosition().getColumn() - 1,
                player.getPosition().getRow());
            case RIGHT ->
                new Position(player.getPosition().getColumn() + 1,
                player.getPosition().getRow());
        };

        if (canMoveTo(newPosition)) {
            player.move(newPosition, movementDirection);
            checkDeath();
        }
    }

    public void update() {
        if (gameOver) {
            return;
        }
        tickCounter++;
        if (lanes.get(player.getPosition().getRow()).drift(tickCounter) != 0) {
            Position newPosition = new Position(player.getPosition().getColumn() + lanes.get(
                    player.getPosition().getRow()).drift(
                    tickCounter), player.getPosition().getRow());
            player.move(newPosition, player.getFacing());

        }
        for (Lane l: lanes) {
            l.update(tickCounter);
        }
        checkDeath();
    }

}
