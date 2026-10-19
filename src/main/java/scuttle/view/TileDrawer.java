package scuttle.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static scuttle.model.Constants.VISIBLE_ROWS;
import static scuttle.view.ViewConstants.TILE_SIZE;

public final class TileDrawer {

    private final int cameraRow;

    public TileDrawer(int cameraRow) {
        this.cameraRow = cameraRow;
    }

    static BufferedImage variant(BufferedImage[] variants, int column, int row) {
        long randomHash = column * 73856093 ^ row * 19349663;
        return variants[Math.floorMod(randomHash, variants.length)];
    }

    void drawTile(Graphics2D g, BufferedImage image, int column, int row) {
        g.drawImage(image, columnToX(column), rowToY(row), TILE_SIZE, TILE_SIZE, null);
    }

    void drawTileMirrored(Graphics2D g, BufferedImage image, int column, int row) {
        g.drawImage(image, columnToX(column) + TILE_SIZE, rowToY(row), -TILE_SIZE, TILE_SIZE, null);
    }

    private int rowToY(int row) {
        return (VISIBLE_ROWS - 1 - (row - cameraRow)) * TILE_SIZE;
    }

    private int columnToX(int column) {
        return column * TILE_SIZE;
    }

}
