package ui;

import config.SaveData;
import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TitleScreen implements Screen {

    GamePanel gp;
    public int titleScreenState = 0;
    // 0 = main menu  1 = new game slot select  2 = class select  3 = load game slot select

    public int commandNum = 0;
    private SaveData.SlotSummary[] slotSummaries = new SaveData.SlotSummary[4];

    public TitleScreen(GamePanel gp) { this.gp = gp; }

    public void refreshSlotSummaries() {
        for (int i = 0; i < 4; i++) slotSummaries[i] = gp.saveData.getSlotSummary(i);
    }

    public void resetToMainMenu() {
        titleScreenState = 0;
        commandNum = 0;
    }

    @Override
    public void onKeyPressed(int code) {
        switch (titleScreenState) {
            case 0 -> mainMenuKey(code);
            case 1 -> newGameSlotKey(code);
            case 2 -> classSelectKey(code);
            case 3 -> loadGameSlotKey(code);
        }
    }

    private void mainMenuKey(int code) {
        if (code == KeyEvent.VK_W) { commandNum--; gp.playSE(11); if (commandNum < 0) commandNum = 2; }
        if (code == KeyEvent.VK_S) { commandNum++; gp.playSE(11); if (commandNum > 2) commandNum = 0; }
        if (code == KeyEvent.VK_ENTER) {
            gp.playSE(4);
            if (commandNum == 0) { refreshSlotSummaries(); titleScreenState = 1; commandNum = 0; }
            if (commandNum == 1) { refreshSlotSummaries(); titleScreenState = 3; commandNum = 0; }
            if (commandNum == 2) System.exit(0);
        }
    }

    private void newGameSlotKey(int code) {
        if (code == KeyEvent.VK_W) { commandNum--; gp.playSE(11); if (commandNum < 0) commandNum = 4; }
        if (code == KeyEvent.VK_S) { commandNum++; gp.playSE(11); if (commandNum > 4) commandNum = 0; }
        if (code == KeyEvent.VK_ENTER) {
            gp.playSE(4);
            if (commandNum <= 3) {
                gp.saveData.clearSlot(commandNum);
                gp.saveData.currentSlot = commandNum;
                gp.restart();
                titleScreenState = 2;
                commandNum = 0;
            }
            if (commandNum == 4) { titleScreenState = 0; commandNum = 0; }
        }
    }

    private void classSelectKey(int code) {
        if (code == KeyEvent.VK_W) { commandNum--; gp.playSE(11); if (commandNum < 0) commandNum = 3; }
        if (code == KeyEvent.VK_S) { commandNum++; gp.playSE(11); if (commandNum > 3) commandNum = 0; }
        if (code == KeyEvent.VK_ENTER) {
            gp.playSE(4);
            if (commandNum == 0) { gp.player.playerClass = "Fighter";  gp.saveData.save(); gp.gameState = GameState.PLAY; gp.playMusic(0); }
            if (commandNum == 1) { gp.player.playerClass = "Thief";    gp.saveData.save(); gp.gameState = GameState.PLAY; gp.playMusic(0); }
            if (commandNum == 2) { gp.player.playerClass = "Sorcerer"; gp.saveData.save(); gp.gameState = GameState.PLAY; gp.playMusic(0); }
            if (commandNum == 3) { titleScreenState = 1; commandNum = 0; }
        }
    }

    private void loadGameSlotKey(int code) {
        if (code == KeyEvent.VK_W) { commandNum--; gp.playSE(11); if (commandNum < 0) commandNum = 4; }
        if (code == KeyEvent.VK_S) { commandNum++; gp.playSE(11); if (commandNum > 4) commandNum = 0; }
        if (code == KeyEvent.VK_ENTER) {
            gp.playSE(4);
            if (commandNum <= 3 && gp.saveData.hasSaveFile(commandNum)) {
                gp.saveData.load(commandNum);
                titleScreenState = 0;
                commandNum = 0;
            }
            if (commandNum == 4) { titleScreenState = 0; commandNum = 0; }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        switch (titleScreenState) {
            case 0 -> drawMain(g2);
            case 1 -> drawSlotSelect(g2, false);
            case 2 -> drawClassSelect(g2);
            case 3 -> drawSlotSelect(g2, true);
        }
    }

    private void drawMain(Graphics2D g2) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Beautiful names";
        int x = ScreenUtils.centeredX(g2, text, gp.screenWidth);
        int y = gp.tileSize * 3;
        g2.setColor(Color.gray);  g2.drawString(text, x + 3, y + 3);
        g2.setColor(Color.white); g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize;
        y += (int) (gp.tileSize * 2.3);
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        y += (int) (gp.tileSize * 3.5);
        drawMainOption(g2, "NEW GAME",  y, 0); y += gp.tileSize;
        drawMainOption(g2, "LOAD GAME", y, 1); y += gp.tileSize;
        drawMainOption(g2, "QUIT",      y, 2);
    }

    private void drawMainOption(Graphics2D g2, String label, int y, int cmd) {
        int x = ScreenUtils.centeredX(g2, label, gp.screenWidth);
        g2.drawString(label, x, y);
        if (commandNum == cmd && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);
    }

    private void drawSlotSelect(Graphics2D g2, boolean loadMode) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        g2.setColor(Color.white);
        String title = loadMode ? "Load Game" : "New Game";
        g2.drawString(title, ScreenUtils.centeredX(g2, title, gp.screenWidth), gp.tileSize * 2);

        g2.setFont(g2.getFont().deriveFont(32F));
        int slotH  = gp.tileSize + 10;
        int startY = (int) (gp.tileSize * 3.2);
        int slotX  = gp.tileSize * 3;
        int slotW  = gp.tileSize * 14;

        for (int i = 0; i < 4; i++) {
            int slotY = startY + i * (slotH + 12);
            ScreenUtils.drawSubWindow(g2, slotX, slotY, slotW, slotH);

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
            if (commandNum == i && gp.uTool.flickerImage(400)) {
                g2.setColor(Color.white);
                g2.drawString(">", slotX - 28, slotY + slotH - 8);
            }
        }

        g2.setColor(Color.white);
        String back = "Back";
        int backY = startY + 4 * (slotH + 12) + gp.tileSize / 2;
        int backX = ScreenUtils.centeredX(g2, back, gp.screenWidth);
        g2.drawString(back, backX, backY);
        if (commandNum == 4 && gp.uTool.flickerImage(400)) g2.drawString(">", backX - gp.tileSize, backY);
    }

    private void drawClassSelect(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(42F));

        int y = gp.tileSize * 3;
        drawClassOption(g2, "Select your class!", y, -1);
        y += gp.tileSize * 2; drawClassOption(g2, "Fighter",  y, 0);
        y += gp.tileSize;     drawClassOption(g2, "Theif",    y, 1);
        y += gp.tileSize;     drawClassOption(g2, "Sorcerer", y, 2);
        y += gp.tileSize * 2; drawClassOption(g2, "Back",     y, 3);
    }

    private void drawClassOption(Graphics2D g2, String label, int y, int cmd) {
        int x = ScreenUtils.centeredX(g2, label, gp.screenWidth);
        g2.drawString(label, x, y);
        if (commandNum == cmd && gp.uTool.flickerImage(400)) g2.drawString(">", x - gp.tileSize, y);
    }
}
