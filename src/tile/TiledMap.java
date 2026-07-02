package tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TiledMap {
    // TMJ rendering
    public int width, height, layerCount;
    public int[][][] layers;
    public final HashMap<Integer, BufferedImage> tileImages = new HashMap<>();

    // LDtk rendering
    public final List<DrawCmd> drawList = new ArrayList<>();

    // Collision (from LDtk IntGrid)
    public int[][] intGrid; // [col][row] — 1=solid, 0=passable
    public int intGridWidth, intGridHeight;

    public static class DrawCmd {
        public final int worldX, worldY;
        public final BufferedImage image;
        public DrawCmd(int x, int y, BufferedImage img) { worldX = x; worldY = y; image = img; }
    }
}
