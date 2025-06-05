package enemy;

import java.awt.Color;

import player.Player;

public class Cavalry extends Enemy {

	public Cavalry(int type, int rank, Player player) {
		super(type, rank, player);
		this.width = 30;
		this.height = 40;
		init();
	}

	public void init() {
		dead = false;

		if (type == 1) {
			damage = 1;
			health = 1;
			enemyColor = Color.RED.brighter().brighter();
			if (rank == 1) {
				speed = 8;
			}
			if (rank == 2) {
				speed = 10;
			}
		} else if (type == 2) {
			damage = 2;
			speed = 5;
			knockback = 50;
			enemyColor = Color.RED;
			if (rank == 1) {
				health = 3;
			} else if (rank == 2) {
				health = 5;
			}
		} else if (type == 3) {
			damage = 4;
			health = 5;
			knockback = 40;
			enemyColor = Color.RED.darker().darker();
			if (rank == 1) {
				speed = 4;
			} else if (rank == 2) {
				speed = 6;
			}
		}
	}

}
