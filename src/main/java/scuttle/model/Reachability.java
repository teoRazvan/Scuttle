package scuttle.model;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import static scuttle.model.Constants.VISIBLE_COLLUMNS;

public final class Reachability {

    private Reachability() {
    }

    public static Set<Position> reachable(List<Lane> lanes, Collection<Position> starts,
            int fromRow, int toRow) {
        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new ArrayDeque<>();
        for (Position start: starts) {
            if (!visited.contains(start) && isOpen(lanes, start, fromRow, toRow)) {
                visited.add(start);
                queue.add(start);
            }
        }
        while (!queue.isEmpty()) {
            Position current = queue.poll();
            for (Direction direction: Direction.values()) {
                Position neighbour = current.step(direction);
                if (!visited.contains(neighbour) && isOpen(lanes, neighbour, fromRow, toRow)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        return visited;
    }

    private static boolean isOpen(List<Lane> lanes, Position position, int fromRow, int toRow) {
        return position.getColumn() >= 0 && position.getColumn() < VISIBLE_COLLUMNS
                && position.getRow() >= fromRow && position.getRow() < toRow
                && lanes.get(position.getRow()).canStepOn(position.getColumn());
    }

}
