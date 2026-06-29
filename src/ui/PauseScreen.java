package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseScreen implements Screen {

    GamePanel gp;

    public PauseScreen(GamePanel gp) { this.gp = gp; }

    @Override
    public void onKeyPressed(int code) {
        if (code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameState.PLAY;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
        String text = "PAUSED";
        int x = ScreenUtils.centeredX(g2, text, gp.screenWidth);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }
}
