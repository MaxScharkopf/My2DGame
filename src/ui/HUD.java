package ui;

import entity.Entity;
import main.GamePanel;
import objects.OBJ_Heart;
import objects.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HUD {

    GamePanel gp;
    BufferedImage heart_full, heart_half, heart_blank;
    BufferedImage crystal_full, crystal_blank;

    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public HUD(GamePanel gp) {
        this.gp = gp;

        Entity heart  = new OBJ_Heart(gp);
        heart_full  = heart.image;
        heart_half  = heart.image2;
        heart_blank = heart.image3;

        Entity crystal  = new OBJ_ManaCrystal(gp);
        crystal_full  = crystal.image;
        crystal_blank = crystal.image2;
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void drawPlayerLife(Graphics2D g2) {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

        x = (gp.tileSize / 2) - 5;
        y = (int) (gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        x = (gp.tileSize / 2) - 5;
        y = (int) (gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }

    public void drawMessage(Graphics2D g2) {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int count = messageCounter.get(i) + 1;
                messageCounter.set(i, count);
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
}
