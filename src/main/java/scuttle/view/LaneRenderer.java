package scuttle.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import scuttle.model.FishLane;
import scuttle.model.GroundLane;
import scuttle.model.Lane;
import scuttle.model.Mover;
import scuttle.model.RaftLane;
import scuttle.model.TorpedoLane;
import static scuttle.model.TorpedoLane.LAUNCHER_COLUMN;

/**
 *
 * @author teo03
 */
public class LaneRenderer {

    private void drawGroundLane(Graphics2D g, TileDrawer tiles, GroundLane lane, int row) {
        for (int column = 0; column < VISIBLE_COLUMNS; column++) {
            tiles.drawTile(g, tiles.variant(Sprites.SAND, column, row), column, row);
            if (!lane.canStepOn(column)) {
                tiles.drawTile(g, tiles.variant(Sprites.CORAL, column, row), column, row);
            }

        }
    }

    private BufferedImage getFishSprite(int i, int length, int direction) {
        if (length == 1) {
            return Sprites.FISH_SINGLE;
        } else {
            if (direction == 1) {
                if (i == length - 1) {
                    return Sprites.FISH_HEAD;
                } else if (i == 0) {
                    return Sprites.FISH_TAIL;
                } else {
                    return Sprites.FISH_BODY;
                }
            } else {
                if (i == 0) {
                    return Sprites.FISH_HEAD;
                } else if (i == length - 1) {
                    return Sprites.FISH_TAIL;
                } else {
                    return Sprites.FISH_BODY;
                }
            }
        }
    }

    private void drawFishLane(Graphics2D g, TileDrawer tiles, FishLane lane, int row) {
        for (int column = 0; column < VISIBLE_COLUMNS; column++) {
            tiles.drawTile(g, tiles.variant(Sprites.TATTERED, column, row), column, row);
        }
        for (Mover m: lane.getFishes()) {
            for (int i = 0; i < m.getLength(); i++) {
                if (lane.getDirection() == 1) {
                    tiles.drawTile(g, getFishSprite(i, m.getLength(), lane.getDirection()),
                            m.getStartCell() + i, row);
                } else {
                    tiles.drawTileMirrored(g, getFishSprite(i, m.getLength(), lane.getDirection()),
                            m.getStartCell() + i, row);
                }
            }
        }
    }

    private void drawRaftLane(Graphics2D g, TileDrawer tiles, RaftLane lane, int row) {
        for (int column = 0; column < VISIBLE_COLUMNS; column++) {
            tiles.drawTile(g, tiles.variant(Sprites.CHASM, column, row), column, row);
        }
        for (Mover m: lane.getRafts()) {
            for (int i = 0; i < m.getLength(); i++) {
                if (i == 0) {
                    tiles.drawTile(g, Sprites.JELLY_LEFT, m.getStartCell() + i, row);
                } else if (i == m.getLength() - 1) {
                    tiles.drawTile(g, Sprites.JELLY_RIGHT, m.getStartCell() + i, row);
                } else {
                    tiles.drawTile(g, Sprites.JELLY_MID, m.getStartCell() + i, row);
                }
            }
        }

    }

    void drawLauncher(Graphics2D g, TileDrawer tiles, TorpedoLane lane, int row, int frame) {
        if (lane.isFiring()) {
            tiles.drawTile(g, Sprites.TORPEDO_BLAST_START[(frame / 3) % 2], LAUNCHER_COLUMN, row);
            tiles.drawTile(g, Sprites.LAUNCHER_FIRE[(frame / 3) % 3], LAUNCHER_COLUMN, row);
        } else if (lane.isWarning()) {
            tiles.drawTile(g, Sprites.LAUNCHER_FIRE[(frame / 3) % 3], LAUNCHER_COLUMN, row);

        } else {
            tiles.drawTile(g, Sprites.TORPEDO_LAUNCHER, LAUNCHER_COLUMN, row);

        }
    }

    private void drawTorpedoLane(Graphics2D g, TileDrawer tiles, TorpedoLane lane, int row,
            int frame) {
        for (int column = 1; column < VISIBLE_COLUMNS; column++) {
            tiles.drawTile(g, tiles.variant(Sprites.TORPEDO_TRACK, column, row), column, row);
            if (lane.isWarning() && (frame / 10) % 2 == 0) {
                tiles.drawTile(g, Sprites.TORPEDO_HAZARD, column, row);
            } else if (lane.isFiring()) {
                tiles.drawTile(g, Sprites.TORPEDO_BLAST[(frame / 3) % 2],
                        column, row);
            }
        }
        tiles.drawTile(g, tiles.variant(Sprites.TORPEDO_TRACK, 0, row), 0, row);
    }

    public void draw(Graphics2D g, TileDrawer tiles, Lane lane, int row, int frame) {
        switch (lane) {
            case GroundLane ground -> {
                drawGroundLane(g, tiles, ground, row);
            }
            case FishLane fish -> {
                drawFishLane(g, tiles, fish, row);
            }
            case RaftLane raft -> {
                drawRaftLane(g, tiles, raft, row);
            }
            case TorpedoLane torpedo -> {
                drawTorpedoLane(g, tiles, torpedo, row, frame);
            }
            default ->
                throw new IllegalStateException("Unexpected value: " + lane);
        }
    }

}
