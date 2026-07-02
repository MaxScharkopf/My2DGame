package world;

import main.GamePanel;
import tile.Tile;
import tile.TiledMap;
import util.UtilityTool;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;
    public TiledMap[] tiledMaps;
    boolean drawPath = true;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[50];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        tiledMaps = new TiledMap[gp.maxMap];

        getTileImage();
        loadMap("/maps/worldV3.txt", 0);
        loadMap("/maps/interior01.txt", 1);
        loadLdtkMap("/maps/testmap.ldtk", 0);
    }

    // -------------------------------------------------------------------------
    // Legacy tile image setup
    // -------------------------------------------------------------------------

    public void getTileImage() {
        setup(0,"grass00", false);
        setup(1,"grass00", false);
        setup(2,"grass00", false);
        setup(3,"grass00", false);
        setup(4,"grass00", false);
        setup(5,"grass00", false);
        setup(6,"blossom_tree_bottom_left", true);
        setup(7,"blossom_tree_bottom_right", true);
        setup(8,"blossom_tree_top_left", true);
        setup(9,"blossom_tree_top_right", true);
        setup(10,"grass00", false);
        setup(11,"grass01", false);
        setup(12,"water00", true);
        setup(13,"water01", true);
        setup(14,"water02", true);
        setup(15,"water03", true);
        setup(16,"water04", true);
        setup(17,"water05", true);
        setup(18,"water06", true);
        setup(19,"water07", true);
        setup(20,"water08", true);
        setup(21,"water09", true);
        setup(22,"water10", true);
        setup(23,"water11", true);
        setup(24,"water12", true);
        setup(25,"water13", true);
        setup(26,"road00", false);
        setup(27,"road01", false);
        setup(28,"road02", false);
        setup(29,"road03", false);
        setup(30,"road04", false);
        setup(31,"road05", false);
        setup(32,"road06", false);
        setup(33,"road07", false);
        setup(34,"road08", false);
        setup(35,"road09", false);
        setup(36,"road10", false);
        setup(37,"road11", false);
        setup(38,"road12", false);
        setup(39,"earth", false);
        setup(40,"wall", true);
        setup(41,"tree", true);
        setup(42,"hut", false);
        setup(43,"floor01", false);
        setup(44,"table01", true);
    }

    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Legacy .txt map loading
    // -------------------------------------------------------------------------

    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    mapTileNum[map][col][row] = Integer.parseInt(numbers[col]);
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception ignored) {
        }
    }

    // -------------------------------------------------------------------------
    // LDtk map loading
    // -------------------------------------------------------------------------

    public void loadLdtkMap(String ldtkPath, int mapIndex) {
        try {
            InputStream is = getClass().getResourceAsStream(ldtkPath);
            if (is == null) { System.err.println("LDtk file not found: " + ldtkPath); return; }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            String json = sb.toString();

            TiledMap tm = new TiledMap();
            Map<String, BufferedImage> sheetCache = new HashMap<>();
            UtilityTool uTool = new UtilityTool();

            // Get first level
            List<String> levels = extractJsonObjects(json, "levels");
            if (levels.isEmpty()) { System.err.println("No levels found in " + ldtkPath); return; }
            String level = levels.get(0);

            // Parse layer instances — LDtk lists them top-to-bottom, reverse for bottom-up drawing
            List<String> layerObjs = extractJsonObjects(level, "layerInstances");
            Collections.reverse(layerObjs);

            for (String layerObj : layerObjs) {
                String type = extractJsonString(layerObj, "__type");
                if (type == null) continue;

                if ("IntGrid".equals(type)) {
                    int cWid = extractJsonInt(layerObj, "__cWid");
                    int cHei = extractJsonInt(layerObj, "__cHei");
                    int[] csv = extractJsonIntArray(layerObj, "intGridCsv");
                    tm.intGrid = new int[cWid][cHei];
                    tm.intGridWidth = cWid;
                    tm.intGridHeight = cHei;
                    for (int i = 0; i < csv.length; i++) {
                        tm.intGrid[i % cWid][i / cWid] = csv[i];
                    }

                } else if ("Tiles".equals(type)) {
                    String tilesetPath = extractJsonString(layerObj, "__tilesetRelPath");
                    if (tilesetPath == null) continue;
                    String imageName = stripPath(tilesetPath);
                    int gridSize = extractJsonInt(layerObj, "__gridSize");
                    if (gridSize == 0) gridSize = 16;

                    BufferedImage sheet = sheetCache.get(imageName);
                    if (sheet == null) {
                        InputStream imgIs = getClass().getResourceAsStream("/tiles/" + imageName);
                        if (imgIs == null) { System.err.println("Tileset image not found: " + imageName); continue; }
                        sheet = ImageIO.read(imgIs);
                        sheetCache.put(imageName, sheet);
                    }

                    for (String tileObj : extractJsonObjects(layerObj, "gridTiles")) {
                        int[] px  = extractJsonIntArray(tileObj, "px");
                        int[] src = extractJsonIntArray(tileObj, "src");
                        if (px.length < 2 || src.length < 2) continue;

                        int srcX = src[0], srcY = src[1];
                        if (srcX + gridSize > sheet.getWidth() || srcY + gridSize > sheet.getHeight()) continue;

                        BufferedImage slice  = sheet.getSubimage(srcX, srcY, gridSize, gridSize);
                        BufferedImage scaled = uTool.scaleImage(slice, gp.tileSize, gp.tileSize);
                        int worldX = px[0] * gp.tileSize / gridSize;
                        int worldY = px[1] * gp.tileSize / gridSize;
                        tm.drawList.add(new TiledMap.DrawCmd(worldX, worldY, scaled));
                    }
                }
            }

            tiledMaps[mapIndex] = tm;
            System.out.println("Loaded LDtk map: " + ldtkPath +
                " (" + tm.drawList.size() + " tiles, intGrid: " + tm.intGridWidth + "x" + tm.intGridHeight + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Legacy .tmj loading (kept for reference, unused)
    // -------------------------------------------------------------------------

    private static class TilesetInfo {
        final int firstgid, columns, tileWidth, tileHeight;
        final String imageName;

        TilesetInfo(int firstgid, int columns, int tileWidth, int tileHeight, String imageName) {
            this.firstgid = firstgid;
            this.columns = columns;
            this.tileWidth = tileWidth;
            this.tileHeight = tileHeight;
            this.imageName = imageName;
        }
    }

    public void loadTiledMap(String tmjPath, int mapIndex) {
        try {
            InputStream is = getClass().getResourceAsStream(tmjPath);
            if (is == null) { System.err.println("TMJ not found: " + tmjPath); return; }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            String json = sb.toString();

            List<TilesetInfo> tilesets = new ArrayList<>();
            for (String tsObj : extractJsonObjects(json, "tilesets")) {
                int firstgid  = extractJsonInt(tsObj, "firstgid");
                String source = extractJsonString(tsObj, "source");
                if (source != null) {
                    TilesetInfo info = parseTsx(source, firstgid);
                    if (info != null) tilesets.add(info);
                } else {
                    String image   = extractJsonString(tsObj, "image");
                    int columns    = extractJsonInt(tsObj, "columns");
                    int tileWidth  = extractJsonInt(tsObj, "tilewidth");
                    int tileHeight = extractJsonInt(tsObj, "tileheight");
                    if (image != null && columns > 0) {
                        tilesets.add(new TilesetInfo(firstgid, columns, tileWidth, tileHeight, stripPath(image)));
                    }
                }
            }
            tilesets.sort(Comparator.comparingInt(t -> t.firstgid));

            List<int[][]> layerList = new ArrayList<>();
            int mapWidth = 0, mapHeight = 0;

            for (String layerObj : extractJsonObjects(json, "layers")) {
                if (!layerObj.contains("\"type\":\"tilelayer\"")) continue;
                int w = extractJsonInt(layerObj, "width");
                int h = extractJsonInt(layerObj, "height");
                if (mapWidth == 0) { mapWidth = w; mapHeight = h; }
                int[] flat = extractJsonIntArray(layerObj, "data");
                int[][] grid = new int[w][h];
                for (int i = 0; i < flat.length; i++) {
                    grid[i % w][i / w] = flat[i];
                }
                layerList.add(grid);
            }

            if (mapWidth == 0 || layerList.isEmpty()) return;

            TiledMap tm = new TiledMap();
            tm.width      = mapWidth;
            tm.height     = mapHeight;
            tm.layerCount = layerList.size();
            tm.layers     = layerList.toArray(new int[0][][]);

            Map<String, BufferedImage> sheetCache = new HashMap<>();
            UtilityTool uTool = new UtilityTool();
            for (int[][] layer : tm.layers) {
                for (int col = 0; col < mapWidth; col++) {
                    for (int row = 0; row < mapHeight; row++) {
                        int gid = layer[col][row];
                        if (gid == 0 || tm.tileImages.containsKey(gid)) continue;
                        BufferedImage img = buildTileImage(gid, tilesets, sheetCache, uTool);
                        if (img != null) tm.tileImages.put(gid, img);
                    }
                }
            }

            tiledMaps[mapIndex] = tm;
            System.out.println("Loaded TMJ map: " + tmjPath + " (" + tm.tileImages.size() + " unique tiles across " + tm.layerCount + " layers)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TilesetInfo parseTsx(String source, int firstgid) {
        try {
            InputStream is = getClass().getResourceAsStream("/maps/" + source);
            if (is == null) { System.err.println("TSX not found: " + source); return null; }

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(is);
            Element root = doc.getDocumentElement();

            int columns    = Integer.parseInt(root.getAttribute("columns"));
            int tileWidth  = Integer.parseInt(root.getAttribute("tilewidth"));
            int tileHeight = Integer.parseInt(root.getAttribute("tileheight"));

            Element imageEl = (Element) root.getElementsByTagName("image").item(0);
            String imageName = stripPath(imageEl.getAttribute("source"));

            return new TilesetInfo(firstgid, columns, tileWidth, tileHeight, imageName);
        } catch (Exception e) {
            System.err.println("Failed to parse TSX: " + source);
            return null;
        }
    }

    private BufferedImage buildTileImage(int gid, List<TilesetInfo> tilesets,
                                         Map<String, BufferedImage> sheetCache,
                                         UtilityTool uTool) {
        TilesetInfo ts = null;
        for (TilesetInfo info : tilesets) {
            if (info.firstgid <= gid && (ts == null || info.firstgid > ts.firstgid)) ts = info;
        }
        if (ts == null) return null;

        int localId = gid - ts.firstgid;
        int col = localId % ts.columns;
        int row = localId / ts.columns;
        int x = col * ts.tileWidth;
        int y = row * ts.tileHeight;

        BufferedImage sheet = sheetCache.get(ts.imageName);
        if (sheet == null) {
            try {
                InputStream is = getClass().getResourceAsStream("/tiles/" + ts.imageName);
                if (is == null) return null;
                sheet = ImageIO.read(is);
                sheetCache.put(ts.imageName, sheet);
            } catch (IOException e) {
                return null;
            }
        }

        if (x + ts.tileWidth > sheet.getWidth() || y + ts.tileHeight > sheet.getHeight()) return null;
        BufferedImage slice = sheet.getSubimage(x, y, ts.tileWidth, ts.tileHeight);
        return uTool.scaleImage(slice, gp.tileSize, gp.tileSize);
    }

    // -------------------------------------------------------------------------
    // Draw
    // -------------------------------------------------------------------------

    public void draw(Graphics2D g2) {
        TiledMap tm = tiledMaps[gp.currentMap];
        if (tm != null) {
            drawTiledMap(g2, tm);
        } else {
            drawLegacyMap(g2);
        }

        if (drawPath) {
            g2.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX  = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY  = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }
    }

    private void drawTiledMap(Graphics2D g2, TiledMap tm) {
        if (!tm.drawList.isEmpty()) {
            // LDtk path
            for (TiledMap.DrawCmd cmd : tm.drawList) {
                int screenX = cmd.worldX - gp.player.worldX + gp.player.screenX;
                int screenY = cmd.worldY - gp.player.worldY + gp.player.screenY;
                if (cmd.worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    cmd.worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    cmd.worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    cmd.worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    g2.drawImage(cmd.image, screenX, screenY, null);
                }
            }
        } else if (tm.layers != null) {
            // TMJ path
            for (int layer = 0; layer < tm.layerCount; layer++) {
                for (int worldCol = 0; worldCol < tm.width; worldCol++) {
                    for (int worldRow = 0; worldRow < tm.height; worldRow++) {
                        int gid = tm.layers[layer][worldCol][worldRow];
                        if (gid == 0) continue;
                        int worldX  = worldCol * gp.tileSize;
                        int worldY  = worldRow * gp.tileSize;
                        int screenX = worldX - gp.player.worldX + gp.player.screenX;
                        int screenY = worldY - gp.player.worldY + gp.player.screenY;
                        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                            BufferedImage img = tm.tileImages.get(gid);
                            if (img != null) g2.drawImage(img, screenX, screenY, null);
                        }
                    }
                }
            }
        }
    }

    private void drawLegacyMap(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
            int worldX  = worldCol * gp.tileSize;
            int worldY  = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    // -------------------------------------------------------------------------
    // JSON helpers
    // -------------------------------------------------------------------------

    private static int extractJsonInt(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) return 0;
        idx += search.length();
        while (idx < json.length() && (json.charAt(idx) == ' ' || json.charAt(idx) == '\t')) idx++;
        int end = idx;
        if (end < json.length() && json.charAt(end) == '-') end++;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        if (end == idx) return 0;
        return Integer.parseInt(json.substring(idx, end));
    }

    private static String extractJsonString(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) return null;
        idx += search.length();
        while (idx < json.length() && (json.charAt(idx) == ' ' || json.charAt(idx) == '\t')) idx++;
        if (idx >= json.length() || json.charAt(idx) != '"') return null;
        idx++;
        int end = json.indexOf("\"", idx);
        if (end == -1) return null;
        return json.substring(idx, end);
    }

    private static int[] extractJsonIntArray(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) return new int[0];
        idx += search.length();
        while (idx < json.length() && (json.charAt(idx) == ' ' || json.charAt(idx) == '\t')) idx++;
        if (idx >= json.length() || json.charAt(idx) != '[') return new int[0];
        idx++;
        int end = json.indexOf("]", idx);
        if (end == -1) return new int[0];
        String content = json.substring(idx, end).trim();
        if (content.isEmpty()) return new int[0];
        String[] parts = content.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    private static List<String> extractJsonObjects(String json, String arrayKey) {
        List<String> objects = new ArrayList<>();
        String search = "\"" + arrayKey + "\":";
        int arrayStart = json.indexOf(search);
        if (arrayStart == -1) return objects;
        arrayStart += search.length();
        while (arrayStart < json.length() && (json.charAt(arrayStart) == ' ' || json.charAt(arrayStart) == '\t')) arrayStart++;
        if (arrayStart >= json.length() || json.charAt(arrayStart) != '[') return objects;
        arrayStart++;

        int depth = 0;
        int objStart = -1;
        for (int i = arrayStart; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) objStart = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && objStart != -1) {
                    objects.add(json.substring(objStart, i + 1));
                    objStart = -1;
                }
            } else if (c == ']' && depth == 0) {
                break;
            }
        }
        return objects;
    }

    private static String stripPath(String path) {
        int slash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return path.substring(slash + 1);
    }
}
