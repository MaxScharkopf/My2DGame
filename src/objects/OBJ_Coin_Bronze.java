package objects;

import entity.EntityType;
import entity.ItemEntity;
import entity.LivingEntity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends ItemEntity {

    GamePanel gp;

    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = EntityType.PICK_UP_ONLY;
        name = "Bronze coin";
        value = 1;
        down1 = setup("/objects/coin_bronze", gp.tileSize, gp.tileSize);
    }

    @Override
    public void use(LivingEntity entity) {
        gp.playSE(1);
        gp.ui.addMessage("Coin " + value);
        gp.player.coin += value;
    }
}
