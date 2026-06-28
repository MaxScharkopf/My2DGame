package entity;

import main.GamePanel;

public class Projectile extends LivingEntity {

    LivingEntity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, LivingEntity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }

    @Override
    public void update() {
        if (user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.em.monster);
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack);
                generateParticle(user.projectile, gp.em.monster[gp.currentMap][monsterIndex]);
                alive = false;
            }
        } else {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (!gp.player.invincible && contactPlayer) {
                damagePlayer(attack);
                generateParticle(user.projectile, gp.player);
                alive = false;
            }
        }

        switch (direction) {
            case "up"    -> worldY -= speed;
            case "down"  -> worldY += speed;
            case "left"  -> worldX -= speed;
            case "right" -> worldX += speed;
        }

        life--;
        if (life <= 0) {
            alive = false;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public boolean haveResource(LivingEntity user) { return false; }
    public void subtractResource(LivingEntity user) {}
}
