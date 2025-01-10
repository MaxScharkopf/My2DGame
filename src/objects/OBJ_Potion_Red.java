package objects;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;


    public OBJ_Potion_Red(GamePanel gp) {

        super(gp);

        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[Red Potion] \n Heals your life by "+value+".";
        price = 10;
    }
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;

        if(gp.player.life != gp.player.maxLife){
            gp.ui.currentDialogue = "You drink the " + name + "!\n" +
                    " Your life has been recovered by "+ value + ".";

            entity.life += value;

            gp.player.inventory.remove(gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow));
            gp.playSE(10);
        } else {
            gp.ui.currentDialogue = "Your health bar is full. \n This will have no effect";
        }
    }
}
