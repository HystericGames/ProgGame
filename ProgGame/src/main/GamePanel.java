package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import enemies.Enemy;
import player.Player;

public class GamePanel extends JPanel implements KeyListener, Runnable {
    private static final long serialVersionUID = 1L;

    public static int WIDTH = 1080;
    public static int HEIGHT = 720;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private Player player;
    private Enemy enemy;

    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
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
        enemy = new Enemy(1, 1, player);
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

            if (wait < 0) wait = 5;

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        player.update();
        enemy.update();
    }

    private void render() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        player.draw(g);
        enemy.draw(g);
    }

    @Override
    protected void paintComponent(Graphics gPanel) {
        super.paintComponent(gPanel);
        gPanel.drawImage(image, 0, 0, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int input = e.getKeyCode();
        if (input == KeyEvent.VK_LEFT) player.setLeft(true);
        if (input == KeyEvent.VK_RIGHT) player.setRight(true);
        if (input == KeyEvent.VK_UP) player.setUp(true);
        if (input == KeyEvent.VK_DOWN) player.setDown(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int input = e.getKeyCode();
        if (input == KeyEvent.VK_LEFT) player.setLeft(false);
        if (input == KeyEvent.VK_RIGHT) player.setRight(false);
        if (input == KeyEvent.VK_UP) player.setUp(false);
        if (input == KeyEvent.VK_DOWN) player.setDown(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
