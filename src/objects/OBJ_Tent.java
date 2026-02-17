package objects;

import entity.Entity;
import main.GamePanel;

public class OBJ_Tent extends Entity {

    GamePanel gp;
    public OBJ_Tent(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Tent";
        down1 = setup("/objects/tent", gp.tileSize, gp.tileSize);
        description = "[Tent]\n You can sleep into \nthe next day";
        price = 300;
        stackable = true;
    }
    public boolean use(Entity entity) {

        gp.gameState = gp.sleepState;
        gp.playSE(16);
        gp.player.life = gp.player.maxLife;
        gp.player.mana = gp.player.maxMana;
        gp.player.getPlayerSleepingImage(down1);
        return true; // return false if you want it to stay in inventory
    }
}
