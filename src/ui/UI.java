package ui;

import entity.Entity;
import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;
import main.GameState;
import objects.OBJ_Coin_Bronze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    BufferedImage coin;

    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    public int subState = 0;
    int counter = 0;
    public LivingEntity npc;

    public HUD hud;
    public TitleScreen titleScreen;
    public GameOverScreen gameOverScreen;

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        Entity bronze_coin = new OBJ_Coin_Bronze(gp);
        coin = bronze_coin.down1;

        hud = new HUD(gp);
        titleScreen = new TitleScreen(gp);
        gameOverScreen = new GameOverScreen(gp);
    }

    public void addMessage(String text) {
        hud.addMessage(text);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if (gp.gameState == GameState.TITLE) {
            titleScreen.draw(g2, this);
        }
        if (gp.gameState == GameState.PLAY) {
            hud.drawPlayerLife(g2);
            hud.drawMessage(g2);
        }
        if (gp.gameState == GameState.PAUSE) {
            hud.drawPlayerLife(g2);
            drawPauseScreen();
        }
        if (gp.gameState == GameState.DIALOGUE) {
            hud.drawPlayerLife(g2);
            drawDialogueScreen();
        }
        if (gp.gameState == GameState.CHARACTER) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        if (gp.gameState == GameState.OPTIONS) {
            drawOptionsScreen();
        }
        if (gp.gameState == GameState.GAME_OVER) {
            gameOverScreen.draw(g2, this);
        }
        if (gp.gameState == GameState.TRANSITION) {
            drawTransition();
        }
        if (gp.gameState == GameState.TRADE) {
            drawTradeScreen();
        }
    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width  = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen() {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize - 20;
        final int frameWidth  = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 11;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 40;

        g2.drawString("Level",      textX, textY); textY += lineHeight;
        g2.drawString("Life",       textX, textY); textY += lineHeight;
        g2.drawString("Mana",       textX, textY); textY += lineHeight;
        g2.drawString("Strength",   textX, textY); textY += lineHeight;
        g2.drawString("Dexterity",  textX, textY); textY += lineHeight;
        g2.drawString("Attack",     textX, textY); textY += lineHeight;
        g2.drawString("Defense",    textX, textY); textY += lineHeight;
        g2.drawString("Exp",        textX, textY); textY += lineHeight;
        g2.drawString("Next Level", textX, textY); textY += lineHeight;
        g2.drawString("Coin",       textX, textY); textY += lineHeight + 15;
        g2.drawString("Weapon",     textX, textY); textY += lineHeight + 10;
        g2.drawString("Shield",     textX, textY);

        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = gp.player.life + "/" + gp.player.maxLife;
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = gp.player.mana + "/" + gp.player.maxMana;
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY); textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize + 10;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 34, null);
    }

    public void drawInventory(LivingEntity entity, boolean cursor) {
        int frameX, frameY, frameWidth, frameHeight, slotCol, slotRow;

        if (entity == gp.player) {
            frameX = gp.tileSize * 13;
            frameY = gp.tileSize - 20;
            frameWidth  = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth  = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;

        for (int i = 0; i < entity.inventory.size(); i++) {
            if (entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        if (cursor) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);

            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, gp.tileSize, gp.tileSize, 10, 10);

            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int textX   = dFrameX + 20;
            int textY   = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(28F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if (itemIndex < entity.inventory.size()) {
                drawSubWindow(dFrameX, dFrameY, frameWidth, gp.tileSize * 3);

                int nameCount = 0;
                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    if (nameCount == 0) {
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
                        nameCount++;
                    } else {
                        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
                    }
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
    }

    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth  = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0: options_top(frameX, frameY); break;
            case 1: options_fullScreenNotification(frameX, frameY); break;
            case 2: options_control(frameX, frameY); break;
            case 3: options_endGameConfirmation(frameX, frameY); break;
        }

        gp.keyH.enterPressed = false;
    }

    public void options_top(int frameX, int frameY) {
        int textX;
        int textY;

        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;

        g2.drawString("Full Screen", textX, textY);
        if (commandNum == 0) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { gp.fullScreenOn = !gp.fullScreenOn; subState = 1; }
        }

        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1 && gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);

        textY += gp.tileSize;
        g2.drawString("Sound Effects", textX, textY);
        if (commandNum == 2 && gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);

        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if (commandNum == 3) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { subState = 2; commandNum = 0; }
        }

        textY += gp.tileSize;
        g2.drawString("Save", textX, textY);
        if (commandNum == 4) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { gp.saveData.save(); }
        }

        textY += gp.tileSize;
        g2.drawString("Quit", textX, textY);
        if (commandNum == 5) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { subState = 3; commandNum = 0; }
        }

        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 6) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { gp.gameState = GameState.PLAY; commandNum = 0; }
        }

        textX = frameX + (int) (gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if (gp.fullScreenOn) g2.fillRect(textX, textY, 24, 24);

        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        g2.fillRect(textX, textY, 24 * gp.music.volumeScale, 24);

        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        g2.fillRect(textX, textY, 24 * gp.se.volumeScale, 24);

        gp.config.saveConfig();
    }

    public void options_fullScreenNotification(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "The change will take effect \n after you restart the game. ";
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) subState = 0;
        }
    }

    public void options_control(int frameX, int frameY) {
        String text = "Controls";
        int textX = getXforCenteredText(text);
        int textY = frameY + gp.tileSize;
        g2.drawString(text, frameX, frameY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;

        g2.drawString("Move",             textX, textY); textY += gp.tileSize;
        g2.drawString("Confirm/Attack",   textX, textY); textY += gp.tileSize;
        g2.drawString("Shoot/Cast",       textX, textY); textY += gp.tileSize;
        g2.drawString("Character Screen", textX, textY); textY += gp.tileSize;
        g2.drawString("Pause",            textX, textY); textY += gp.tileSize;
        g2.drawString("Options",          textX, textY);

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;

        g2.drawString("WASD",  textX, textY); textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY); textY += gp.tileSize;
        g2.drawString("F",     textX, textY); textY += gp.tileSize;
        g2.drawString("C",     textX, textY); textY += gp.tileSize;
        g2.drawString("P",     textX, textY); textY += gp.tileSize;
        g2.drawString("ESC",   textX, textY);

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { subState = 0; commandNum = 3; }
        }
    }

    public void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Quit the game and \n return to the title screen?";
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { subState = 0; gp.gameState = GameState.TITLE; gp.stopMusic(); }
        }

        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) { subState = 0; commandNum = 4; }
        }
    }

    public void drawTransition() {
        counter++;
        g2.setColor(new Color(0, 0, 0, 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (counter >= 50) {
            counter = 0;
            gp.gameState = GameState.PLAY;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }

    public void drawTradeScreen() {
        switch (subState) {
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        gp.keyH.enterPressed = false;
    }

    public void trade_select() {
        drawDialogueScreen();

        int x = gp.tileSize * 15;
        int y = gp.tileSize * 6;
        drawSubWindow(x, y, gp.tileSize * 3, (int) (gp.tileSize * 3.5));

        x += gp.tileSize;
        y += gp.tileSize;

        g2.drawString("Buy", x, y);
        if (commandNum == 0) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", x - 25, y);
            if (gp.keyH.enterPressed) subState = 1;
        }
        y += gp.tileSize;

        g2.drawString("Sell", x, y);
        if (commandNum == 1) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", x - 25, y);
            if (gp.keyH.enterPressed) subState = 2;
        }
        y += gp.tileSize;

        g2.drawString("Leave", x, y);
        if (commandNum == 2) {
            if (gp.uTool.flickerImage(350)) g2.drawString(">", x - 25, y);
            if (gp.keyH.enterPressed) { commandNum = 0; gp.gameState = GameState.DIALOGUE; currentDialogue = "Come again, hehe!"; }
        }
    }

    public void trade_buy() {
        drawInventory(gp.player, false);
        drawInventory(npc, true);

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        drawSubWindow(x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("[ESC] Back", x + 25, y + 60);

        x = gp.tileSize * 12;
        drawSubWindow(x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("Your Coin: " + gp.player.coin, x + 25, y + 60);

        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if (itemIndex < npc.inventory.size()) {
            x = (int) (gp.tileSize * 5.5);
            y = (int) (gp.tileSize * 5.5);
            drawSubWindow(x, y, (int) (gp.tileSize * 2.5), gp.tileSize);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null);

            int price = ((ItemEntity) npc.inventory.get(itemIndex)).price;
            String text = "" + price;
            x = getXforAlignToRightText(text, gp.tileSize * 8 - 20);
            g2.drawString(text, x, y + 34);

            if (gp.keyH.enterPressed) {
                if (((ItemEntity) npc.inventory.get(itemIndex)).price > gp.player.coin) {
                    subState = 0;
                    gp.gameState = GameState.DIALOGUE;
                    currentDialogue = " You trying to scam me or something?";
                    drawDialogueScreen();
                } else if (gp.player.inventory.size() == gp.player.maxInventorySize) {
                    subState = 0;
                    gp.gameState = GameState.DIALOGUE;
                    currentDialogue = " Your inventory is full";
                } else {
                    gp.player.coin -= ((ItemEntity) npc.inventory.get(itemIndex)).price;
                    gp.player.inventory.add(npc.inventory.get(itemIndex));
                }
            }
        }
    }

    public void trade_sell() {
        drawInventory(gp.player, true);

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        drawSubWindow(x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("[ESC] Back", x + 25, y + 60);

        x = gp.tileSize * 12;
        drawSubWindow(x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("Your Coin: " + gp.player.coin, x + 25, y + 60);

        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if (itemIndex < gp.player.inventory.size()) {
            x = (int) (gp.tileSize * 15.5);
            y = gp.tileSize * 5;
            drawSubWindow(x, y, (int) (gp.tileSize * 2.5), gp.tileSize);
            g2.drawImage(coin, x + 10, y + 10, 32, 32, null);

            int price = ((ItemEntity) gp.player.inventory.get(itemIndex)).price / 2;
            String text = "" + price;
            x = getXforAlignToRightText(text, gp.tileSize * 18 - 20);
            g2.drawString(text, x, y + 34);

            if (gp.keyH.enterPressed) {
                if (gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
                        gp.player.inventory.get(itemIndex) == gp.player.currentShield) {
                    commandNum = 0;
                    subState = 0;
                    gp.gameState = GameState.DIALOGUE;
                    currentDialogue = "You cannot sell equipped items!";
                } else {
                    gp.player.inventory.remove(itemIndex);
                    gp.player.coin += price;
                }
            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        return slotCol + (slotRow * 5);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }
}
