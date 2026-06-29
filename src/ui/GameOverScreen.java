package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverScreen implements Screen {

    GamePanel gp;
    public int commandNum = -1;

    public GameOverScreen(GamePanel gp) { this.gp = gp; }

    @Override
    public void onKeyPressed(int code) {
        if (code == KeyEvent.VK_W) { commandNum--; if (commandNum < 0) commandNum = 1; gp.playSE(11); }
        if (code == KeyEvent.VK_S) { commandNum++; if (commandNum > 1) commandNum = 0; gp.playSE(11); }
        if (code == KeyEvent.VK_ENTER) {
            if (commandNum == 0) { gp.gameState = GameState.PLAY;  gp.retry();   }
            if (commandNum == 1) { gp.gameState = GameState.TITLE; gp.restart(); }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));
        String text = "Game Over";
        int x = ScreenUtils.centeredX(g2, text, gp.screenWidth);
        int y = gp.tileSize * 4;
        g2.setColor(Color.black);  g2.drawString(text, x, y);
        g2.setColor(Color.white);  g2.drawString(text, x - 4, y - 4);

        g2.setFont(g2.getFont().deriveFont(50F));

        text = "Restart";
        x = ScreenUtils.centeredX(g2, text, gp.screenWidth);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0 && gp.uTool.flickerImage(350)) g2.drawString(">", x - 40, y);

        text = "Quit";
        x = ScreenUtils.centeredX(g2, text, gp.screenWidth);
        y += 55;
        g2.drawString(text, x, y);
        if (commandNum == 1 && gp.uTool.flickerImage(350)) g2.drawString(">", x - 40, y);
    }
}
