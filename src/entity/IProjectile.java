package entity;

public interface IProjectile extends IUpdatable {
    void set(int worldX, int worldY, String direction, boolean alive, Entity user);
    void subtractResource(Entity user);
    boolean haveResource(Entity user);
}
