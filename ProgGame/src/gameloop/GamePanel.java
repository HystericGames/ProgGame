package gameloop;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import enemy.*;
import handler.*;
import player.Player;
import player.Weapon;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = -452762559258401813L;
	public static int WIDTH = 1080;
	public static int HEIGHT = 720;

	private Thread thread;
	protected boolean running;

	private BufferedImage image;
	private Graphics2D g;

	protected Player player;
	public ArrayList<Enemy> enemies;
	protected ArrayList<Weapon> bullets;

	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	private int waveNumber;

	private int score = 0;
	private int kills=0;
	private int medals=0;

	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocusInWindow();
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
		enemies = new ArrayList<>();
		bullets = new ArrayList<>();
		waveNumber = 0;
		running = true;

		KeyMouseHandler inputHandler = new KeyMouseHandler(player, bullets);
		addKeyListener(inputHandler);
		addMouseListener(inputHandler);
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

		for (Enemy enemy : enemies) {
			enemy.update();
		}

		if (enemies.isEmpty()) {
			waveNumber++;
			spawnEnemies(waveNumber * 2);
		}

		CollisionHandler.handleEnemyCollisions(enemies);

		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).update()) {
				bullets.remove(i);
				i--;
			}
		}

		score += CollisionHandler.handleBulletEnemyCollisions(bullets, enemies, this);

		CollisionHandler.handlePlayerEnemyCollisions(player, enemies);

		if (player.isDead()) {
			running = false;
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

		if (!player.isDead()) {
			player.draw(g);
			for (Enemy enemy : enemies) {
				enemy.draw(g);
			}
			for (Weapon bullet : bullets) {
				bullet.draw(g);
			}
			g.setColor(Color.BLACK);
			g.drawString("Score: " + score, 20, 30);
			g.drawString("Kills: " + kills, 20, 50);
			g.drawString("Medals: " + medals, 20, 70);
		} else {
			drawGameOverScreen();
		}
	}

	private void drawGameOverScreen() {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		String text = "Game Over";
		int stringWidth = g.getFontMetrics().stringWidth(text);
		g.drawString(text, (WIDTH - stringWidth) / 2, HEIGHT / 2 - 20);

		g.setFont(new Font("Arial", Font.PLAIN, 24));
		String scoreText = "Final Score: " + score;
		int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
		g.drawString(scoreText, (WIDTH - scoreWidth) / 2, HEIGHT / 2 + 20);
	}

	@Override
	protected void paintComponent(Graphics gPanel) {
		super.paintComponent(gPanel);
		gPanel.drawImage(image, 0, 0, null);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public static int getWIDTH() {
		return WIDTH;
	}

	public static int getHEIGHT() {
		return HEIGHT;
	}

	public void increaseKillCount() {
		kills++;
	}

	public void increaseMedalCount() {
		medals++;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getMedals() {
		return medals;
	}

	public void setMedals(int medals) {
		this.medals = medals;
	}
	
	

}
