package ui;

import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;

import java.awt.*;

public class ScreenUtils {

    public static void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public static int centeredX(Graphics2D g2, String text, int screenWidth) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return screenWidth / 2 - length / 2;
    }

    public static int rightAlignedX(Graphics2D g2, String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }

    public static int itemIndex(int slotCol, int slotRow) {
        return slotCol + slotRow * 5;
    }

    public static void drawInventory(Graphics2D g2, GamePanel gp, LivingEntity entity,
                                     boolean cursor, int slotCol, int slotRow) {
        int frameX, frameY;
        final int frameWidth  = gp.tileSize * 6;
        final int frameHeight = gp.tileSize * 5;

        if (entity == gp.player) {
            frameX = gp.tileSize * 13;
            frameY = gp.tileSize - 20;
        } else {
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
        }

        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        final int slotSize = gp.tileSize + 3;

        for (int i = 0; i < entity.inventory.size(); i++) {
            if (entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }
            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;
            if (i == 4 || i == 9 || i == 14) { slotX = slotXstart; slotY += slotSize; }
        }

        if (cursor) {
            int cursorX = slotXstart + slotSize * slotCol;
            int cursorY = slotYstart + slotSize * slotRow;
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, gp.tileSize, gp.tileSize, 10, 10);

            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int textX   = dFrameX + 20;
            int textY   = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(28F));

            int idx = itemIndex(slotCol, slotRow);
            if (idx < entity.inventory.size()) {
                drawSubWindow(g2, dFrameX, dFrameY, frameWidth, gp.tileSize * 3);
                int nameCount = 0;
                for (String line : entity.inventory.get(idx).description.split("\n")) {
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
}
