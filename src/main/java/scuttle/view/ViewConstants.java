package scuttle.view;

import static scuttle.model.Constants.VISIBLE_COLUMNS;
import static scuttle.model.Constants.VISIBLE_ROWS;

public final class ViewConstants {

    static final int ORIGINAL_TILE_SIZE = 16;
    static final int SCALE = 3;

    static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    static final int SCREEN_WIDTH = VISIBLE_COLUMNS * TILE_SIZE;
    static final int SCREEN_HEIGHT = VISIBLE_ROWS * TILE_SIZE;

    private ViewConstants() {

    }

}
