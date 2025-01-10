package critters;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class CRIT_Butterfly extends Entity {
    GamePanel gp;

    public CRIT_Butterfly(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // SLIME STATUS
        type = type_critter;
        name = "Butterfly";
        speed = 2;
        maxLife = 1;
        strength = 0;
        attack = 0;
        defense = 0;
        life = maxLife;

        immuneCollision = true;
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage() {
        up1 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        up2 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        down1 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        down2 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        left1 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        left2 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        right1 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
        right2 = setup("/critters/Butterfly_1", gp.tileSize, gp.tileSize);
    }
    public void setAction() {
        actionLockCounter ++;

        if(actionLockCounter == 120) {

            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick a num 1-100

            if(i <= 25) {
                direction = "up";
            }
            if(25 < i && i <= 50) {
                direction = "down";
            }
            if(50 < i && i <= 75) {
                direction = "left";
            }
            if(75 < i) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
}

