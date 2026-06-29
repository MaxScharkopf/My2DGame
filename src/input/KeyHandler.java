package input;

import main.GamePanel;
import main.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    public boolean showDebugText = false;

    public KeyHandler(GamePanel gp) { this.gp = gp; }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == GameState.PLAY) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_ENTER) enterPressed = true;
            if (code == KeyEvent.VK_F) shotKeyPressed = true;
            if (code == KeyEvent.VK_ESCAPE) gp.gameState = GameState.OPTIONS;
            if (code == KeyEvent.VK_P) gp.gameState = GameState.PAUSE;
            if (code == KeyEvent.VK_C) gp.gameState = GameState.CHARACTER;
            if (code == KeyEvent.VK_T) showDebugText = !showDebugText;
            if (code == KeyEvent.VK_R) {
                switch (gp.currentMap) {
                    case 0 -> gp.tileM.loadMap("/maps/worldV3.txt", 0);
                    case 1 -> gp.tileM.loadMap("/maps/interior01.txt", 1);
                }
            }
        } else {
            gp.ui.onKeyPressed(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_F) shotKeyPressed = false;
    }
}
