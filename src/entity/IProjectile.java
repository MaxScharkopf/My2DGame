package entity;

/**
 * Contract for any projectile that can be fired by an entity. Extends IUpdatable
 * so the projectile advances each tick.
 * @param worldX/worldY spawn position, direction travel direction, alive initial
 *        alive state, user the entity that fired the projectile.
 */
public interface IProjectile extends IUpdatable {
    void set(int worldX, int worldY, String direction, boolean alive, Entity user);
    void subtractResource(Entity user);
    boolean haveResource(Entity user);
}
