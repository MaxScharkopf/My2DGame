package objects;

import entity.EntityType;
import entity.ItemEntity;
import main.GamePanel;

public class OBJ_Shield_Blue extends ItemEntity {

    public OBJ_Shield_Blue(GamePanel gp) {
        super(gp);

        type = EntityType.SHIELD;
        name = "Blue Shield";
        down1 = setup("/objects/shield_blue", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\n An old, but strong shield.";
    }
}
