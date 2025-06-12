package handler;

import java.awt.event.*;
import java.util.List;

import javax.swing.Timer;

import main.GamePanel;
import player.Player;
import player.Weapon;

public class KeyMouseHandler implements KeyListener, MouseListener, MouseMotionListener {

	private Player player;
	private List<Weapon> bullets;
	private GamePanel game;

	private boolean mouseHeld = false;
	private Timer fireTimer;
	private long lastShotTime = 0;
	
	private int currentMouseX = 0;
	private int currentMouseY = 0;


	public KeyMouseHandler(GamePanel game, Player player, List<Weapon> bullets) {
		this.game = game;
		this.player = player;
		this.bullets = bullets;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int input = e.getKeyCode();
		if (input == KeyEvent.VK_A)
			player.setLeft(true);
		if (input == KeyEvent.VK_D)
			player.setRight(true);
		if (input == KeyEvent.VK_W)
			player.setUp(true);
		if (input == KeyEvent.VK_S)
			player.setDown(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int input = e.getKeyCode();
		if (input == KeyEvent.VK_A)
			player.setLeft(false);
		if (input == KeyEvent.VK_D)
			player.setRight(false);
		if (input == KeyEvent.VK_W)
			player.setUp(false);
		if (input == KeyEvent.VK_S)
			player.setDown(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!game.isInShop()) {
			mouseHeld = true;
			tryFireBullet(e);
			startFiringLoop(e);
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		mouseHeld = false;
		if (fireTimer != null) {
			fireTimer.stop();
		}
	}
	
	private void startFiringLoop(MouseEvent e) {
		int delay = getFireDelayForLevel(player.getWeaponUpgradeLevel());

		fireTimer = new Timer(delay, evt -> {
			if (!mouseHeld || game.isInShop()) {
				fireTimer.stop();
				return;
			}
			tryFireBullet(e);
		});
		fireTimer.start();
	}
	
	private void tryFireBullet(MouseEvent e) {
		long now = System.currentTimeMillis();
		int delay = getFireDelayForLevel(player.getWeaponUpgradeLevel());

		if (now - lastShotTime >= delay) {
			int playerCenterX = player.getX() + player.getWidth() / 2;
			int playerCenterY = player.getY() + player.getHeight() / 2;
			double angle = Math.atan2(currentMouseY - playerCenterY, currentMouseX - playerCenterX);

			bullets.add(new Weapon(angle, playerCenterX, playerCenterY, player.getWeaponUpgradeLevel()));
			lastShotTime = now;
		}
	}


	private int getFireDelayForLevel(int level) {
		return switch (level) {
		case 1 -> 1000; // Winchester M1886
		case 2 -> 1000; // Enfield M1917 Rifle
		case 3 -> 500; // RSC M1917
		case 4 -> 200; // Colt–Vickers M1915 MG
		case 5 -> 100; // BAR M1918
		default -> 1000;
		};
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		currentMouseX = e.getX();
		currentMouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		currentMouseX = e.getX();
		currentMouseY = e.getY();
	}
}
