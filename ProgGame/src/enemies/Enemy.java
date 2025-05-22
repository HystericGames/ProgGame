package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.GamePanel;
import player.Player;

public class Enemy {
    private double x, y;
    private double dx, dy;
    private int width, height;
    private Color color;
    private int speed;

    private Player player;

    public Enemy(int type, int rank, Player player) {
    	this.player = player;
        width = 60;
        height = 40;
        speed = 3;
        color = Color.RED;

        int side = (int)(Math.random() * 4);
        switch (side) {
            case 0:
                x = Math.random() * GamePanel.WIDTH;
                y = -height;
                break;
            case 1:
                x = GamePanel.WIDTH;
                y = Math.random() * GamePanel.HEIGHT;
                break;
            case 2:
                x = Math.random() * GamePanel.WIDTH;
                y = GamePanel.HEIGHT;
                break;
            case 3:
                x = -width;
                y = Math.random() * GamePanel.HEIGHT;
                break;
        }

        dx = dy = 0;
    }

    public void update() {
        double targetX = player.getX() + player.getWidth() / 2.0;
        double targetY = player.getY() + player.getHeight() / 2.0;

        double angleToPlayer = Math.atan2(targetY - (y + height / 2.0), targetX - (x + width / 2.0));
        dx = Math.cos(angleToPlayer) * speed;
        dy = Math.sin(angleToPlayer) * speed;

        x += dx;
        y += dy;

    }

    public void draw(Graphics2D g) {
        double angle = Math.atan2(dy, dx);
        AffineTransform old = g.getTransform();

        g.translate(x + width / 2.0, y + height / 2.0);
        g.rotate(angle);

        g.setColor(color);
        g.fillRect(-width / 2, -height / 2, width, height);

        g.setTransform(old);
    }
}
