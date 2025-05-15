package player;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class Player {
    private int x, y, width, height;
    private Color color;

    public Player() {
    	x = GamePanel.WIDTH/2;
	    y = GamePanel.HEIGHT/2;
        width = 50;
        height = 50;
        color = Color.BLACK;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}
