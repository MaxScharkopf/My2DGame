package objects;

import entity.EntityType;
import entity.ItemEntity;
import main.GamePanel;

public class OBJ_Axe extends ItemEntity {

    public OBJ_Axe(GamePanel gp) {
        super(gp);

        type = EntityType.AXE;
        name = "Woodcutter's Axe";
        down1 = setup("/objects/weapon/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "]\n Wooden axe.";
        price = 76;
    }
}
