package world;

import entity.Entity;
import entity.LivingEntity;
import main.GamePanel;
import tiles_interactive.InteractiveTile;

import java.util.ArrayList;

public class EntityManager {

    GamePanel gp;

    public LivingEntity[][] npc;
    public LivingEntity[][] monster;
    public LivingEntity[][] critter;
    public InteractiveTile[][] iTile;
    public ArrayList<Entity> particleList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();

    public EntityManager(GamePanel gp) {
        this.gp = gp;
        npc     = new LivingEntity[gp.maxMap][10];
        monster = new LivingEntity[gp.maxMap][20];
        critter = new LivingEntity[gp.maxMap][20];
        iTile   = new InteractiveTile[gp.maxMap][50];
    }

    public void update() {
        // NPC
        for (int i = 0; i < npc[1].length; i++) {
            if (npc[gp.currentMap][i] != null) {
                npc[gp.currentMap][i].update();
            }
        }
        // MONSTER
        for (int i = 0; i < monster[1].length; i++) {
            if (monster[gp.currentMap][i] != null) {
                if (monster[gp.currentMap][i].alive && !monster[gp.currentMap][i].dying) {
                    monster[gp.currentMap][i].update();
                }
                if (!monster[gp.currentMap][i].alive) {
                    monster[gp.currentMap][i].checkDrop();
                    monster[gp.currentMap][i] = null;
                }
            }
        }
        // CRITTER
        for (int i = 0; i < critter[1].length; i++) {
            if (critter[gp.currentMap][i] != null) {
                if (critter[gp.currentMap][i].alive) {
                    critter[gp.currentMap][i].update();
                }
                if (!critter[gp.currentMap][i].alive) {
                    critter[gp.currentMap][i] = null;
                }
            }
        }
        // PROJECTILE
        for (int i = 0; i < projectileList.size(); i++) {
            if (projectileList.get(i) != null) {
                if (projectileList.get(i).alive) {
                    projectileList.get(i).update();
                }
                if (!projectileList.get(i).alive) {
                    projectileList.remove(i);
                }
            }
        }
        // PARTICLE
        for (int i = 0; i < particleList.size(); i++) {
            if (particleList.get(i) != null) {
                if (particleList.get(i).alive) {
                    particleList.get(i).update();
                }
                if (!particleList.get(i).alive) {
                    particleList.remove(i);
                }
            }
        }
        // INTERACTIVE TILE
        for (int i = 0; i < iTile[1].length; i++) {
            if (iTile[gp.currentMap][i] != null) {
                iTile[gp.currentMap][i].update();
            }
        }
    }
}
