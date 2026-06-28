package objects;

import entity.ItemEntity;
import main.GamePanel;

public class OBJ_Boots extends ItemEntity {

    public OBJ_Boots(GamePanel gp) {
        super(gp);

        name = "Boots";
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
    }
}
