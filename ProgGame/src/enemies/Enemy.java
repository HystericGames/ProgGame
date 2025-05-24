package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import main.GamePanel;
import player.Player;

public abstract class Enemy {
    private double x;
	private double y;
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
        
        this.dead = false;

        // Random spawn
        int side = (int)(Math.random() * 4);
        switch (side) {
            case 0:
                setX(Math.random() * GamePanel.WIDTH);
                setY(-height);
                break;
            case 1:
                setX(GamePanel.WIDTH);
                setY(Math.random() * GamePanel.HEIGHT);
                break;
            case 2:
                setX(Math.random() * GamePanel.WIDTH);
                setY(GamePanel.HEIGHT);
                break;
            case 3:
                setX(-width);
                setY(Math.random() * GamePanel.HEIGHT);
                break;
        }
        dx = dy = 0;
    }

    public void update() {
        double targetX = player.getX() + player.getWidth() / 2.0;
        double targetY = player.getY() + player.getHeight() / 2.0;

        double angleToPlayer = Math.atan2(targetY - (getY() + height / 2.0), targetX - (getX() + width / 2.0));
        dx = Math.cos(angleToPlayer) * speed;
        dy = Math.sin(angleToPlayer) * speed;

        setX(getX() + dx);
        setY(getY() + dy);

    }

    public void draw(Graphics2D g) {
        double angle = Math.atan2(dy, dx);
        AffineTransform old = g.getTransform();

        g.translate(getX() + width / 2.0, getY() + height / 2.0);
        g.rotate(angle);

        g.setColor(enemyColor);
        g.fillRect(-width / 2, -height / 2, width, height);

        g.setTransform(old);
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int) getX(), (int) getY(), width, height);
    }

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	

}
