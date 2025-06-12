package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

public class Weapon {
	private int x;
	private int y;

	private int width;
	private int height;

	private double dx;
	private double dy;
	
	private int speed;

	public double angle;
	
	private int damage;

	private Color bulletColor;
	private Color bulletBoundaryColor;

	public Weapon(double angle, int x, int y, int weaponLevel) {
	    this.x = x;
	    this.y = y;
	    speed = 10;
	    this.angle = angle;
	    setDamage(weaponLevel);
	    dx = Math.cos(angle) * speed;
	    dy = Math.sin(angle) * speed;
	    width = 20;
	    height = 5;
	    bulletColor = Color.ORANGE;
	    bulletBoundaryColor = bulletColor.darker().darker();
	}


	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

	public double getAngle() {
		return angle;
	}

	public boolean update() {

		x += dx;
		y += dy;
		if (x > GamePanel.WIDTH - width || y > GamePanel.HEIGHT - height || x < -width || y < -height)
			return true;
		return false;

	}

	public void draw(Graphics2D g) {
		// Save current transform
		java.awt.geom.AffineTransform old = g.getTransform();

		// Set rotation around the bullet center
		g.translate(x, y);
		g.rotate(angle);

		// Set bullet style
		g.setStroke(new BasicStroke(2));
		g.setColor(bulletColor);

		// Mitte des Spielers als Start
		g.fillRect(-width / 2, -height / 2, width, height);

		g.setColor(bulletBoundaryColor);
		g.drawRect(-width / 2, -height / 2, width, height);

		// Restore old transform
		g.setTransform(old);
	}
	
	public Rectangle getBounds() {
	    return new Rectangle(x - width / 2, y - height / 2, width, height);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int level) {
		switch (level) {
	        case 1 -> this.damage = 1;
	        case 2 -> this.damage = 2;
	        case 3 -> this.damage = 2;
	        case 4 -> this.damage = 2;
	        case 5 -> this.damage = 3;
        default -> this.damage = 1;
		};
	}




}
