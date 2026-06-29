package ui;

import java.awt.Graphics2D;

public interface Screen {
    default void update() {}
    void onKeyPressed(int code);
    void draw(Graphics2D g2);
}
