package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
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
    public boolean running;

    private BufferedImage image;
    private Graphics2D g;

    protected Player player;
    public ArrayList<Enemy> enemies;
    protected ArrayList<Weapon> bullets;

    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    private int waveNumber = 0;

    private int score = 0;
    private int kills = 0;
    private int medals = 0;

    private boolean inShop = false;
    private ShopPanel shopPanel;
    
    private NetworkManager networkManager;
    private boolean isMultiplayer = false;
    private Player otherPlayer;
    
    private List<NetworkManager.ScoreUpdate> multiplayerScores = new ArrayList<>();

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null);
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
        running = true;

        KeyMouseHandler inputHandler = new KeyMouseHandler(this, player, bullets);
        addKeyListener(inputHandler);
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);

        shopPanel = new ShopPanel(this, player);
        shopPanel.setBounds(200, 100, 680, 500); 
        shopPanel.setVisible(false);
        add(shopPanel);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu multiplayerMenu = new JMenu("Multiplayer");
        
        JMenuItem hostGameItem = new JMenuItem("Host Game");
        hostGameItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter your name:");
            if (name != null && !name.trim().isEmpty()) {
                networkManager = new NetworkManager();
                if (networkManager.startHost(name.trim())) {
                    isMultiplayer = true;
                    multiplayerScores.add(new NetworkManager.ScoreUpdate(name.trim(), 0));
                }
            }
        });

        
        JMenuItem joinGameItem = new JMenuItem("Join Game");
        joinGameItem.addActionListener(e -> {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel("Enter host IP:"), BorderLayout.NORTH);
            JTextField ipField = new JTextField(15);
            panel.add(ipField, BorderLayout.CENTER);
            
            int result = JOptionPane.showConfirmDialog(this, panel, "Connect", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String name = JOptionPane.showInputDialog("Enter your name:");
                if (name != null && !name.trim().isEmpty()) {
                    JDialog loadingDialog = new JDialog();
                    loadingDialog.setTitle("Connecting...");
                    loadingDialog.add(new JLabel("Attempting to connect..."));
                    loadingDialog.setSize(200, 100);
                    loadingDialog.setLocationRelativeTo(this);
                    loadingDialog.setModal(false);
                    loadingDialog.setVisible(true);
                    
                    new Thread(() -> {
                        networkManager = new NetworkManager();
                        if (networkManager.connectToHost(ipField.getText().trim(), name.trim())) {
                            isMultiplayer = true;
                            multiplayerScores = networkManager.getCurrentScores();
                        }
                        SwingUtilities.invokeLater(loadingDialog::dispose);
                    }).start();
                }
            }
        });


        
        multiplayerMenu.add(hostGameItem);
        multiplayerMenu.add(joinGameItem);
        menuBar.add(multiplayerMenu);
        
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setJMenuBar(menuBar);
    }

    @Override
    public void run() {
    	DatabaseHelper.initializeDatabase();
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
        
        if (networkManager != null) {
            networkManager.close();
        }
    }

    private void saveHighScore() {
        if (score <= 0) return;
        
        SwingUtilities.invokeLater(() -> {
            String playerName;

            if (isMultiplayer && networkManager != null) {
                playerName = networkManager.playerName;
            } 
            else {
                playerName = JOptionPane.showInputDialog(
                    this, 
                    "Enter your name (max 15 chars):", 
                    "Game Over - Final Score: " + score, 
                    JOptionPane.PLAIN_MESSAGE
                );
                
                if (playerName == null) {
                    playerName = "Anonymous";
                } else {
                    playerName = playerName.trim();
                    if (playerName.length() > 15) {
                        playerName = playerName.substring(0, 15);
                    }
                    if (playerName.isEmpty()) {
                        playerName = "Anonymous";
                    }
                }
            }

            DatabaseHelper.addHighScore(playerName, score);
            repaint();
        });
    }

    private void drawHighScores(Graphics2D g) {
        List<DatabaseHelper.HighScore> highscores = DatabaseHelper.getHighScores(10);

        g.setColor(new Color(0, 0, 0, 180)); 
        g.fillRect(20, 100, 300, 30 + highscores.size() * 25);

        g.setColor(Color.WHITE);
        g.drawRect(20, 100, 300, 30 + highscores.size() * 25);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("High Scores:", 30, 125);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        int y = 150;
        for (int i = 0; i < highscores.size(); i++) {
            DatabaseHelper.HighScore hs = highscores.get(i);
            if (i == 0) {
                g.setColor(Color.YELLOW);
                g.drawString(String.format("#%d %s: %d", i+1, hs.playerName, hs.score), 30, y);
            } else {
                g.setColor(Color.WHITE);
                g.drawString(String.format("#%d %s: %d", i+1, hs.playerName, hs.score), 30, y);
            }
            y += 25;
        }
    }
    

    private void update() {
        if (!inShop) {
            player.update();
            
            if (isMultiplayer && networkManager != null) {
                try {
                    if (FPS % 60 == 0 && networkManager.socket != null 
                        && !networkManager.socket.isClosed()) {
                        networkManager.sendScore(score);
                    }
                    
                    if (networkManager.socket != null && !networkManager.socket.isClosed()) {
                        multiplayerScores = networkManager.getCurrentScores();
                    } else {
                        isMultiplayer = false;
                    }
                    FPS++;
                } catch (Exception e) {
                    isMultiplayer = false;
                }
            }

            
            for (Enemy enemy : enemies) enemy.update();

            if (enemies.isEmpty()) {
                inShop = true;
                shopPanel.setVisible(true); 
            }

            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).update()) {
                    bullets.remove(i);
                    i--;
                }
            }

            score += CollisionHandler.handleBulletEnemyCollisions(bullets, enemies, this);
            CollisionHandler.handlePlayerEnemyCollisions(player, enemies);
            CollisionHandler.handleEnemyCollisions(enemies);

            if (player.isDead()) {
            	running = false;
            	saveHighScore();
            }
        }
    }

    private void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            enemies.add(new EnemyHandler(player).createRandomEnemy());
        }
    }
    
    private void drawMultiplayerScores() {    
        // Title
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Multiplayer Scores:", WIDTH - 250, 35);
        
        // Scores
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        int y = 60;
        
        multiplayerScores.sort((a, b) -> Integer.compare(b.score, a.score));
        
        for (NetworkManager.ScoreUpdate update : multiplayerScores) {
            if (update.playerName.equals(networkManager != null ? networkManager.playerName : "")) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawString(update.playerName + ": " + update.score, WIDTH - 250, y);
            y += 25;
        }
    }

    private void render() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        if (isMultiplayer) {
            drawMultiplayerScores();
        }

        if (!player.isDead()) {
            player.draw(g);
            if (isMultiplayer && otherPlayer != null) {
                otherPlayer.draw(g);
            }
            for (Enemy enemy : enemies) {
                enemy.draw(g);
            }
            for (Weapon bullet : bullets) {
                bullet.draw(g);
            }

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            g.drawString("Player Stats:", 20, 35);

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Score: " + score, 20, 55);
            g.drawString("Kills: " + kills, 20, 75);
            g.drawString("Medals: " + medals, 20, 95);
        } else {
            drawGameOverScreen();
        }
    }

    private void drawGameOverScreen() {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "Game Over";
        int stringWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (WIDTH - stringWidth) / 2, HEIGHT / 2 - 60);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String scoreText = "Final Score: " + score;
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (WIDTH - scoreWidth) / 2, HEIGHT / 2 - 20);

        drawHighScores(g);
    }

    @Override
    protected void paintComponent(Graphics gPanel) {
        super.paintComponent(gPanel);
        gPanel.drawImage(image, 0, 0, null);
    }

    public boolean isInShop() {
        return inShop;
    }

    public void startNextWave() {
        waveNumber++;
        spawnEnemies(waveNumber * 2);
        inShop = false;
        shopPanel.setVisible(false);
    }

    public void changeKillCount(int value) {
        kills += value;
        if (kills < 0) kills = 0;
    }

    public void changeMedalCount(int value) {
        medals += value;
        if (medals < 0) medals = 0;
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

    public ShopPanel getShopPanel() {
        return shopPanel;
    }
}
