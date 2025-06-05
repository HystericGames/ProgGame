package enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import player.Player;

public class Infantry extends Enemy {

	public Infantry(int type, int rank, Player player) {
		super(type, rank, player);
		this.width = 40;
		this.height = 60;
		init();
	}

	public void init() {

		dead = false;

		enemyBoundaryColor = Color.DARK_GRAY;

		if (type == 1) {
			damage = 1;
			speed = 5;
			if (rank == 1) {

				health = 1;
				enemyColor = Color.GREEN.brighter();

			}

			if (rank == 2) {
				health = 2;
				enemyColor = Color.GREEN;
			}

		} else if (type == 2) {
			damage = 1;
			speed = 3;
			if (rank == 1) {
				health = 3;
				enemyColor = Color.GREEN;
			} else if (rank == 2) {
				health = 4;
				enemyColor = Color.GREEN.darker();
			}

		} else if (type == 3) {
			damage = 2;
			speed = 2;
			if (rank == 1) {
				health = 5;
				enemyColor = Color.GREEN.darker();
			} else if (rank == 2) {
				health = 5;
				enemyColor = Color.GREEN.darker().darker();
			}

		}
	}

	@Override
	public void draw(Graphics2D g) {
		double angle = Math.atan2(dy, dx);
		AffineTransform old = g.getTransform();

		g.translate(getX() + width / 2.0, getY() + height / 2.0);
		g.rotate(angle);

		// Fill main body
		g.setColor(enemyColor);
		g.fillRect(-width / 2, -height / 2, width, height);

		// Draw boundary
		g.setColor(enemyBoundaryColor);
		g.drawRect(-width / 2, -height / 2, width, height);

		g.setTransform(old);
	}

}
