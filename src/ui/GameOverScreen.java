package ui;

import main.GamePanel;

import java.awt.*;

public class GameOverScreen {

    GamePanel gp;

    public GameOverScreen(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2, UI ui) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));
        String text = "Game Over";
        g2.setColor(Color.black);
        int x = ui.getXforCenteredText(text);
        int y = gp.tileSize * 4;
        g2.drawString(text, x, y);
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        g2.setFont(g2.getFont().deriveFont(50F));

        text = "Restart";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (ui.commandNum == 0 && gp.uTool.flickerImage(350)) g2.drawString(">", x - 40, y);

        text = "Quit";
        x = ui.getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if (ui.commandNum == 1 && gp.uTool.flickerImage(350)) g2.drawString(">", x - 40, y);
    }
}
