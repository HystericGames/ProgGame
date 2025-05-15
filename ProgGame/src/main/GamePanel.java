package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import player.Player;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = -6830395646660633218L;

    private Color bgColor;
    private Graphics2D g;
    private Image image;
    private Player player;

    public static int WIDTH;
    public static int HEIGHT;

    public GamePanel() {
        bgColor = new Color(255, 255, 255);
        setBackground(bgColor);

        WIDTH = 1080;
        HEIGHT = 720;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        player = new Player();
        
        run();
    }

    public void run() {
        render();
        repaint();
    }

    private void render() {
        g.setColor(bgColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        player.draw(g);
    }

    @Override
    protected void paintComponent(Graphics gPanel) {
        super.paintComponent(gPanel);
        gPanel.drawImage(image, 0, 0, null);
    }
}
