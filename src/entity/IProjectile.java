package entity;

/**
 * Contract for any projectile that can be fired by a living entity.
 */
public interface IProjectile extends IUpdatable {

    void set(int worldX, int worldY, String direction, boolean alive, LivingEntity user);

    void subtractResource(LivingEntity user);

    boolean haveResource(LivingEntity user);
}
