package entity;

/**
 * Marks an item as having an on-use effect (e.g. potions, consumables).
 */
public interface IUsable {
    void use(LivingEntity entity);
}
