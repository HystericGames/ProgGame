package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.GamePanel;
import player.Player;

public class Enemy {
    private double x, y;
    private double dx, dy;
    private int width, height;
    private Color color;
    private int speed;

    private Player player; // Referenz auf den Spieler

    public Enemy(int type, int rank, Player player) {
        this.player = player;

        width = 60;
        height = 40;
        speed = 3;
        color = Color.RED;

        // Zufällige Seite (0 = oben, 1 = rechts, 2 = unten, 3 = links)
        int side = (int)(Math.random() * 4);
        switch (side) {
            case 0: // oben
                x = Math.random() * GamePanel.WIDTH;
                y = -height;
                break;
            case 1: // rechts
                x = GamePanel.WIDTH;
                y = Math.random() * GamePanel.HEIGHT;
                break;
            case 2: // unten
                x = Math.random() * GamePanel.WIDTH;
                y = GamePanel.HEIGHT;
                break;
            case 3: // links
                x = -width;
                y = Math.random() * GamePanel.HEIGHT;
                break;
        }

        // Start-Richtung wird gesetzt, aber später dynamisch angepasst
        dx = dy = 0;
    }

    public void update() {
        // Richtung zum Spieler berechnen
        double targetX = player.getX() + player.getWidth() / 2.0;
        double targetY = player.getY() + player.getHeight() / 2.0;

        double angleToPlayer = Math.atan2(targetY - (y + height / 2.0), targetX - (x + width / 2.0));
        dx = Math.cos(angleToPlayer) * speed;
        dy = Math.sin(angleToPlayer) * speed;

        // Bewegung
        x += dx;
        y += dy;

        // Optional: Kantenreflexion (falls gewünscht)
        if ((x <= 0 && dx < 0) || (x + width >= GamePanel.WIDTH && dx > 0)) {
            dx = -dx;
        }
        if ((y <= 0 && dy < 0) || (y + height >= GamePanel.HEIGHT && dy > 0)) {
            dy = -dy;
        }
    }

    public void draw(Graphics2D g) {
        double angle = Math.atan2(dy, dx);
        AffineTransform old = g.getTransform();

        g.translate(x + width / 2.0, y + height / 2.0);
        g.rotate(angle);

        g.setColor(color);
        g.fillRect(-width / 2, -height / 2, width, height);

        g.setTransform(old);
    }
}
