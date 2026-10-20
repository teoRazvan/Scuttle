package scuttle.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static scuttle.model.Constants.VISIBLE_ROWS;
import static scuttle.view.ViewConstants.TILE_SIZE;

final class TileDrawer {

    //Two large primes so neighbouring get unrelated variants
    private final static int HASH_PRIME_ROW = 19349663;
    private final static int HASH_PRIME_COLUMN = 73856093;
    private final int cameraRow;

    TileDrawer(int cameraRow) {
        this.cameraRow = cameraRow;
    }

    static BufferedImage variant(BufferedImage[] variants, int column, int row) {
        int hash = column * HASH_PRIME_COLUMN ^ row * HASH_PRIME_ROW;
        return variants[Math.floorMod(hash, variants.length)];
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
