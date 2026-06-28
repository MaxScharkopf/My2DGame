package world;

import critters.CRIT_Butterfly;
import entity.NPC_Merchant;
import entity.NPC_OldMan;
import main.GamePanel;
import monster.MON_GreenSlime;
import objects.*;
import tiles_interactive.IT_DryTree;
import tiles_interactive.InteractiveTile;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        int mapNum = 0;
        int i = 0;
        gp.om.obj[mapNum][i] = new OBJ_Axe(gp);
        gp.om.obj[mapNum][i].worldX = gp.tileSize * 22;
        gp.om.obj[mapNum][i].worldY = gp.tileSize * 16;
        i++;
        gp.om.obj[mapNum][i] = new OBJ_Shield_Blue(gp);
        gp.om.obj[mapNum][i].worldX = gp.tileSize * 22;
        gp.om.obj[mapNum][i].worldY = gp.tileSize * 15;
        i++;
    }
    public void setNPC() {

        // MAP 0
        int mapNum = 0;
        int i = 0;
        gp.em.npc[mapNum][i] = new NPC_OldMan(gp);
        gp.em.npc[mapNum][i].worldX = gp.tileSize * 21;
        gp.em.npc[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        // MAP 1
        mapNum = 1;
        i = 0;
        gp.em.npc[mapNum][i] = new NPC_Merchant(gp);
        gp.em.npc[mapNum][i].worldX = gp.tileSize * 12;
        gp.em.npc[mapNum][i].worldY = gp.tileSize * 7;
    }
    public void setMonster() {

        int mapNum = 0;
        int i = 0;
        gp.em.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.em.monster[mapNum][i].worldX = gp.tileSize * 23;
        gp.em.monster[mapNum][i].worldY = gp.tileSize * 36;
        i++;
        gp.em.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.em.monster[mapNum][i].worldX = gp.tileSize * 23;
        gp.em.monster[mapNum][i].worldY = gp.tileSize * 37;
        i++;
        gp.em.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.em.monster[mapNum][i].worldX = gp.tileSize * 35;
        gp.em.monster[mapNum][i].worldY = gp.tileSize * 39;
        i++;
        gp.em.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.em.monster[mapNum][i].worldX = gp.tileSize * 36;
        gp.em.monster[mapNum][i].worldY = gp.tileSize * 36;
    }
    public void setCritter() {

        int mapNum = 0;
    }
    public void setInteractiveTile() {

        int mapNum = 0;
        int i = 0;

        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 27, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 28, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 29, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 30, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 31, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 32, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 33, 12); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 31, 21); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 18, 40); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 17, 40); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 16, 40); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 15, 40); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 14, 40); i++;
        gp.em.iTile[mapNum][i] = new IT_DryTree(gp, 13, 40);
    }
}
