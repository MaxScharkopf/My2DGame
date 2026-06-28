package objects;

import entity.LivingEntity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Rock extends Projectile {

    GamePanel gp;

    public OBJ_Rock(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Rock";
        speed = 4;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    private void getImage() {
        up1    = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        up2    = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down1  = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down2  = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left1  = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left2  = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
    }

    @Override
    public boolean haveResource(LivingEntity user) {
        return user.ammo >= useCost;
    }

    @Override
    public void subtractResource(LivingEntity user) {
        user.ammo -= useCost;
    }

    @Override public Color getParticleColor() { return new Color(40, 50, 0); }
    @Override public int getParticleSize() { return 6; }
    @Override public int getParticleSpeed() { return 1; }
    @Override public int getParticleMaxLife() { return 20; }
}
