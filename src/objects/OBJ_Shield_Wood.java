package objects;

import entity.EntityType;
import entity.ItemEntity;
import main.GamePanel;

public class OBJ_Shield_Wood extends ItemEntity {

    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        type = EntityType.SHIELD;
        name = "Wood Shield";
        down1 = setup("/objects/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[" + name + "]\n An old shield.";
        price = 33;
    }
}
