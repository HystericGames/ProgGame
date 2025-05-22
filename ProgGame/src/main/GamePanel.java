package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import enemies.*;
import player.Player;
import player.Weapon;

public class GamePanel extends JPanel implements KeyListener, Runnable, MouseListener {

	private static final long serialVersionUID = -452762559258401813L;
	public static int WIDTH = 1080;
	public static int HEIGHT = 720;

	private Thread thread;
	private boolean running;

	private BufferedImage image;
	private Graphics2D g;

	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Weapon> bullets;

	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	private int waveNumber;

	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);
		addMouseListener(this);
		setBackground(Color.WHITE);
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		player = new Player();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<>();
		waveNumber = 0;

		running = true;
	}

	@Override
	public void run() {
		init();
		long start, elapsed, wait;

		while (running) {
			start = System.nanoTime();

			update();
			render();
			repaint();

			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1_000_000;

			if (wait < 0)
				wait = 5;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		player.update();

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}
		if (enemies.size() == 0) {
			waveNumber++;
			spawnEnemies(waveNumber * 3);
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).update()) {
				bullets.remove(i);
				i--;
			}
		}


	}

	private void spawnEnemies(int count) {
		for (int i = 0; i < count; i++) {
			enemies.add(new EnemyHandler(player).createRandomEnemy());
		}
	}


	private void render() {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		player.draw(g);
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}

	}

	@Override
	protected void paintComponent(Graphics gPanel) {
		super.paintComponent(gPanel);
		gPanel.drawImage(image, 0, 0, null);
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
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
}
