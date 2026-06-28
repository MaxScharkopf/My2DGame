package entity;

public interface ILiving {
    int getLife();
    int getMaxLife();
    boolean isAlive();
    boolean isDying();
    void takeDamage(int damage);
}
