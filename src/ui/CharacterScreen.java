package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CharacterScreen implements Screen {

    GamePanel gp;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;

    public CharacterScreen(GamePanel gp) { this.gp = gp; }

    public int getItemIndex() {
        return ScreenUtils.itemIndex(playerSlotCol, playerSlotRow);
    }

    @Override
    public void onKeyPressed(int code) {
        if (code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) gp.gameState = GameState.PLAY;
        if (code == KeyEvent.VK_ENTER) gp.player.selectItem(playerSlotCol, playerSlotRow);
        if (code == KeyEvent.VK_W && playerSlotRow > 0) { playerSlotRow--; gp.playSE(11); }
        if (code == KeyEvent.VK_S && playerSlotRow < 3) { playerSlotRow++; gp.playSE(11); }
        if (code == KeyEvent.VK_A && playerSlotCol > 0) { playerSlotCol--; gp.playSE(11); }
        if (code == KeyEvent.VK_D && playerSlotCol < 4) { playerSlotCol++; gp.playSE(11); }
    }

    @Override
    public void draw(Graphics2D g2) {
        drawStats(g2);
        ScreenUtils.drawInventory(g2, gp, gp.player, true, playerSlotCol, playerSlotRow);
    }

    private void drawStats(Graphics2D g2) {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize - 20;
        final int frameWidth  = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 11;
        ScreenUtils.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineH = 40;

        g2.drawString("Level",      textX, textY); textY += lineH;
        g2.drawString("Life",       textX, textY); textY += lineH;
        g2.drawString("Mana",       textX, textY); textY += lineH;
        g2.drawString("Strength",   textX, textY); textY += lineH;
        g2.drawString("Dexterity",  textX, textY); textY += lineH;
        g2.drawString("Attack",     textX, textY); textY += lineH;
        g2.drawString("Defense",    textX, textY); textY += lineH;
        g2.drawString("Exp",        textX, textY); textY += lineH;
        g2.drawString("Next Level", textX, textY); textY += lineH;
        g2.drawString("Coin",       textX, textY); textY += lineH + 15;
        g2.drawString("Weapon",     textX, textY); textY += lineH + 10;
        g2.drawString("Shield",     textX, textY);

        final int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize;

        String v;
        v = String.valueOf(gp.player.level);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = gp.player.life + "/" + gp.player.maxLife;
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = gp.player.mana + "/" + gp.player.maxMana;
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.strength);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.dexterity);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.attack);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.defense);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.exp);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.nextLevelExp);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;
        v = String.valueOf(gp.player.coin);
        g2.drawString(v, ScreenUtils.rightAlignedX(g2, v, tailX), textY); textY += lineH;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize + 10;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 34, null);
    }
}
