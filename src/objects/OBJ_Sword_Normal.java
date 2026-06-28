package objects;

import entity.Entity;
import entity.EntityType;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        type = EntityType.SWORD;
        name = "Normal Sword";
        down1 = setup("/objects/weapon/sword_normal", gp.tileSize,gp.tileSize);
        attackValue = 1;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name +"]\n An old sword.";
    }
}
