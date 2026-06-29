package objects;

import entity.EntityType;
import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;
import main.GameState;

public class OBJ_Potion_Red extends ItemEntity {

    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.CONSUMABLE;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[Red Potion] \n Heals your life by " + value + ".";
        price = 10;
    }

    @Override
    public void use(LivingEntity entity) {
        gp.gameState = GameState.DIALOGUE;

        if (gp.player.life != gp.player.maxLife) {
            gp.ui.dialogueScreen.currentDialogue = "You drink the " + name + "!\n" +
                    " Your life has been recovered by " + value + ".";
            entity.life += value;
            gp.player.inventory.remove(gp.ui.characterScreen.getItemIndex());
            gp.playSE(10);
        } else {
            gp.ui.dialogueScreen.currentDialogue = "Your health bar is full. \n This will have no effect";
        }
    }
}
