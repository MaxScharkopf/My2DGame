package entity;

/**
 * Contract for any projectile that can be fired by an entity. Extends IUpdatable
 * so the projectile advances each tick.
 * @param worldX/worldY spawn position, direction travel direction, alive initial
 *        alive state, user the entity that fired the projectile.
 */
public interface IProjectile extends IUpdatable {

    /**
     * Initialises the projectile at a position and direction before adding it to the game.
     * @param worldX spawn X position in world coordinates.
     * @param worldY spawn Y position in world coordinates.
     * @param direction the cardinal direction the projectile travels.
     * @param alive sets the projectile active when true.
     * @param user the entity that fired this projectile.
     */
    void set(int worldX, int worldY, String direction, boolean alive, Entity user);

    /**
     * Deducts the resource cost (mana, ammo, etc.) from the firing entity.
     * @param user the entity paying the cost.
     */
    void subtractResource(Entity user);

    /**
     * Returns true if the firing entity has enough resources to shoot.
     * @param user the entity being checked for resources.
     */
    boolean haveResource(Entity user);
}
