package entity;

import main.GamePanel;

public interface IProjectile extends IEntity{

    /*
    *
    *
     */
    public void set(int worldX, int worldY, String direction, boolean alive, Entity user);
    public void update();

    public void subtractResource(Entity user);
}