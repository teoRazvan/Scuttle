package scuttle.view;

import java.awt.image.BufferedImage;
import scuttle.model.Direction;

public final class Sprites {

    static final BufferedImage COIN = GuiAssets.load("/sprites/coin.png");

    //lanes
    static final BufferedImage[] SAND = loadVariants("sand", 3);
    static final BufferedImage[] CORAL = loadVariants("coral", 3);
    static final BufferedImage[] TATTERED = loadVariants("tattered", 2);
    static final BufferedImage[] CHASM = loadVariants("chasm", 2);

    //fish
    static final BufferedImage FISH_SINGLE = GuiAssets.load("/sprites/fish_single.png");
    static final BufferedImage FISH_HEAD = GuiAssets.load("/sprites/fish_head.png");
    static final BufferedImage FISH_BODY = GuiAssets.load("/sprites/fish_body.png");
    static final BufferedImage FISH_TAIL = GuiAssets.load("/sprites/fish_tail.png");

    //rafts
    static final BufferedImage JELLY_LEFT = GuiAssets.load("/sprites/jelly_left.png");
    static final BufferedImage JELLY_MID = GuiAssets.load("/sprites/jelly_mid.png");
    static final BufferedImage JELLY_RIGHT = GuiAssets.load("/sprites/jelly_right.png");

    //torpedo
    static final BufferedImage TORPEDO_HAZARD = GuiAssets.load("/sprites/torpedo_hazard.png");
    static final BufferedImage TORPEDO_LAUNCHER = GuiAssets.load("/sprites/torpedo_launcher.png");
    static final BufferedImage[] TORPEDO_TRACK = loadVariants("torpedo_track", 2);
    static final BufferedImage[] TORPEDO_BLAST = loadVariants("torpedo_blast", 2);
    static final BufferedImage[] LAUNCHER_FIRE = loadVariants("torpedo_launcher_fire", 3);
    static final BufferedImage[] TORPEDO_BLAST_START = loadVariants("torpedo_blast_start", 2);

    //player
    static final BufferedImage CRAB_DEAD = GuiAssets.load("/sprites/crab_dead.png");
    private static final BufferedImage CRAB_UP = GuiAssets.load("/sprites/crab_up.png");
    private static final BufferedImage CRAB_DOWN = GuiAssets.load("/sprites/crab_down.png");
    private static final BufferedImage CRAB_LEFT = GuiAssets.load("/sprites/crab_left.png");
    private static final BufferedImage CRAB_RIGHT = GuiAssets.load("/sprites/crab_right.png");

    //map edges
    static final BufferedImage[] EDGE_LEFT = loadVariants("edge_left", 2);
    static final BufferedImage[] EDGE_RIGHT = loadVariants("edge_right", 2);
    static final BufferedImage[] EDGE_BOTTOM = loadVariants("edge_back", 2);
    static final BufferedImage EDGE_CORNER_LEFT = GuiAssets.load(
            "/sprites/edge_corner_left.png");
    static final BufferedImage EDGE_CORNER_RIGHT = GuiAssets.load(
            "/sprites/edge_corner_right.png");

    static final BufferedImage HUD_PANEL = GuiAssets.load("/sprites/hud_panel.png");

    private Sprites() {
    }

    private static BufferedImage[] loadVariants(String name, int count) {
        BufferedImage[] sprites = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            sprites[i] = GuiAssets.load("/sprites/" + name + "_" + i + ".png");
        }
        return sprites;
    }

    static BufferedImage crab(Direction direction) {
        return switch (direction) {
            case UP ->
                CRAB_UP;
            case DOWN ->
                CRAB_DOWN;
            case RIGHT ->
                CRAB_RIGHT;
            case LEFT ->
                CRAB_LEFT;
        };
    }

}
