package main;

import entity.Entity;

public class CollisionChecker
{
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        if(!entity.immuneCollision){
            // sets boundary corners for the player barrier
            int entityLeftWorldX = entity.worldX + entity.solidArea.x;
            int entityRightWorldX = entityLeftWorldX + entity.solidArea.x + entity.solidArea.width;
            int entityTopWorldY = entity.worldY + entity.solidArea.y;
            int entityBottomWorldY = entity.worldY + entity.solidArea.y +entity.solidArea.height;

            // sets boundary lines for player barrier
            int entityLeftCol = entityLeftWorldX/gp.tileSize;
            int entityRightCol = entityRightWorldX/gp.tileSize;
            int entityTopRow = entityTopWorldY/gp.tileSize;
            int entityBottomRow = entityBottomWorldY/gp.tileSize;

            int tileNum1, tileNum2;

            /* Checks if a tile marked as 'true' for collision is interacting with the player.
            * The area for which the player can collide with an object is not the size of a tile
            * but rather small, so you don't need to line up directly with the path to walk down it */
            switch (entity.direction) {
                case "up" -> {
                    entityTopRow = ((entityTopWorldY - entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
                case "down" -> {
                    entityBottomRow = ((entityBottomWorldY + entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
                case "left" -> {
                    entityLeftCol = ((entityLeftWorldX - entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
                case "right" -> {
                    entityRightCol = ((entityRightWorldX + entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
            }
        }
    }


    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for(int i = 0; i < gp.obj[1].length ; i++) {
            if(gp.obj[gp.currentMap][i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up" -> {entity.solidArea.y -= entity.speed;}
                    case "down" -> {entity.solidArea.y += entity.speed;}
                    case "left" -> {entity.solidArea.x -= entity.speed;}
                    case "right" -> {entity.solidArea.x += entity.speed;}
                }

                if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    if (gp.obj[gp.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX; // reset x
                entity.solidArea.y = entity.solidAreaDefaultY; // reset y
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX; // reset x
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY; // reset y
            }
        }

        return index;
    }
    // NPC OR MONSTER COLLISION
    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;

        for (int i = 0 ; i < target[1].length; i++) {

            if (target[gp.currentMap][i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up" -> {entity.solidArea.y -= entity.speed;}
                    case "down" -> {entity.solidArea.y += entity.speed;}
                    case "left" -> {entity.solidArea.x -= entity.speed;}
                    case "right" -> {entity.solidArea.x += entity.speed;}
                }

                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if(target[gp.currentMap][i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX; // reset x
                entity.solidArea.y = entity.solidAreaDefaultY; // reset y
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX; // reset x
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY; // reset y
            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up" -> {entity.solidArea.y -= entity.speed;}
            case "down" -> {entity.solidArea.y += entity.speed;}
            case "left" -> {entity.solidArea.x -= entity.speed;}
            case "right" -> {entity.solidArea.x += entity.speed;}
        }
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX; // reset x
        entity.solidArea.y = entity.solidAreaDefaultY; // reset y
        gp.player.solidArea.x = gp.player.solidAreaDefaultX; // reset x
        gp.player.solidArea.y = gp.player.solidAreaDefaultY; // reset y

        return contactPlayer;
    }
}
