package entity;

import main.GamePanel;
import util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity implements IEntity, IUsable {

    GamePanel gp;

    // WALK SPRITES
    public BufferedImage left1, left2, right1, right2, up1, down1, up2, down2;
    // UI / STATIC SPRITES
    public BufferedImage image, image2, image3;

    public int spriteNum = 1;
    public int spriteCounter = 0;

    // COLLISION
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public boolean collisionOn = false;
    public boolean immuneCollision = false;

    // POSITION & IDENTITY
    public int worldX, worldY;
    public String direction = "down";
    public boolean alive = true;

    // METADATA
    public String name;
    public String description = "";
    public EntityType type;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    @Override public void update() {}

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

            g2.drawImage(image, screenX, screenY, null);
        }
    }

    @Override public void use(LivingEntity entity) {}

    // Stubs so Entity references can call these without casting in generateParticle().
    public Color getParticleColor() { return null; }
    public int getParticleSize() { return 0; }
    public int getParticleSpeed() { return 0; }
    public int getParticleMaxLife() { return 0; }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
