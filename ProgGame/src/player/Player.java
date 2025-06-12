package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

public class Player {
	private int x, y, width, height;
	private Color color;

	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	private int speed;

	private int dx;
	private int dy;

	private int health;
	private int maxHealth;

	private int weaponUpgradeLevel = 1;

	public Player() {
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT / 2;
		width = 50;
		height = 50;
		color = Color.BLACK;
		speed = 5;

		maxHealth = 10;
		health = maxHealth;
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);

		g.setColor(Color.GRAY);
		g.fillRect(x, y - 15, width, 8);

		g.setColor(Color.RED);
		int healthBarWidth = (int) ((health / (double) maxHealth) * width);
		g.fillRect(x, y - 15, healthBarWidth, 8);

		g.setColor(Color.DARK_GRAY);
		g.drawLine(x, y, x + width, y + height);
		g.drawLine(x + width, y, x, y + height);
	}

	public void update() {
		if (left)
			dx = -speed;
		if (right)
			dx = speed;
		if (up)
			dy = -speed;
		if (down)
			dy = speed;
		x += dx;
		y += dy;
		if (x < 3)
			x = 3;
		if (y < 3)
			y = 3;
		if (x > GamePanel.WIDTH - 50 - 3)
			x = GamePanel.WIDTH - 50 - 3;
		if (y > GamePanel.HEIGHT - 50 - 3)
			y = GamePanel.HEIGHT - 50 - 3;
		dx = 0;
		dy = 0;
	}

	public void damage(int amount) {
		health -= amount;
		if (health < 0)
			health = 0;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) getX(), (int) getY(), width, height);
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public boolean isDead() {
		return health <= 0;
	}

	public int getWeaponUpgradeLevel() {
		return weaponUpgradeLevel;
	}

	public int getWeaponDamage() {
		int temp;
		switch (weaponUpgradeLevel) {
		case 1 -> temp = 1;
		case 2 -> temp = 2;
		case 3 -> temp = 2;
		case 4 -> temp = 2;
		case 5 -> temp = 3;
		default -> temp = 1;
		}
		;
		return temp;
	}

	public void upgradeWeapon() {
		if (weaponUpgradeLevel < 5) {
			weaponUpgradeLevel++;
		}
	}

}
