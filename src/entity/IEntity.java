package entity;

/**
 * Base contract for all game entities. Guarantees that every entity
 * can be updated each tick and drawn to the screen each frame.
 */
public interface IEntity extends IUpdatable, IDrawable {
}
