package entity;

import java.awt.*;

/**
 * Extends IEntity for equipment items that carry a color identity (e.g. for
 * UI tinting or particle effects).
 * @param color the Color value get/set on this equipment piece.
 */
public interface IEntityEquipment extends IEntity
{
    public Color getColor();
    public void setColor(Color color);
}
