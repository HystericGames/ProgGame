package ui;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ShopButton {
    private Rectangle bounds;
    private String label;
    private Runnable onClick;

    public ShopButton(int x, int y, int width, int height, String label, Runnable onClick) {
        this.bounds = new Rectangle(x, y, width, height);
        this.label = label;
        this.onClick = onClick;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fill(bounds);
        g.setColor(Color.WHITE);
        g.draw(bounds);
        g.drawString(label, bounds.x + 10, bounds.y + 25);
    }

    public void handleClick(MouseEvent e) {
        if (bounds.contains(e.getPoint()) && onClick != null) {
            onClick.run();
        }
    }
}
