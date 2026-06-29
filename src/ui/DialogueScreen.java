package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DialogueScreen implements Screen {

    GamePanel gp;
    public String currentDialogue = "";

    public DialogueScreen(GamePanel gp) { this.gp = gp; }

    @Override
    public void onKeyPressed(int code) {
        if (code != KeyEvent.VK_ENTER) return;

        int npcIndex = gp.cChecker.checkEntity(gp.player, gp.em.npc);
        if (npcIndex != 999) {
            int dIdx = gp.em.npc[gp.currentMap][npcIndex].dialogueIndex;
            if (gp.em.npc[gp.currentMap][npcIndex].dialogues[dIdx] == null) {
                gp.gameState = GameState.PLAY;
                gp.em.npc[gp.currentMap][npcIndex].dialogueIndex = 0;
            } else {
                gp.em.npc[gp.currentMap][npcIndex].speak();
            }
        } else {
            gp.gameState = GameState.PLAY;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width  = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;
        ScreenUtils.drawSubWindow(g2, x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
}
