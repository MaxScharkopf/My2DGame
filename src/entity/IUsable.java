package entity;

/**
 * Marks an item as having an on-use effect (e.g. potions, consumables).
 * @param entity the entity that triggered the use action.
 */
public interface IUsable {

    /**
     * Applies this item's effect to the given entity.
     * @param entity the entity using this item.
     */
    void use(Entity entity);
}
