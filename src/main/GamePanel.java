package main;

import ai.PathFinder;
import audio.Sound;
import config.Config;
import entity.Entity;
import entity.Player;
import input.KeyHandler;
import physics.CollisionChecker;
import ui.UI;
import util.UtilityTool;
import world.AssetSetter;
import world.EntityManager;
import world.EventHandler;
import world.ObjectManager;
import world.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile, very small
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10; // number of maps
    public int currentMap = 0;

    // FOR FULL SCREEEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;

    // FPS
    double FPS = 60;

    // SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public EventHandler eHandler = new EventHandler(this);
    public Sound se = new Sound(); // sound effect
    public Sound music = new Sound(); // musicA
    public UtilityTool uTool = new UtilityTool();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public EntityManager em = new EntityManager(this);
    public ObjectManager om = new ObjectManager(this);
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public GameState gameState;

    // Constructor
    public GamePanel (){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // improves rendering performance
        this.addKeyListener(keyH); // key input
        this.setFocusable(true); //able to be 'focused' to recieve input
    }

    public void setupGame() {

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setCritter();
        aSetter.setInteractiveTile();


        gameState = GameState.TITLE;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn) {
            setFullScreen();
        }
    }
    public void retry() {

        player.setDefaultPositions();
        player.restoreLifeAndMana();

        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setCritter();
    }
    public void restart() {

        player.setDefaultValues();
        player.setItems();

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setCritter();
        aSetter.setInteractiveTile();
    }
    public void setFullScreen() {

        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

    }
    public void startGameThread() {

        gameThread = new Thread(this); // 'this' refers to the GamePanel class
        gameThread.start(); // calls our run method automatically
    }
    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime(); // start game loop time
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime(); // time at the start of game loop

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            /*  Checking if it's greater than 1 is important as currentTime and lastTime
            *   should be extremely close to one another                                 */

            if(delta >= 1){
                update();// update information such as character position (FPS)
               // repaint(); // draw the screen with the updated information and calls @paintComponent
                drawToTempScreen(); // draw everything to buffered image
                drawToScreen(); // draw the buffered image screen
                delta --;
                drawCount ++;
            }


            if (timer >= 1000000000){
                // System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update(){
        if(gameState == GameState.PLAY) {
            player.update();
            em.update();
        }
        if(gameState == GameState.PAUSE) {

        }
    }
    /* Layering for this method is from bottom to top in terms of what will be displayed*/
    // ACTIVE IN GAME-LOOP
    public void drawToTempScreen() {

        // DEBUG
        long drawStart = 0;
        if(keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN 1
        if(gameState == GameState.TITLE) {
            ui.draw(g2);
        }
        // OTHERS
        else {
            // TILE
            tileM.draw(g2); // drawing tiles before player, or we would not see our character

            // INTERACTIVE TILE
            for (int i = 0; i < em.iTile[1].length; i++) {
                if (em.iTile[currentMap][i] != null) {
                    em.iTile[currentMap][i].draw(g2);
                }
            }

            // ADD ENTITIES TO THE LIST
            entityList.add(player);

            for (int i = 0; i < em.npc[1].length; i++) {
                if (em.npc[currentMap][i] != null) {
                    entityList.add(em.npc[currentMap][i]);
                }
            }
            for (int i = 0; i < om.obj[1].length; i++) {
                if (om.obj[currentMap][i] != null) {
                    entityList.add(om.obj[currentMap][i]);
                }
            }
            for (int i = 0; i < em.monster[1].length; i++) {
                if (em.monster[currentMap][i] != null) {
                    entityList.add(em.monster[currentMap][i]);
                }
            }
            for (int i = 0; i < em.critter[1].length; i++) {
                if (em.critter[currentMap][i] != null) {
                    entityList.add(em.critter[currentMap][i]);
                }
            }
            for (Entity value : em.projectileList) {
                if (value != null) {
                    entityList.add(value);
                }
            }
            for (Entity value : em.particleList) {
                if (value != null) {
                    entityList.add(value);
                }
            }
            // SORT
            entityList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    return Integer.compare(e1.worldY, e2.worldY); // compare entity by world y to order the array
                }
            });

            // DRAW ENTITIES
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            // EMPTY ENTITY LIST
            entityList.clear();

            // UI
            ui.draw(g2);
        }

        // DEBUG
        if(keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);

            int x = 10;
            int y = 400;
            int lineHeight = 20;
            g2.drawString("WorldX " + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY " + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col " + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("Row " + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;
            g2.drawString("Draw Time: " + passed, x, y);
        }
    }
    public void drawToScreen() {

        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    // methods for playing sound
    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {

        music.stop();
    }
    public void playSE(int i) {

        se.setFile(i);
        se.play();
    }
}
