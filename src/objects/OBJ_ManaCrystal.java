package objects;

import entity.EntityType;
import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;

public class OBJ_ManaCrystal extends ItemEntity {

    GamePanel gp;

    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.PICK_UP_ONLY;
        name = "Mana Crystal";
        value = 1;
        down1 = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }

    @Override
    public void use(LivingEntity entity) {
        gp.playSE(2);
        gp.ui.addMessage("Mana " + value);
        entity.mana += value;
    }
}
