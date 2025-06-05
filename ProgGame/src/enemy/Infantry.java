package enemy;

import java.awt.Color;

import player.Player;

public class Infantry extends Enemy {

	public Infantry(int type, int rank, Player player) {
		super(type, rank, player);
		this.width = 40;
		this.height = 50;
		init();
	}

	public void init() {

		dead = false;

		knockback = 80;

		if (type == 1) {
			damage = 1;
			speed = 5;
			if (rank == 1) {
				health = 1;
				enemyColor = Color.BLUE;
			}

			if (rank == 2) {
				health = 2;
				enemyColor = Color.BLUE;
			}

		} else if (type == 2) {
			damage = 1;
			speed = 3;
			if (rank == 1) {
				health = 3;
				enemyColor = Color.BLUE.darker();
			} else if (rank == 2) {
				health = 4;
				enemyColor = Color.BLUE.darker();
			}

		} else if (type == 3) {
			damage = 2;
			speed = 2;
			if (rank == 1) {
				health = 5;
				enemyColor = Color.BLUE.darker().darker();
			} else if (rank == 2) {
				health = 5;
				enemyColor = Color.BLUE.darker().darker();
			}

		}
	}

}
