package scuttle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static scuttle.model.Constants.CAMERA_OFFSET;
import static scuttle.model.Constants.LANES_AHEAD;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.GameRules.COIN_GROUND_CHANCE;

public class World {

    private final Player player;
    private final List<Lane> lanes;
    private final Set<Position> coins = new HashSet<>();
    private final Random random = new Random();
    private int tickCounter;
    private boolean gameOver;
    private int score, furthestRow;
    private int coinsCollected;
    private final LaneGenerator generator = new LaneGenerator();
    private int cameraRow;

    public World() {
        this.cameraRow = 0;
        this.player = new Player(new Position(VISIBLE_COLUMNS / 2, 2));
        this.lanes = new ArrayList<>();
        this.gameOver = false;
        this.tickCounter = 0;
        this.score = 0;
        this.furthestRow = player.getPosition().getRow();
        this.coinsCollected = 0;

        generateAhead();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void checkDeath() {
        gameOver = lanes.get(player.getPosition().getRow()).isDeadly(
                player.getPosition().getColumn());
    }

    public int getCameraRow() {
        return cameraRow;
    }

    public int getScore() {
        return score;
    }

    private Set<Position> reachableCells() {
        return Reachability.reachable(lanes, List.of(player.getPosition()), cameraRow, lanes.size());
    }

    public int getCoinAmount() {
        return coinsCollected;
    }

    public Set<Position> getCoins() {
        return Collections.unmodifiableSet(coins);
    }

    private void generateAhead() {
        int fromRow = lanes.size();
        while (lanes.size() < player.getPosition().getRow() + LANES_AHEAD) {
            lanes.add(generator.next());
        }
        scatterCoinsOnRows(fromRow, lanes.size());
    }

    private boolean canMoveTo(Position p) {
        //TODO Helper method
        return (p.getColumn() >= 0 && p.getColumn() < VISIBLE_COLUMNS) && (p.getRow() >= cameraRow
                && p.getRow() < lanes.size())
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
        Position newPosition = player.getPosition().step(movementDirection);

        if (canMoveTo(newPosition)) {
            generateAhead();
            player.move(newPosition, movementDirection);
            cameraRow = Math.max(cameraRow, player.getPosition().getRow() - CAMERA_OFFSET);
            collectCoinAtPlayer();
            checkDeath();
        }
        int row = player.getPosition().getRow();
        if (row > furthestRow) {
            score += row - furthestRow;
            furthestRow = row;
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
            collectCoinAtPlayer();
        }
        for (Lane l: lanes) {
            l.update(tickCounter);
        }
        checkDeath();
    }

    private void scatterCoinsOnRows(int fromRow, int toRow) {
        Position playerPosition = player.getPosition();
        Set<Position> reachable = reachableCells();

        for (int row = fromRow; row < toRow; row++) {
            Lane lane = lanes.get(row);
            if (lane.getType() != LaneType.GROUND) {
                continue;
            }
            if (random.nextInt(100) >= COIN_GROUND_CHANCE) {
                continue;
            }
            List<Integer> columns = new ArrayList<>();
            for (int column = 0; column < VISIBLE_COLUMNS; column++) {
                if (reachable.contains(new Position(column, row))) {
                    columns.add(column);
                }
            }
            if (columns.isEmpty()) {
                continue;
            }
            Position cell = new Position(columns.get(random.nextInt(columns.size())), row);
            if (!cell.equals(playerPosition)) {
                coins.add(cell);
            }
        }
    }

    private void spawnCoins(int count) {
        List<Position> candidates = new ArrayList<>();
        Position playerPosition = player.getPosition();
        Set<Position> reachable = reachableCells();

        for (int row = cameraRow; row < lanes.size(); row++) {
            Lane lane = lanes.get(row);
            if (lane.getType() != LaneType.GROUND) {
                continue;
            }
            for (int column = 0; column < VISIBLE_COLUMNS; column++) {
                if (!reachable.contains(new Position(column, row))) {
                    continue;
                }
                Position cell = new Position(column, row);
                if (!cell.equals(playerPosition) && !coins.contains(cell)) {
                    candidates.add(cell);
                }
            }
        }
        Collections.shuffle(candidates, random);
        int toPlace = Math.min(count, candidates.size());
        for (int i = 0; i < toPlace; i++) {
            coins.add(candidates.get(i));
        }
    }

    private void collectCoinAtPlayer() {
        if (coins.remove(player.getPosition())) {
            coinsCollected++;
            spawnCoins(1);
        }
    }

}
