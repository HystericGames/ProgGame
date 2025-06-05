package gameloop;

import enemy.Enemy;
import player.Player;
import player.Weapon;

import java.awt.Rectangle;
import java.util.List;

public class CollisionHandler {

    public static void handleEnemyCollisions(List<Enemy> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e1 = enemies.get(i);
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy e2 = enemies.get(j);
                if (e1.getBounds().intersects(e2.getBounds())) {
                    double angle = Math.atan2(e2.getY() - e1.getY(), e2.getX() - e1.getX());
                    double push = 3;

                    e1.setX(e1.getX() - Math.cos(angle) * push);
                    e1.setY(e1.getY() - Math.sin(angle) * push);
                    e2.setX(e2.getX() + Math.cos(angle) * push);
                    e2.setY(e2.getY() + Math.sin(angle) * push);
                }
            }
        }
    }

    public static int handleBulletEnemyCollisions(List<Weapon> bullets, List<Enemy> enemies) {
        int scoreGained = 0;

        for (int i = 0; i < bullets.size(); i++) {
            Weapon bullet = bullets.get(i);
            boolean removeBullet = bullet.update();
            Rectangle bulletRect = bullet.getBounds();

            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                if (bulletRect.intersects(e.getBounds())) {
                    e.setHealth(e.getHealth() - 1);

                    double angle = bullet.getAngle();
                    e.setX(e.getX() + Math.cos(angle) * e.getKnockback());
                    e.setY(e.getY() + Math.sin(angle) * e.getKnockback());

                    removeBullet = true;

                    if (e.getHealth() <= 0) {
                        e.setDead(true);
                        enemies.remove(j);
                        if (e.getType() == 1) scoreGained += 1;
                        else if (e.getType() == 2) scoreGained += 2;
                        else scoreGained +=3;
                        j--;
                    }

                    break;
                }
            }

            if (removeBullet) {
                bullets.remove(i);
                i--;
            }
        }

        return scoreGained;
    }

    public static void handlePlayerEnemyCollisions(Player player, List<Enemy> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (player.getBounds().intersects(e.getBounds())) {
                player.damage(e.getDamage());
                enemies.remove(i);
                i--;

                if (player.isDead()) {
                    break;
                }
            }
        }
    }
}

