package enemy;

import java.awt.Color;

import player.Player;

public class Tank extends Enemy {

	public Tank(int type, int rank, Player player) {
		super(type, rank, player);
		this.width = 30;
		this.height = 60;
		init();
	}

	public void init() {
		dead = false;
		if (type == 1) {
			damage = 2;
			speed = 3;
			knockback=60;
			enemyColor = Color.GREEN;
			if (rank == 1) {
				health = 3;
			}
			if (rank == 2) {
				health = 4;
			}
		} else if (type == 2) {
			damage = 3;
			speed = 2;
			knockback=40;
			enemyColor = Color.GREEN.darker();
			if (rank == 1) {
				health = 5;
			} else if (rank == 2) {
				health = 6;
			}

		} else if (type == 3) {
			speed = 1;
			knockback=10;
			damage = 5;
			enemyColor = Color.GREEN.darker().darker();
			if (rank == 1) {
				health = 8;
			} else if (rank == 2) {
				health = 10;
			}

		}
	}


}
