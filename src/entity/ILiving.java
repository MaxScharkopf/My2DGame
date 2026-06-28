package entity;

/**
 * Represents an entity with a health pool that can receive damage and die.
 * @param damage the net damage amount applied to life in takeDamage(int).
 */
public interface ILiving {
    int getLife();
    int getMaxLife();
    boolean isAlive();
    boolean isDying();
    void takeDamage(int damage);
}
