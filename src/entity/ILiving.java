package entity;

/**
 * Represents an entity with a health pool that can receive damage and die.
 * @param damage the net damage amount applied to life in takeDamage(int).
 */
public interface ILiving {

    /** Returns the entity's current life total. */
    int getLife();

    /** Returns the entity's maximum possible life. */
    int getMaxLife();

    /** Returns true if the entity is currently alive. */
    boolean isAlive();

    /** Returns true if the entity is in its dying animation. */
    boolean isDying();

    /**
     * Applies damage to this entity, ignoring the hit if invincible.
     * @param damage net damage to subtract from life.
     */
    void takeDamage(int damage);
}
