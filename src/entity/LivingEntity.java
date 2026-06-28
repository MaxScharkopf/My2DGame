package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/** Base class for all entities that have HP, can move, and participate in combat. */
public abstract class LivingEntity extends Entity implements ILiving, ICombatant {

    // ATTACK SPRITES
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2;
    public BufferedImage attackLeft1, attackLeft2, attackRight1, attackRight2;

    // HP & STATS
    public int life, maxLife;
    public int mana, maxMana;
    public int ammo;
    public int level, strength, dexterity, attack, defense;
    public int exp, nextLevelExp, coin;
    public int speed;

    // PROJECTILE COST
    public int useCost;

    // LIVING STATE
    public boolean dying = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean onPath = false;
    boolean hpBarOn = false;

    // MOVEMENT
    public String lastDirection;

    // DIALOGUE
    public String[] dialogues = new String[20];
    public int dialogueIndex = 0;

    // INVENTORY
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public ItemEntity currentWeapon;
    public ItemEntity currentShield;
    public Projectile projectile;

    // COUNTERS
    public int actionLockCounter = 0;
    public int soundCounter = 0;
    public int invincibleCounter = 0;
    public int dyingCounter = 0;
    int hpBarCounter = 0;
    public int shotAvailableCounter;

    public LivingEntity(GamePanel gp) {
        super(gp);
    }

    // ILiving
    @Override public int getLife() { return life; }
    @Override public int getMaxLife() { return maxLife; }
    @Override public boolean isAlive() { return alive; }
    @Override public boolean isDying() { return dying; }
    @Override
    public void takeDamage(int damage) {
        if (!invincible) {
            life -= damage;
            invincible = true;
        }
    }

    // ICombatant
    @Override public void damageReaction() {}

    public void setAction() {}
    public void checkDrop() {}

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.om.obj[1].length; i++) {
            if (gp.om.obj[gp.currentMap][i] == null) {
                gp.om.obj[gp.currentMap][i] = droppedItem;
                gp.om.obj[gp.currentMap][i].worldX = worldX;
                gp.om.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    public Color getParticleColor() { return null; }
    public int getParticleSize() { return 0; }
    public int getParticleSpeed() { return 0; }
    public int getParticleMaxLife() { return 0; }

    public void generateParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife,  2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2,  1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife,  2,  1);

        gp.em.particleList.add(p1);
        gp.em.particleList.add(p2);
        gp.em.particleList.add(p3);
        gp.em.particleList.add(p4);
    }

    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.em.npc);
        gp.cChecker.checkEntity(this, gp.em.monster);
        gp.cChecker.checkEntity(this, gp.em.iTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == EntityType.MONSTER && contactPlayer) {
            damagePlayer(attack);
        }
    }

    public void damagePlayer(int attack) {
        gp.combat.hitPlayer(attack);
    }

    @Override
    public void update() {
        setAction();
        checkCollision();

        if (!collisionOn) {
            switch (direction) {
                case "up"    -> worldY -= speed;
                case "down"  -> worldY += speed;
                case "left"  -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        spriteCounter++;
        if (spriteCounter > 24) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up"    -> { if (spriteNum == 1) image = up1; else image = up2; }
                case "down"  -> { if (spriteNum == 1) image = down1; else image = down2; }
                case "left"  -> { if (spriteNum == 1) image = left1; else image = left2; }
                case "right" -> { if (spriteNum == 1) image = right1; else image = right2; }
            }

            if (type == EntityType.MONSTER && hpBarOn) {
                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                if (hpBarValue > 0) {
                    g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
                }
                hpBarCounter++;
                if (hpBarCounter > 300) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);
            changeAlpha(g2, 1f);
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i = 5;
        if (dyingCounter <= i)           changeAlpha(g2, 0f);
        if (dyingCounter > i  && dyingCounter <= i*2) changeAlpha(g2, 1f);
        if (dyingCounter > i*2 && dyingCounter <= i*3) changeAlpha(g2, 0f);
        if (dyingCounter > i*3 && dyingCounter <= i*4) changeAlpha(g2, 1f);
        if (dyingCounter > i*4 && dyingCounter <= i*5) changeAlpha(g2, 0f);
        if (dyingCounter > i*5 && dyingCounter <= i*6) changeAlpha(g2, 1f);
        if (dyingCounter > i*6 && dyingCounter <= i*7) changeAlpha(g2, 0f);
        if (dyingCounter > i*7 && dyingCounter <= i*8) changeAlpha(g2, 1f);
        if (dyingCounter > i*8) alive = false;
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up"    -> direction = "down";
            case "down"  -> direction = "up";
            case "left"  -> direction = "right";
            case "right" -> direction = "left";
        }
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

        if (gp.pFinder.search()) {
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            int enLeftX  = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY   = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
                if (enLeftX > nextX) {
                    direction = "up";
                    checkCollision();
                    if (collisionOn) direction = "left";
                }
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
                if (enLeftX > nextX) {
                    direction = "down";
                    checkCollision();
                    if (collisionOn) direction = "left";
                }
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                direction = (enLeftX > nextX) ? "left" : "right";
            }
        }
    }
}
