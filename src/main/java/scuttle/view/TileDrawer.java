package scuttle.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static scuttle.model.Constants.VISIBLE_ROWS;
import static scuttle.view.ViewConstants.TILE_SIZE;

/**
 * Converts grid cells to screen pixels for one frame, and picks the tile variants.
 */
final class TileDrawer {

    //Two large primes so neighbouring cells get unrelated variants
    private static final int HASH_PRIME_ROW = 19349663;
    private static final int HASH_PRIME_COLUMN = 73856093;
    private final int cameraRow;

    TileDrawer(int cameraRow) {
        this.cameraRow = cameraRow;
    }

    /* Picks a variant from the cell’s position instead of at random, so the same cell always shows
    the same sand or coral in every frame (no flickering). Multiplying by two large primes and
    XOR-ing mixes the numbers, so neighbouring cells get unrelated variants. floorMod keeps the
    index positive.
     */
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

    /* Grid rows count upward from the bottom, but screen pixels count downward from the top. So
    the row is first made relative to the camera, then flipped (VISIBLE_ROWS - 1 - …),
    then multiplied by the tile size.*/
    private int rowToY(int row) {
        return (VISIBLE_ROWS - 1 - (row - cameraRow)) * TILE_SIZE;
    }

    private int columnToX(int column) {
        return column * TILE_SIZE;
    }

}
