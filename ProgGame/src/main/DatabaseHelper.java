package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_NAME = "highscores.db";
    private static final String TABLE_NAME = "highscores";
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "player_name TEXT NOT NULL," +
                         "score INTEGER NOT NULL," +
                         "date TEXT DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
    }
    
    public static void addHighScore(String playerName, int score) {
        String sql = "INSERT INTO " + TABLE_NAME + "(player_name, score) VALUES(?,?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding high score: " + e.getMessage());
        }
    }
    
    public static List<HighScore> getHighScores(int limit) {
        List<HighScore> highscores = new ArrayList<>();
        String sql = "SELECT player_name, score, date FROM " + TABLE_NAME + 
                     " ORDER BY score DESC LIMIT ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                highscores.add(new HighScore(
                    rs.getString("player_name"),
                    rs.getInt("score"),
                    rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting high scores: " + e.getMessage());
        }
        
        return highscores;
    }
    
    public static class HighScore {
        public final String playerName;
        public final int score;
        public final String date;
        
        public HighScore(String playerName, int score, String date) {
            this.playerName = playerName;
            this.score = score;
            this.date = date;
        }
    }
}