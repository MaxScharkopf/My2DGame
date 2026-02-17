package objects;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity  {
    GamePanel gp;
    public OBJ_Axe(GamePanel gp) {

        super(gp);

        type = type_axe;
        name = "Woodcutter's Axe" ;
        down1 = setup("/objects/weapon/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name +"]\n Wooden axe.";
        price = 76;
        knockBackPower = 10;

    }
}
