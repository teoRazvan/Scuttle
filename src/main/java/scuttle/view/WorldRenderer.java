package scuttle.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.util.List;
import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;
import scuttle.model.Lane;
import scuttle.model.Player;
import scuttle.model.Position;
import scuttle.model.TorpedoLane;
import scuttle.model.World;
import static scuttle.view.ViewConstants.SCREEN_HEIGHT;
import static scuttle.view.ViewConstants.SCREEN_WIDTH;

public final class WorldRenderer {

    private final LaneRenderer laneRenderer = new LaneRenderer();
    private static final Color UNDERWATER_TINT = new Color(10, 60, 140, 85);

    private static final Color VIGNETTE_EDGE = new Color(0, 15, 45, 170);
    private static final Color VIGNETTE_CENTER = new Color(0, 15, 45, 0);
    private final RadialGradientPaint vignette;

    public WorldRenderer() {
        this.vignette = new RadialGradientPaint(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, SCREEN_WIDTH
                * 0.6f, new float[]{0.35f, 1f}, new Color[]{VIGNETTE_CENTER, VIGNETTE_EDGE});
    }

    private void drawTint(Graphics2D g) {
        g.setColor(UNDERWATER_TINT);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setPaint(vignette);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void drawEdges(Graphics2D g, TileDrawer tiles, int cameraRow, int row) {
        if (row == cameraRow) {
            tiles.drawTile(g, Sprites.EDGE_CORNER_LEFT, 0, row);
            tiles.drawTile(g, Sprites.EDGE_CORNER_RIGHT, VISIBLE_COLUMNS - 1, row);
            for (int column = 1; column < VISIBLE_COLUMNS - 1; column++) {
                tiles.drawTile(g, tiles.variant(Sprites.EDGE_BOTTOM, column, row), column, row);
            }
        } else {
            tiles.drawTile(g, tiles.variant(Sprites.EDGE_LEFT, 0, row), 0, row);
            tiles.drawTile(g, tiles.variant(Sprites.EDGE_RIGHT, VISIBLE_COLUMNS - 1, row),
                    VISIBLE_COLUMNS - 1,
                    row);
        }
    }

    private void drawLanes(Graphics2D g, TileDrawer tiles, World world, int frame) {
        List<Lane> lanes = world.getLanes();

        for (int row = world.getCameraRow(); row < Math.min(VISIBLE_ROWS + world.getCameraRow(),
                lanes.size()); row++) {
            Lane lane = lanes.get(row);

            laneRenderer.draw(g, tiles, lane, row, frame);

            this.drawEdges(g, tiles, world.getCameraRow(), row);
            if (lane instanceof TorpedoLane) {
                laneRenderer.drawLauncher(g, tiles, (TorpedoLane) lane, row, frame);
            }
        }
    }

    private void drawCoins(Graphics2D g, TileDrawer tiles, World world) {

        for (Position coin: world.getCoins()) {
            tiles.drawTile(g, Sprites.COIN, coin.getColumn(), coin.getRow());
        }
    }

    private void drawPlayer(Graphics2D g, TileDrawer tiles, World world) {
        Player player = world.getPlayer();
        if (world.isGameOver()) {
            tiles.drawTile(g, Sprites.CRAB_DEAD, player.getPosition().getColumn(),
                    player.getPosition().getRow());
        } else {
            tiles.drawTile(g, Sprites.crab(player.getFacing()), player.getPosition().getColumn(),
                    player.getPosition().getRow());
        }
    }

    void draw(Graphics2D g, World world, int frame) {
        TileDrawer tiles = new TileDrawer(world.getCameraRow());
        drawLanes(g, tiles, world, frame);
        drawCoins(g, tiles, world);
        drawPlayer(g, tiles, world);
        drawTint(g);
    }

}
