package entity;

import main.GamePanel;

/** Base class for all equippable or consumable items: weapons, shields, potions, keys, etc. */
public abstract class ItemEntity extends Entity {

    public int attackValue;
    public int defenseValue;
    public int price;
    public int value;

    public ItemEntity(GamePanel gp) {
        super(gp);
    }
}
