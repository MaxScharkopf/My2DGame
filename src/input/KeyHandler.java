package input;

import main.GamePanel;
import main.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    // DEBUG
    public boolean showDebugText = false;


    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (gp.gameState == GameState.TITLE) {
            titleState(code);
        }
        else if (gp.gameState == GameState.PLAY) {
            playState(code);
        }
        else if (gp.gameState == GameState.PAUSE) {
            pauseState(code);
        }
        else if (gp.gameState == GameState.DIALOGUE) {
            dialogueState(code);
        }
        else if (gp.gameState == GameState.CHARACTER) {
            characterState(code);
        }
        else if (gp.gameState == GameState.OPTIONS) {
            optionsState(code);
        }
        else if (gp.gameState == GameState.GAME_OVER) {
            gameOverState(code);
        }
        else if(gp.gameState == GameState.TRADE) {
            tradeState(code);
        }
    }
    public void titleState(int code) {
        if (gp.ui.titleScreen.titleScreenState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                gp.playSE(11);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                gp.playSE(11);
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.ui.titleScreen.titleScreenState = 1;
                }
                if (gp.ui.commandNum == 1) {
                    if (gp.saveData.hasSaveFile()) {
                        gp.saveData.load();
                    }
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
                gp.playSE(4);
            }
        }
        else if (gp.ui.titleScreen.titleScreenState == 1) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                gp.playSE(11);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                gp.playSE(11);
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if(gp.ui.commandNum != 3) {
                    if (gp.ui.commandNum == 0) {
                        System.out.println("Do fighter stuff");
                        gp.gameState = GameState.PLAY;
                    }
                    if (gp.ui.commandNum == 1) {
                        System.out.println("Do Theif stuff");
                        gp.gameState = GameState.PLAY;
                    }
                    if (gp.ui.commandNum == 2) {
                        System.out.println("Do Sorcerer stuff");
                        gp.gameState = GameState.PLAY;
                    }
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 3) {
                    System.out.println("Back");
                    gp.ui.commandNum = 0;
                    gp.ui.titleScreen.titleScreenState = 0;
                }
                gp.playSE(4);
            }
        }
    }
    public void playState(int code) {
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameState.OPTIONS;
        }
        if (code == KeyEvent.VK_P) {
            gp.gameState = GameState.PAUSE;
        }
        if (code == KeyEvent.VK_C) {
            gp.gameState = GameState.CHARACTER;
        }
        if (code == KeyEvent.VK_T) {
            if(!showDebugText) {
                showDebugText = true;
            }
            else {
                showDebugText = false;
            }
        }
        if(code == KeyEvent.VK_R) {
            switch(gp.currentMap) {
                case 0: gp.tileM.loadMap("/maps/worldV3.txt", 0); break;
                case 1: gp.tileM.loadMap("/maps/interior01.txt", 1); break;
            }
        }
    }
    public void pauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.gameState = GameState.PLAY;
        }
    }
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) {

            int npcIndex = gp.cChecker.checkEntity(gp.player, gp.em.npc);
            if (npcIndex != 999) {
                int dialogIndex = gp.em.npc[gp.currentMap][npcIndex].dialogueIndex;
                if (gp.em.npc[gp.currentMap][npcIndex].dialogues[dialogIndex] == null) {
                    gp.gameState = GameState.PLAY;
                    gp.em.npc[gp.currentMap][npcIndex].dialogueIndex = 0;
                } else if (gp.em.npc[npcIndex] != null) {
                    gp.em.npc[gp.currentMap][npcIndex].speak();
                }
            } else {
                gp.gameState = GameState.PLAY;
            }
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_C) {
            gp.gameState = GameState.PLAY;
        }
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        playerInventory(code);
    }
    public void optionsState(int code) {

        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameState.PLAY;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
            gp.playSE(4);
        }
        int maxCommandNum = 0;
        switch(gp.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(11);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(11);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(11);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.playSE(11);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(11);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.playSE(11);
                }
            }
        }
    }
    public void gameOverState(int code) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(11);
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(11);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = GameState.PLAY;
                gp.retry();
            }
            else if(gp.ui.commandNum == 1) {
                gp.gameState = GameState.TITLE;
                gp.restart();
            }
        }
    }
    public void tradeState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(gp.ui.subState == 0) {
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum --;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
                gp.playSE(11);
            }
            if(code == KeyEvent.VK_S) {
                gp.ui.commandNum ++;
                if(gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
                gp.playSE(11);
            }
        }
        if(gp.ui.subState == 1){
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gp.ui.subState = 0;
            }
        }
        if(gp.ui.subState == 2){
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gp.ui.subState = 0;
            }
        }
    }
    public void playerInventory(int code) {
        if (code == KeyEvent.VK_W) {
            if(gp.ui.playerSlotRow != 0){
                gp.ui.playerSlotRow --;
                gp.playSE(11);
            }
        }
        if (code == KeyEvent.VK_S) {
            if(gp.ui.playerSlotRow != 3){
                gp.ui.playerSlotRow ++;
                gp.playSE(11);
            }
        }
        if (code == KeyEvent.VK_A) {
            if(gp.ui.playerSlotCol != 0){
                gp.ui.playerSlotCol --;
                gp.playSE(11);
            }
        }
        if (code == KeyEvent.VK_D) {
            if(gp.ui.playerSlotCol != 4){
                gp.ui.playerSlotCol ++;
                gp.playSE(11);
            }
        }
    }
    public void npcInventory(int code) {
        if (code == KeyEvent.VK_W) {
            if(gp.ui.npcSlotRow != 0){
                gp.ui.npcSlotRow --;
                gp.playSE(11);
            }
        }
        if (code == KeyEvent.VK_S) {
            if(gp.ui.npcSlotRow != 3){
                gp.ui.npcSlotRow ++;
                gp.playSE(11);
            }
        }
        if (code == KeyEvent.VK_A) {
            if(gp.ui.npcSlotCol != 0){
                gp.ui.npcSlotCol --;
                gp.playSE(11);
            }
        }
        if (code == KeyEvent.VK_D) {
            if(gp.ui.npcSlotCol != 4){
                gp.ui.npcSlotCol ++;
                gp.playSE(11);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }
}
