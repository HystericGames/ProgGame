package gameloop;

import enemy.Enemy;
import enemy.Infantry;
import player.Player;

public class EnemyHandler {
	
	private Player player;
	
	
	
	public EnemyHandler(Player player) {
		super();
		this.player = player;
	}



	protected Enemy createRandomEnemy() {
		int type = (int) (Math.random() * 3) + 1; 
		int rank = (int) (Math.random() * 2) + 1; 

		switch (type) {
			case 1:
				return new Infantry(type, rank, player);
			// case 2: return new Tank(type, rank, player);
			// case 3: return new Cavalry(type, rank, player);
			default:
				return new Infantry(type, rank, player); 
		}
	}

}
