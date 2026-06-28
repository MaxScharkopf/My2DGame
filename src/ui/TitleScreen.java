package ui;

import main.GamePanel;

import java.awt.*;

public class TitleScreen {

    GamePanel gp;
    public int titleScreenState = 0;

    public TitleScreen(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2, UI ui) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (titleScreenState == 0) {
            drawMain(g2, ui);
        } else if (titleScreenState == 1) {
            drawClassSelect(g2, ui);
        }
    }

    private void drawMain(Graphics2D g2, UI ui) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Beautiful names";
        int x = ui.getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x + 3, y + 3);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize;
        y += (int) (gp.tileSize * 2.3);
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = ui.getXforCenteredText(text);
        y += (int) (gp.tileSize * 3.5);
        g2.drawString(text, x, y);
        if (ui.commandNum == 0 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);

        text = "LOAD GAME";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (ui.commandNum == 1 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);

        text = "QUIT";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (ui.commandNum == 2 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);
    }

    private void drawClassSelect(Graphics2D g2, UI ui) {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(42F));

        String text = "Select your class!";
        int x = ui.getXforCenteredText(text);
        int y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        text = "Fighter";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if (ui.commandNum == 0 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);

        text = "Theif";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (ui.commandNum == 1 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);

        text = "Sorcerer";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (ui.commandNum == 2 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);

        text = "Back";
        x = ui.getXforCenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if (ui.commandNum == 3 && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);
    }
}
