package ui;

import javax.swing.*;
import java.awt.*;
import main.GamePanel;
import player.Player;

public class ShopPanel extends JPanel {
    private static final long serialVersionUID = 3712616867311097737L;

    private GamePanel game;
    private Player player;

    private JLabel healthLabel;
    private JLabel maxHealthLabel;
    private JLabel speedLabel;
    private JLabel killsLabel;
    private JLabel medalsLabel;

    public ShopPanel(GamePanel game, Player player) {
        this.game = game;
        this.player = player;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 180));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("SHOP");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 20)));

        setupStatsPanel();
        updateStats();

        add(Box.createRigidArea(new Dimension(0, 20)));

        addButton("+1 Health (1 Kill)", () -> {
            if (game.getKills() >= 1 && player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getHealth() + 1);
                game.increaseKillCount(-1);
                updateStats();
            }
        });

        addButton("+1 Speed (2 Kills)", () -> {
            if (game.getKills() >= 2 && player.getSpeed() < 10) {
                player.setSpeed(player.getSpeed() + 1);
                game.increaseKillCount(-2);
                updateStats();
            }
        });

        addButton("+1 MaxHealth (3 Medals)", () -> {
            if (game.getMedals() >= 3) {
                player.setMaxHealth(player.getMaxHealth() + 1);
                game.increaseMedalCount(-3);
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
        statsPanel.setOpaque(false); // transparent background

        healthLabel = createStatLabel();
        maxHealthLabel = createStatLabel();
        speedLabel = createStatLabel();
        killsLabel = createStatLabel();
        medalsLabel = createStatLabel();

        statsPanel.add(healthLabel);
        statsPanel.add(maxHealthLabel);
        statsPanel.add(speedLabel);
        statsPanel.add(killsLabel);
        statsPanel.add(medalsLabel);

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
        killsLabel.setText("Kills: " + game.getKills());
        medalsLabel.setText("Medals: " + game.getMedals());
    }

    private void addButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 40));
        button.addActionListener(e -> action.run());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(button);
    }
}
