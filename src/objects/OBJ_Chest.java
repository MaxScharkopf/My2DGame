package objects;

import entity.ItemEntity;
import main.GamePanel;

public class OBJ_Chest extends ItemEntity {

    public OBJ_Chest(GamePanel gp) {
        super(gp);

        name = "Chest";
        down1 = setup("/objects/chest", gp.tileSize, gp.tileSize);
    }
}
