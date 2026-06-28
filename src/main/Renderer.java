package main;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class Renderer {

    GamePanel gp;
    BufferedImage tempScreen;
    Graphics2D g2;
    ArrayList<Entity> entityList = new ArrayList<>();

    public Renderer(GamePanel gp) {
        this.gp = gp;
    }

    public void init() {
        tempScreen = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
    }

    public void draw() {
        long drawStart = 0;
        if (gp.keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        if (gp.gameState == GameState.TITLE) {
            gp.ui.draw(g2);
        } else {
            // TILE
            gp.tileM.draw(g2);

            // INTERACTIVE TILE
            for (int i = 0; i < gp.em.iTile[1].length; i++) {
                if (gp.em.iTile[gp.currentMap][i] != null) {
                    gp.em.iTile[gp.currentMap][i].draw(g2);
                }
            }

            // ADD ENTITIES TO THE LIST
            entityList.add(gp.player);
            for (int i = 0; i < gp.em.npc[1].length; i++) {
                if (gp.em.npc[gp.currentMap][i] != null) entityList.add(gp.em.npc[gp.currentMap][i]);
            }
            for (int i = 0; i < gp.om.obj[1].length; i++) {
                if (gp.om.obj[gp.currentMap][i] != null) entityList.add(gp.om.obj[gp.currentMap][i]);
            }
            for (int i = 0; i < gp.em.monster[1].length; i++) {
                if (gp.em.monster[gp.currentMap][i] != null) entityList.add(gp.em.monster[gp.currentMap][i]);
            }
            for (int i = 0; i < gp.em.critter[1].length; i++) {
                if (gp.em.critter[gp.currentMap][i] != null) entityList.add(gp.em.critter[gp.currentMap][i]);
            }
            for (Entity e : gp.em.projectileList) { if (e != null) entityList.add(e); }
            for (Entity e : gp.em.particleList)   { if (e != null) entityList.add(e); }

            // SORT BY WORLD Y (painter's order)
            entityList.sort(Comparator.comparingInt(e -> e.worldY));

            // DRAW ENTITIES
            for (Entity entity : entityList) {
                entity.draw(g2);
            }
            entityList.clear();

            // UI
            gp.ui.draw(g2);
        }

        if (gp.keyH.showDebugText) {
            long passed = System.nanoTime() - drawStart;
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10, y = 400, lineHeight = 20;
            g2.drawString("WorldX " + gp.player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY " + gp.player.worldY, x, y); y += lineHeight;
            g2.drawString("Col " + (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize, x, y); y += lineHeight;
            g2.drawString("Row " + (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize, x, y); y += lineHeight;
            g2.drawString("Draw Time: " + passed, x, y);
        }
    }

    public void drawToScreen() {
        Graphics g = gp.getGraphics();
        g.drawImage(tempScreen, 0, 0, gp.screenWidth2, gp.screenHeight2, null);
        g.dispose();
    }
}
