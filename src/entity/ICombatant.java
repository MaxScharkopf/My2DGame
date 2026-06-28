package entity;

/**
 * Marks an entity as capable of participating in combat. Implementors
 * define how they react when struck by overriding damageReaction().
 */
public interface ICombatant {

    /** Called immediately after this entity takes a hit. Override to add knockback, aggro, etc. */
    void damageReaction();
}
