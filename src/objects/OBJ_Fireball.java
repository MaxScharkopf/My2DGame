package objects;

import entity.LivingEntity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Fireball extends Projectile {

    GamePanel gp;

    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Fireball";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1    = setup("/projectile/fireball_up_1",    gp.tileSize, gp.tileSize);
        up2    = setup("/projectile/fireball_up_2",    gp.tileSize, gp.tileSize);
        down1  = setup("/projectile/fireball_down_1",  gp.tileSize, gp.tileSize);
        down2  = setup("/projectile/fireball_down_2",  gp.tileSize, gp.tileSize);
        left1  = setup("/projectile/fireball_left_1",  gp.tileSize, gp.tileSize);
        left2  = setup("/projectile/fireball_left_2",  gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/fireball_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/fireball_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public boolean haveResource(LivingEntity user) {
        return user.mana >= useCost;
    }

    @Override
    public void subtractResource(LivingEntity user) {
        user.mana -= useCost;
    }

    @Override public Color getParticleColor() { return new Color(240, 50, 0); }
    @Override public int getParticleSize() { return 10; }
    @Override public int getParticleSpeed() { return 1; }
    @Override public int getParticleMaxLife() { return 20; }
}
