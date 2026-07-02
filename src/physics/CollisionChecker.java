package physics;

import entity.Entity;
import entity.LivingEntity;
import main.GamePanel;
import tile.TiledMap;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(LivingEntity entity) {

        if (!entity.immuneCollision) {
            int entityLeftWorldX = entity.worldX + entity.solidArea.x;
            int entityRightWorldX = entityLeftWorldX + entity.solidArea.x + entity.solidArea.width;
            int entityTopWorldY = entity.worldY + entity.solidArea.y;
            int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

            int entityLeftCol = entityLeftWorldX / gp.tileSize;
            int entityRightCol = entityRightWorldX / gp.tileSize;
            int entityTopRow = entityTopWorldY / gp.tileSize;
            int entityBottomRow = entityBottomWorldY / gp.tileSize;

            TiledMap tm = gp.tileM.tiledMaps[gp.currentMap];

            if (tm != null && tm.intGrid != null) {
                switch (entity.direction) {
                    case "up" -> {
                        entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                        if (isTiledSolid(tm, entityLeftCol, entityTopRow) || isTiledSolid(tm, entityRightCol, entityTopRow))
                            entity.collisionOn = true;
                    }
                    case "down" -> {
                        entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                        if (isTiledSolid(tm, entityLeftCol, entityBottomRow) || isTiledSolid(tm, entityRightCol, entityBottomRow))
                            entity.collisionOn = true;
                    }
                    case "left" -> {
                        entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                        if (isTiledSolid(tm, entityLeftCol, entityTopRow) || isTiledSolid(tm, entityLeftCol, entityBottomRow))
                            entity.collisionOn = true;
                    }
                    case "right" -> {
                        entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                        if (isTiledSolid(tm, entityRightCol, entityTopRow) || isTiledSolid(tm, entityRightCol, entityBottomRow))
                            entity.collisionOn = true;
                    }
                }
            } else {
                int tileNum1, tileNum2;
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
    }

    private boolean isTiledSolid(TiledMap tm, int col, int row) {
        if (col < 0 || col >= tm.intGridWidth || row < 0 || row >= tm.intGridHeight) return true;
        return tm.intGrid[col][row] == 1;
    }

    public int checkObject(LivingEntity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gp.om.obj[1].length; i++) {
            if (gp.om.obj[gp.currentMap][i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gp.om.obj[gp.currentMap][i].solidArea.x = gp.om.obj[gp.currentMap][i].worldX + gp.om.obj[gp.currentMap][i].solidArea.x;
                gp.om.obj[gp.currentMap][i].solidArea.y = gp.om.obj[gp.currentMap][i].worldY + gp.om.obj[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up"    -> entity.solidArea.y -= entity.speed;
                    case "down"  -> entity.solidArea.y += entity.speed;
                    case "left"  -> entity.solidArea.x -= entity.speed;
                    case "right" -> entity.solidArea.x += entity.speed;
                }

                if (entity.solidArea.intersects(gp.om.obj[gp.currentMap][i].solidArea)) {
                    if (gp.om.obj[gp.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.om.obj[gp.currentMap][i].solidArea.x = gp.om.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.om.obj[gp.currentMap][i].solidArea.y = gp.om.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public int checkEntity(LivingEntity entity, Entity[][] target) {
        int index = 999;

        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up"    -> entity.solidArea.y -= entity.speed;
                    case "down"  -> entity.solidArea.y += entity.speed;
                    case "left"  -> entity.solidArea.x -= entity.speed;
                    case "right" -> entity.solidArea.x += entity.speed;
                }

                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if (target[gp.currentMap][i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public boolean checkPlayer(LivingEntity entity) {

        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up"    -> entity.solidArea.y -= entity.speed;
            case "down"  -> entity.solidArea.y += entity.speed;
            case "left"  -> entity.solidArea.x -= entity.speed;
            case "right" -> entity.solidArea.x += entity.speed;
        }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
}
