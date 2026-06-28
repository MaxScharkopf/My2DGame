package objects;

import entity.EntityType;
import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;

public class OBJ_Heart extends ItemEntity {

    GamePanel gp;

    public OBJ_Heart(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.PICK_UP_ONLY;
        name = "Heart";
        value = 2;
        down1 = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
    }

    @Override
    public void use(LivingEntity entity) {
        gp.playSE(2);
        gp.ui.addMessage("Life " + value);
        entity.life += value;
    }
}
