package entity;

/**
 * Marks an entity as capable of participating in combat. Implementors
 * define how they react when struck by overriding damageReaction().
 */
public interface ICombatant {
    void damageReaction();
}
