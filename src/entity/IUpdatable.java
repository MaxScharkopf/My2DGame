package entity;

/**
 * Marks an object as having per-frame logic. Implement this on anything
 * that needs to advance its state once per game loop tick.
 */
public interface IUpdatable {

    /** Advances the object's state by one game loop tick. */
    void update();
}
