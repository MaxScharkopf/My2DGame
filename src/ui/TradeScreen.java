package ui;

import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;
import main.GameState;
import objects.OBJ_Coin_Bronze;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TradeScreen implements Screen {

    GamePanel gp;
    public LivingEntity npc;
    public int subState    = 0;
    public int commandNum  = 0;
    public int npcSlotCol  = 0;
    public int npcSlotRow  = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    private final BufferedImage coin;

    public TradeScreen(GamePanel gp) {
        this.gp = gp;
        coin = new OBJ_Coin_Bronze(gp).down1;
    }

    @Override
    public void onKeyPressed(int code) {
        if (subState == 0) {
            if (code == KeyEvent.VK_W) { commandNum = (commandNum - 1 + 3) % 3; gp.playSE(11); }
            if (code == KeyEvent.VK_S) { commandNum = (commandNum + 1) % 3;     gp.playSE(11); }
            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(4);
                switch (commandNum) {
                    case 0 -> subState = 1;
                    case 1 -> subState = 2;
                    case 2 -> {
                        commandNum = 0;
                        gp.ui.dialogueScreen.currentDialogue = "Come again, hehe!";
                        gp.gameState = GameState.DIALOGUE;
                    }
                }
            }
        } else if (subState == 1) {
            moveNpcCursor(code);
            if (code == KeyEvent.VK_ESCAPE) subState = 0;
            if (code == KeyEvent.VK_ENTER)  handleBuy();
        } else if (subState == 2) {
            movePlayerCursor(code);
            if (code == KeyEvent.VK_ESCAPE) subState = 0;
            if (code == KeyEvent.VK_ENTER)  handleSell();
        }
    }

    private void movePlayerCursor(int code) {
        if (code == KeyEvent.VK_W && playerSlotRow > 0) { playerSlotRow--; gp.playSE(11); }
        if (code == KeyEvent.VK_S && playerSlotRow < 3) { playerSlotRow++; gp.playSE(11); }
        if (code == KeyEvent.VK_A && playerSlotCol > 0) { playerSlotCol--; gp.playSE(11); }
        if (code == KeyEvent.VK_D && playerSlotCol < 4) { playerSlotCol++; gp.playSE(11); }
    }

    private void moveNpcCursor(int code) {
        if (code == KeyEvent.VK_W && npcSlotRow > 0) { npcSlotRow--; gp.playSE(11); }
        if (code == KeyEvent.VK_S && npcSlotRow < 3) { npcSlotRow++; gp.playSE(11); }
        if (code == KeyEvent.VK_A && npcSlotCol > 0) { npcSlotCol--; gp.playSE(11); }
        if (code == KeyEvent.VK_D && npcSlotCol < 4) { npcSlotCol++; gp.playSE(11); }
    }

    private void handleBuy() {
        if (npc == null) return;
        int idx = ScreenUtils.itemIndex(npcSlotCol, npcSlotRow);
        if (idx >= npc.inventory.size()) return;
        int price = ((ItemEntity) npc.inventory.get(idx)).price;
        if (price > gp.player.coin) {
            gp.ui.dialogueScreen.currentDialogue = " You trying to scam me or something?";
            gp.gameState = GameState.DIALOGUE;
            subState = 0;
        } else if (gp.player.inventory.size() == gp.player.maxInventorySize) {
            gp.ui.dialogueScreen.currentDialogue = " Your inventory is full";
            gp.gameState = GameState.DIALOGUE;
            subState = 0;
        } else {
            gp.player.coin -= price;
            gp.player.inventory.add(npc.inventory.get(idx));
        }
    }

    private void handleSell() {
        int idx = ScreenUtils.itemIndex(playerSlotCol, playerSlotRow);
        if (idx >= gp.player.inventory.size()) return;
        if (gp.player.inventory.get(idx) == gp.player.currentWeapon ||
                gp.player.inventory.get(idx) == gp.player.currentShield) {
            gp.ui.dialogueScreen.currentDialogue = "You cannot sell equipped items!";
            gp.gameState = GameState.DIALOGUE;
            commandNum = 0;
            subState = 0;
        } else {
            gp.player.coin += ((ItemEntity) gp.player.inventory.get(idx)).price / 2;
            gp.player.inventory.remove(idx);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (subState) {
            case 0 -> trade_select(g2);
            case 1 -> trade_buy(g2);
            case 2 -> trade_sell(g2);
        }
    }

    private void trade_select(Graphics2D g2) {
        gp.ui.dialogueScreen.draw(g2);

        int x = gp.tileSize * 15;
        int y = gp.tileSize * 6;
        ScreenUtils.drawSubWindow(g2, x, y, gp.tileSize * 3, (int) (gp.tileSize * 3.5));
        x += gp.tileSize;
        y += gp.tileSize;

        draw_row(g2, "Buy",   x, y, 0); y += gp.tileSize;
        draw_row(g2, "Sell",  x, y, 1); y += gp.tileSize;
        draw_row(g2, "Leave", x, y, 2);
    }

    private void draw_row(Graphics2D g2, String label, int x, int y, int cmd) {
        g2.drawString(label, x, y);
        if (commandNum == cmd && gp.uTool.flickerImage(350)) g2.drawString(">", x - 25, y);
    }

    private void trade_buy(Graphics2D g2) {
        ScreenUtils.drawInventory(g2, gp, gp.player, false, 0, 0);
        ScreenUtils.drawInventory(g2, gp, npc, true, npcSlotCol, npcSlotRow);

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        ScreenUtils.drawSubWindow(g2, x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("[ESC] Back", x + 25, y + 60);

        x = gp.tileSize * 12;
        ScreenUtils.drawSubWindow(g2, x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("Your Coin: " + gp.player.coin, x + 25, y + 60);

        int idx = ScreenUtils.itemIndex(npcSlotCol, npcSlotRow);
        if (npc != null && idx < npc.inventory.size()) {
            x = (int) (gp.tileSize * 5.5);
            y = (int) (gp.tileSize * 5.5);
            ScreenUtils.drawSubWindow(g2, x, y, (int) (gp.tileSize * 2.5), gp.tileSize);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null);
            String text = "" + ((ItemEntity) npc.inventory.get(idx)).price;
            g2.drawString(text, ScreenUtils.rightAlignedX(g2, text, gp.tileSize * 8 - 20), y + 34);
        }
    }

    private void trade_sell(Graphics2D g2) {
        ScreenUtils.drawInventory(g2, gp, gp.player, true, playerSlotCol, playerSlotRow);

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        ScreenUtils.drawSubWindow(g2, x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("[ESC] Back", x + 25, y + 60);

        x = gp.tileSize * 12;
        ScreenUtils.drawSubWindow(g2, x, y, gp.tileSize * 6, gp.tileSize * 2);
        g2.drawString("Your Coin: " + gp.player.coin, x + 25, y + 60);

        int idx = ScreenUtils.itemIndex(playerSlotCol, playerSlotRow);
        if (idx < gp.player.inventory.size()) {
            x = (int) (gp.tileSize * 15.5);
            y = gp.tileSize * 5;
            ScreenUtils.drawSubWindow(g2, x, y, (int) (gp.tileSize * 2.5), gp.tileSize);
            g2.drawImage(coin, x + 10, y + 10, 32, 32, null);
            String text = "" + ((ItemEntity) gp.player.inventory.get(idx)).price / 2;
            g2.drawString(text, ScreenUtils.rightAlignedX(g2, text, gp.tileSize * 18 - 20), y + 34);
        }
    }
}
