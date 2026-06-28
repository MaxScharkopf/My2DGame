package world;

import entity.Entity;
import main.GamePanel;

public class ObjectManager {

    GamePanel gp;
    public Entity[][] obj;

    public ObjectManager(GamePanel gp) {
        this.gp = gp;
        obj = new Entity[gp.maxMap][20];
    }
}
