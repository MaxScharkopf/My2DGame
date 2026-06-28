package main;

import ai.PathFinder;
import audio.Sound;
import combat.CombatSystem;
import config.Config;
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

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth  = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;

    // FOR FULL SCREEN
    int screenWidth2  = screenWidth;
    int screenHeight2 = screenHeight;
    public boolean fullScreenOn = false;

    // FPS
    double FPS = 60;

    // SYSTEM
    public TileManager      tileM    = new TileManager(this);
    public KeyHandler       keyH     = new KeyHandler(this);
    public EventHandler     eHandler = new EventHandler(this);
    public Sound            se       = new Sound();
    public Sound            music    = new Sound();
    public UtilityTool      uTool    = new UtilityTool();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter      aSetter  = new AssetSetter(this);
    public UI               ui       = new UI(this);
    public Config           config   = new Config(this);
    public PathFinder       pFinder  = new PathFinder(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player        player = new Player(this, keyH);
    public EntityManager em     = new EntityManager(this);
    public ObjectManager om     = new ObjectManager(this);
    public CombatSystem  combat = new CombatSystem(this);
    public Renderer      renderer = new Renderer(this);

    // GAME STATE
    public GameState gameState;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setCritter();
        aSetter.setInteractiveTile();

        gameState = GameState.TITLE;
        renderer.init();

        if (fullScreenOn) {
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
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);
        screenWidth2  = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                renderer.draw();
                renderer.drawToScreen();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == GameState.PLAY) {
            player.update();
            em.update();
        }
    }

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
