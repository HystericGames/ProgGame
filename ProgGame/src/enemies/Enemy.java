package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.GamePanel;
import player.Player;

public abstract class Enemy {
    protected double x, y;
    protected double dx, dy;
    protected int width, height;
    protected Color enemyColor;
    protected Color enemyBoundaryColor;
    protected int speed;
    
    protected int type;
    protected int rank;
    
    protected boolean dead;
    protected int health;

    private Player player;

    public Enemy(int type, int rank, Player player) {
    	this.player = player;
        this.type = type;
        this.rank = rank;
        
        this.width = 40; 
        this.height = 40;

        // Random spawn
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

        g.setColor(enemyColor);
        g.fillRect(-width / 2, -height / 2, width, height);

        g.setTransform(old);
    }
}
