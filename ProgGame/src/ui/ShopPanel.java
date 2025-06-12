package ui;

import javax.swing.*;
import java.awt.*;
import main.GamePanel;
import player.Player;

public class ShopPanel extends JPanel {
    private static final long serialVersionUID = 3712616867311097737L;

    private Player player;
    private JLabel healthLabel;
    private JLabel maxHealthLabel;
    private JLabel speedLabel;
    private JLabel weaponLabel;
    private JLabel damageLabel;

    public ShopPanel(GamePanel game, Player player) {
        this.player = player;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 180));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Call to Command Center");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 20)));

        setupStatsPanel();
        updateStats();

        add(Box.createRigidArea(new Dimension(0, 20)));

        addButton("Reeinforce Division (2 Kills)", () -> {
            if (game.getKills() >= 2 && player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getHealth() + 1);
                game.increaseKillCount(-2);
                updateStats();
            }
        });

        addButton("Speed Upgrade (5 Kills)", () -> {
            if (game.getKills() >= 5 && player.getSpeed() < 10) {
                player.setSpeed(player.getSpeed() + 1);
                game.increaseKillCount(-5);
                updateStats();
            }
        });

        addButton("Division Upgrade (2 Medals)", () -> {
            if (game.getMedals() >= 2) {
                player.setMaxHealth(player.getMaxHealth() + 1);
                game.increaseMedalCount(-2);
                updateStats();
            }
        });
        
        addButton("Weapon Upgrade (5 Medals)", () -> {
            if (game.getMedals() >= 5 && player.getWeaponUpgradeLevel() < 5) {
                player.upgradeWeapon();
                game.increaseMedalCount(-5);
                updateStats();
            }
        });


        addButton("Start Next Wave", () -> {
            setVisible(false);
            game.startNextWave();
        });

        add(Box.createVerticalGlue());
    }

    private void setupStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(5, 1, 5, 5));
        statsPanel.setOpaque(false);

        healthLabel = createStatLabel();
        maxHealthLabel = createStatLabel();
        speedLabel = createStatLabel();
        weaponLabel = createStatLabel();
        damageLabel = createStatLabel();

        statsPanel.add(healthLabel);
        statsPanel.add(maxHealthLabel);
        statsPanel.add(speedLabel);
        statsPanel.add(weaponLabel);
        statsPanel.add(damageLabel);

        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(statsPanel);
    }

    private JLabel createStatLabel() {
        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    public void updateStats() {
        healthLabel.setText("Health: " + player.getHealth());
        maxHealthLabel.setText("Max Health: " + player.getMaxHealth());
        speedLabel.setText("Speed: " + player.getSpeed());
        weaponLabel.setText("Weapon: " + getWeaponName(player.getWeaponUpgradeLevel()));
        damageLabel.setText("Damage: " + player.getWeaponDamage());
    }

    private void addButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 40));
        button.addActionListener(e -> action.run());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(button);
    }
    
    private String getWeaponName(int level) {
        return switch (level) {
            case 1 -> "Winchester M1886";
            case 2 -> "Enfield M1917 Rifle";
            case 3 -> "RSC M1917";
            case 4 -> "Colt–Vickers M1915 MG";
            case 5 -> "BAR M1918";
            default -> "Unknown";
        };
    }

}
