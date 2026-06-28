package entity;

import java.awt.*;

/**
 * Extends IEntity for equipment items that carry a color identity (e.g. for
 * UI tinting or particle effects).
 * @param color the Color value get/set on this equipment piece.
 */
public interface IEntityEquipment extends IEntity
{
    /** Returns the color associated with this equipment piece. */
    Color getColor();

    /**
     * Sets the color associated with this equipment piece.
     * @param color the new color to assign.
     */
    void setColor(Color color);
}
