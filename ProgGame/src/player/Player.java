package player;

import java.awt.Color;
import java.awt.Graphics2D;


import main.GamePanel;

public class Player{
    private int x, y, width, height;
    private Color color;
    
    private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private int speed;

	private int dx;
	private int dy;

    public Player() {
    	x = GamePanel.WIDTH/2;
	    y = GamePanel.HEIGHT/2;
        width = 50;
        height = 50;
        color = Color.BLACK;
        speed = 6;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    public void update() {
    	if(left) dx = -speed;
		if(right) dx = speed;
		if(up) dy = -speed;
		if(down) dy = speed;
		x+=dx;
		y+=dy;
		if(x < 2) x = 1;
		if(y < 2) y = 1;
		if(x > GamePanel.WIDTH - 2*10-3) x = GamePanel.WIDTH - 2*10-3;
		if(y > GamePanel.HEIGHT - 2*10-3) y = GamePanel.HEIGHT - 2*10-3;
		dx=0;
		dy=0;
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}
    
    

}
