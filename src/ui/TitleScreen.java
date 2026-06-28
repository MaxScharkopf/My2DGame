package ui;

import config.SaveData;
import main.GamePanel;

import java.awt.*;

public class TitleScreen {

    GamePanel gp;
    public int titleScreenState = 0;
    // 0 = main menu
    // 1 = new game slot select
    // 2 = class select
    // 3 = load game slot select

    SaveData.SlotSummary[] slotSummaries = new SaveData.SlotSummary[4];

    public TitleScreen(GamePanel gp) {
        this.gp = gp;
    }

    public void refreshSlotSummaries() {
        for (int i = 0; i < 4; i++) {
            slotSummaries[i] = gp.saveData.getSlotSummary(i);
        }
    }

    public void draw(Graphics2D g2, UI ui) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        switch (titleScreenState) {
            case 0 -> drawMain(g2, ui);
            case 1 -> drawSlotSelect(g2, ui, false);
            case 2 -> drawClassSelect(g2, ui);
            case 3 -> drawSlotSelect(g2, ui, true);
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

    private void drawSlotSelect(Graphics2D g2, UI ui, boolean loadMode) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        g2.setColor(Color.white);
        String title = loadMode ? "Load Game" : "New Game";
        g2.drawString(title, ui.getXforCenteredText(title), gp.tileSize * 2);

        g2.setFont(g2.getFont().deriveFont(32F));
        int slotH   = gp.tileSize + 10;
        int startY  = (int) (gp.tileSize * 3.2);
        int slotX   = gp.tileSize * 3;
        int slotW   = gp.tileSize * 14;

        for (int i = 0; i < 4; i++) {
            int slotY = startY + i * (slotH + 12);
            ui.drawSubWindow(slotX, slotY, slotW, slotH);

            SaveData.SlotSummary s = slotSummaries[i];
            String label;
            if (s == null) {
                label = "Slot " + (i + 1) + ":  --- EMPTY ---";
                g2.setColor(loadMode ? Color.gray : Color.white);
            } else {
                label = "Slot " + (i + 1) + ":  Lv." + s.level + "   " + s.coin + " coins   [" + s.mapName + "]";
                g2.setColor(Color.white);
            }

            g2.drawString(label, slotX + 20, slotY + slotH - 8);

            if (ui.commandNum == i && gp.uTool.flickerImage(400)) {
                g2.setColor(Color.white);
                g2.drawString(">", slotX - 28, slotY + slotH - 8);
            }
        }

        // Back option
        g2.setColor(Color.white);
        String back = "Back";
        int backY = startY + 4 * (slotH + 12) + gp.tileSize / 2;
        int backX = ui.getXforCenteredText(back);
        g2.drawString(back, backX, backY);
        if (ui.commandNum == 4 && gp.uTool.flickerImage(400)) g2.drawString(">", backX - gp.tileSize, backY);
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
