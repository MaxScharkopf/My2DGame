package entity;

/**
 * Marks an item as having an on-use effect (e.g. potions, consumables).
 * @param entity the entity that triggered the use action.
 */
public interface IUsable {
    void use(Entity entity);
}
