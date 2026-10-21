package scuttle.view;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import scuttle.model.Direction;
import scuttle.model.Skins;

public final class Sprites {

    static final BufferedImage COIN = GuiAssets.load("/sprites/coin.png");

    //lanes
    static final BufferedImage[] SAND = loadVariants("sand", 3);
    static final BufferedImage[] CORAL = loadVariants("coral", 4);
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

    private static final Map<String, CrabFrames> CRABS = new HashMap<>();

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
        return frames(Skins.equippedId()).facing(direction);
    }

    static BufferedImage crabDead() {
        return frames(Skins.equippedId()).dead;
    }

    static BufferedImage preview(String id) {
        return frames(id).down;
    }

    private static CrabFrames frames(String id) {
        return CRABS.computeIfAbsent(id, CrabFrames::load);
    }

    private static final class CrabFrames {

        private final BufferedImage up;
        private final BufferedImage down;
        private final BufferedImage left;
        private final BufferedImage right;
        private final BufferedImage dead;

        private CrabFrames(BufferedImage up, BufferedImage down, BufferedImage left,
                BufferedImage right, BufferedImage dead) {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;
            this.dead = dead;
        }

        private static CrabFrames load(String id) {
            return new CrabFrames(
                    pose(id, "up"),
                    pose(id, "down"),
                    pose(id, "left"),
                    pose(id, "right"),
                    pose(id, "dead"));
        }

        private static BufferedImage pose(String id, String pose) {
            String path = Skins.DEFAULT_ID.equals(id)
                    ? "/sprites/crab_" + pose + ".png"
                    : "/skins/" + id + "/crab_" + pose + ".png";
            return GuiAssets.load(path);
        }

        private BufferedImage facing(Direction direction) {
            return switch (direction) {
                case UP ->
                    up;
                case DOWN ->
                    down;
                case RIGHT ->
                    right;
                case LEFT ->
                    left;
            };
        }
    }

}
