package objects;

import entity.ItemEntity;
import main.GamePanel;

public class OBJ_Key extends ItemEntity {

    public OBJ_Key(GamePanel gp) {
        super(gp);

        name = "Key";
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
        description = "[" + name +"]\n It opens a door.";
    }
}
