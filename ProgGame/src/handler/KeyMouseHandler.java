package handler;

import java.awt.event.*;
import java.util.List;

import player.Player;
import player.Weapon;

public class KeyMouseHandler implements KeyListener, MouseListener {

	private Player player;
	private List<Weapon> bullets;

	public KeyMouseHandler(Player player, List<Weapon> bullets) {
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
		int mouseX = e.getX();
		int mouseY = e.getY();

		int playerCenterX = player.getX() + player.getWidth() / 2;
		int playerCenterY = player.getY() + player.getHeight() / 2;
		double angle = Math.atan2(mouseY - playerCenterY, mouseX - playerCenterX);

		bullets.add(new Weapon(angle, playerCenterX, playerCenterY));
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
