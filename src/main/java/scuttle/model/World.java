package scuttle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static scuttle.model.Constants.*;

public class World {

    private final Player player;
    private final List<Lane> lanes;
    private int tickCounter;
    private boolean isDead;

    public World() {
        this.player = new Player(new Position(VISIBLE_COLUMNS / 2, VISIBLE_ROWS / 2));
        this.lanes = new ArrayList<>();
        this.isDead = false;
        this.tickCounter = 0;

        //TEMP HardCoded Lanes
        String generationCode = "................" + "................" + "................"
                + "................" + "................" + "..C......C....C." + "....C.......C..."
                + ".CC.....CC....C." + "......C.....C..." + "..C.C.....C...CC" + "C.....CC.......C"
                + "...C.......C.C..";
        for (int i = 0; i < VISIBLE_ROWS; i++) {
            CellType[] cellArray = new CellType[VISIBLE_COLUMNS];
            for (int j = 0; j < VISIBLE_COLUMNS; j++) {
                cellArray[j] = generationCode.charAt(i * VISIBLE_COLUMNS + j) == '.' ? CellType.FREE
                        : CellType.CORAL;
            }
            lanes.addLast(new GroundLane(cellArray));
        }
        lanes.set(7, new FishLane(new ArrayList<Mover>(List.of(new Mover(1, 2), new Mover(-2, 1),
                new Mover(5, 3))), 2000, 1));
        lanes.set(8, new FishLane(new ArrayList<Mover>(List.of(new Mover(17, 2), new Mover(10, 1),
                new Mover(5, 3))), 10, -1));

    }

    public boolean isDead() {
        return isDead;
    }

    private void checkDeath() {
        isDead = lanes.get(player.getPosition().getRow()).isDeadly(player.getPosition().getColumn());
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
        if (isDead) {
            return;
        }
        Position newPosition = switch (movementDirection) {
            case DOWN ->
                newPosition = new Position(player.getPosition().getColumn(),
                player.getPosition().getRow() - 1);
            case UP ->
                newPosition = new Position(player.getPosition().getColumn(),
                player.getPosition().getRow() + 1);
            case LEFT ->
                newPosition = new Position(player.getPosition().getColumn() - 1,
                player.getPosition().getRow());
            case RIGHT ->
                newPosition = new Position(player.getPosition().getColumn() + 1,
                player.getPosition().getRow());
        };

        if (canMoveTo(newPosition)) {
            player.move(newPosition, movementDirection);
            checkDeath();
        }
    }

    public void update() {
        if (isDead) {
            return;
        }
        tickCounter++;
        for (Lane l: lanes) {
            l.update(tickCounter);
        }
        checkDeath();
    }

}
