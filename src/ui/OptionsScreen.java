package ui;

import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OptionsScreen implements Screen {

    GamePanel gp;
    public int commandNum = 0;
    public int subState   = 0;

    public OptionsScreen(GamePanel gp) { this.gp = gp; }

    @Override
    public void onKeyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameState.PLAY;
            commandNum = 0;
            subState   = 0;
            return;
        }

        if (code == KeyEvent.VK_ENTER) {
            gp.playSE(4);
            handleEnter();
            return;
        }

        int maxCmd = switch (subState) {
            case 0  -> 6;
            case 3  -> 1;
            default -> 0;
        };

        if (code == KeyEvent.VK_W) { commandNum--; gp.playSE(11); if (commandNum < 0)    commandNum = maxCmd; }
        if (code == KeyEvent.VK_S) { commandNum++; gp.playSE(11); if (commandNum > maxCmd) commandNum = 0; }

        if (code == KeyEvent.VK_A && subState == 0) {
            if (commandNum == 1 && gp.music.volumeScale > 0) { gp.music.volumeScale--; gp.music.checkVolume(); gp.playSE(11); }
            if (commandNum == 2 && gp.se.volumeScale   > 0) { gp.se.volumeScale--;                            gp.playSE(11); }
        }
        if (code == KeyEvent.VK_D && subState == 0) {
            if (commandNum == 1 && gp.music.volumeScale < 5) { gp.music.volumeScale++; gp.music.checkVolume(); gp.playSE(11); }
            if (commandNum == 2 && gp.se.volumeScale   < 5) { gp.se.volumeScale++;                             gp.playSE(11); }
        }
    }

    private void handleEnter() {
        switch (subState) {
            case 0 -> {
                switch (commandNum) {
                    case 0 -> { gp.fullScreenOn = !gp.fullScreenOn; subState = 1; commandNum = 0; }
                    case 3 -> { subState = 2; commandNum = 0; }
                    case 4 -> gp.saveData.save();
                    case 5 -> { subState = 3; commandNum = 0; }
                    case 6 -> { gp.gameState = GameState.PLAY; commandNum = 0; subState = 0; }
                }
            }
            case 1 -> { if (commandNum == 0) subState = 0; }
            case 2 -> { if (commandNum == 0) { subState = 0; commandNum = 3; } }
            case 3 -> {
                if (commandNum == 0) { subState = 0; gp.gameState = GameState.TITLE; gp.stopMusic(); }
                if (commandNum == 1) { subState = 0; commandNum = 5; }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameW = gp.tileSize * 8;
        int frameH = gp.tileSize * 10;
        ScreenUtils.drawSubWindow(g2, frameX, frameY, frameW, frameH);

        switch (subState) {
            case 0 -> drawTop(g2, frameX, frameY);
            case 1 -> drawFullScreenNote(g2, frameX, frameY);
            case 2 -> drawControls(g2, frameX, frameY);
            case 3 -> drawQuitConfirm(g2, frameX, frameY);
        }
    }

    private void drawTop(Graphics2D g2, int fx, int fy) {
        String title = "Options";
        g2.drawString(title, ScreenUtils.centeredX(g2, title, gp.screenWidth), fy + gp.tileSize);

        int tx = fx + gp.tileSize;
        int ty = fy + gp.tileSize * 3;

        draw_row(g2, "Full Screen",    tx, ty, 0); ty += gp.tileSize;
        draw_row(g2, "Music",          tx, ty, 1); ty += gp.tileSize;
        draw_row(g2, "Sound Effects",  tx, ty, 2); ty += gp.tileSize;
        draw_row(g2, "Control",        tx, ty, 3); ty += gp.tileSize;
        draw_row(g2, "Save",           tx, ty, 4); ty += gp.tileSize;
        draw_row(g2, "Quit",           tx, ty, 5); ty += gp.tileSize * 2;
        draw_row(g2, "Back",           tx, ty, 6);

        // Widgets on the right side
        int wx = fx + (int) (gp.tileSize * 4.5);
        int wy = fy + gp.tileSize * 2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(wx, wy, 24, 24);
        if (gp.fullScreenOn) g2.fillRect(wx, wy, 24, 24);

        wy += gp.tileSize;
        g2.drawRect(wx, wy, 120, 24);
        g2.fillRect(wx, wy, 24 * gp.music.volumeScale, 24);

        wy += gp.tileSize;
        g2.drawRect(wx, wy, 120, 24);
        g2.fillRect(wx, wy, 24 * gp.se.volumeScale, 24);

        gp.config.saveConfig();
    }

    private void draw_row(Graphics2D g2, String label, int tx, int ty, int cmd) {
        g2.drawString(label, tx, ty);
        if (commandNum == cmd && gp.uTool.flickerImage(350)) g2.drawString(">", tx - 25, ty);
    }

    private void drawFullScreenNote(Graphics2D g2, int fx, int fy) {
        int tx = fx + gp.tileSize;
        int ty = fy + gp.tileSize * 3;
        for (String line : "The change will take effect \n after you restart the game. ".split("\n")) {
            g2.drawString(line, tx, ty);
            ty += 40;
        }
        ty = fy + gp.tileSize * 9;
        draw_row(g2, "Back", tx, ty, 0);
    }

    private void drawControls(Graphics2D g2, int fx, int fy) {
        int tx = fx + gp.tileSize;
        int ty = fy + gp.tileSize * 2;
        g2.drawString("Move",             tx, ty); ty += gp.tileSize;
        g2.drawString("Confirm/Attack",   tx, ty); ty += gp.tileSize;
        g2.drawString("Shoot/Cast",       tx, ty); ty += gp.tileSize;
        g2.drawString("Character Screen", tx, ty); ty += gp.tileSize;
        g2.drawString("Pause",            tx, ty); ty += gp.tileSize;
        g2.drawString("Options",          tx, ty);

        tx = fx + gp.tileSize * 6;
        ty = fy + gp.tileSize * 2;
        g2.drawString("WASD",  tx, ty); ty += gp.tileSize;
        g2.drawString("ENTER", tx, ty); ty += gp.tileSize;
        g2.drawString("F",     tx, ty); ty += gp.tileSize;
        g2.drawString("C",     tx, ty); ty += gp.tileSize;
        g2.drawString("P",     tx, ty); ty += gp.tileSize;
        g2.drawString("ESC",   tx, ty);

        tx = fx + gp.tileSize;
        ty = fy + gp.tileSize * 9;
        draw_row(g2, "Back", tx, ty, 0);
    }

    private void drawQuitConfirm(Graphics2D g2, int fx, int fy) {
        int tx = fx + gp.tileSize;
        int ty = fy + gp.tileSize * 3;
        for (String line : "Quit the game and \n return to the title screen?".split("\n")) {
            g2.drawString(line, tx, ty);
            ty += 40;
        }
        ty += gp.tileSize * 3;
        String yes = "Yes";
        int yx = ScreenUtils.centeredX(g2, yes, gp.screenWidth);
        g2.drawString(yes, yx, ty);
        if (commandNum == 0 && gp.uTool.flickerImage(350)) g2.drawString(">", yx - 25, ty);

        ty += gp.tileSize;
        String no = "No";
        int nx = ScreenUtils.centeredX(g2, no, gp.screenWidth);
        g2.drawString(no, nx, ty);
        if (commandNum == 1 && gp.uTool.flickerImage(350)) g2.drawString(">", nx - 25, ty);
    }
}
