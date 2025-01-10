package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum; // 1st
    boolean drawPath = true;

    public TileManager(GamePanel gp)    {
        this.gp = gp;

        tile = new Tile[50]; // number of different tile types we can use
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/worldV3.txt", 0);
        loadMap("/maps/interior01.txt", 1);
    }

    public void getTileImage()  {

        // PLACEHOLDER
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
        // PLACEHOLDER

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
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize,gp.tileSize);
            tile[index].collision = collision;

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath, int map) {
        try{
            InputStream is = getClass().getResourceAsStream(filePath); // loads map data into memory
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // buffers the character stream

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine(); // reads a line of text using the buffer

                while(col < gp.maxWorldCol) {
                    String[] numbers = line.split(" "); // gets string number 1 by 1

                    int num = Integer.parseInt(numbers[col]); // changes string to integer

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch(Exception ignored){

        }

    }

    // ACTIVE IN GAME-LOOP
    public void draw(Graphics2D g2) {

       int worldCol = 0;
       int worldRow = 0;

       while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

           int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

           // position on the map
           int worldX = worldCol * gp.tileSize;
           int worldY = worldRow * gp.tileSize;

           // screen moves around player
           int screenX = worldX - gp.player.worldX + gp.player.screenX;
           int screenY = worldY - gp.player.worldY + gp.player.screenY;

           // creates boundary, so we don't load all tiles at once
           if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // add gp.tileSize to extend screen render by 1
              worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && // sub gp.tileSize to extend screen render by 1
              worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
              worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

               g2.drawImage(tile[tileNum].image, screenX, screenY,null);
           }

           worldCol++; // increments col of array mapTileNum

           if(worldCol == gp.maxWorldCol) {
               worldCol = 0; // reset column
               worldRow++; // increments row of array mapTileNum

           }
       }

       if(drawPath) {
           g2.setColor(new Color(255,0,0,70));

           for(int i = 0; i < gp.pFinder.pathList.size(); i++) {

               int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
               int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
               int screenX = worldX - gp.player.worldX + gp.player.screenX;
               int screenY = worldY - gp.player.worldY + gp.player.screenY;

               g2.fillRect(screenX,screenY,gp.tileSize,gp.tileSize);
           }
       }
    }
}
