package entity;

import java.awt.Graphics2D;

/**
 * Marks an object as renderable to the screen each frame.
 * @param g2 the Graphics2D context provided by the game loop for drawing.
 */
public interface IDrawable {
    void draw(Graphics2D g2);
}
