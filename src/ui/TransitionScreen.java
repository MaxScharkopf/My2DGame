package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;

public class TransitionScreen implements Screen {

    GamePanel gp;
    private int counter = 0;

    public TransitionScreen(GamePanel gp) { this.gp = gp; }

    @Override
    public void update() {
        counter++;
        if (counter >= 50) {
            counter = 0;
            gp.gameState     = GameState.PLAY;
            gp.currentMap    = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }

    @Override
    public void onKeyPressed(int code) {}

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
}
