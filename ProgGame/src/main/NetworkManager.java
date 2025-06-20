package main;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NetworkManager implements AutoCloseable {
    public static final int PORT = 55555;
    public Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public String playerName;
    private ServerSocket serverSocket;
    private List<ScoreUpdate> allScores = new ArrayList<>();
    
    public boolean startHost(String name) {
        try {
            serverSocket = new ServerSocket(PORT);
            this.playerName = name;
            allScores.add(new ScoreUpdate(name, 0));
            new Thread(this::handleConnections).start();
            return true;
        } catch (IOException e) {
            showError("Failed to host game");
            return false;
        }
    }

    private void handleConnections() {
        try {
            socket = serverSocket.accept();
            setupStreams();

            out.writeObject(allScores);
            out.reset();

            new Thread(this::receiveUpdates).start();
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean connectToHost(String hostAddress, String name) {
        try {
            // Create socket with timeout
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostAddress, PORT), 3000); // 3 second timeout
            
            this.playerName = name;
            setupStreams();

            Object received = in.readObject();
            if (received instanceof List<?>) {
                allScores = (List<ScoreUpdate>) received;
                allScores.add(new ScoreUpdate(name, 0));
            }
            new Thread(this::receiveUpdates).start();
            return true;
        } catch (SocketTimeoutException e) {
            showError("Connection timed out - host not found");
            return false;
        } catch (Exception e) {
            showError("Failed to connect: " + e.getMessage());
            return false;
        }
    }
    
    private void setupStreams() throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    public void sendScore(int score) {
        try {
            if (out != null && socket != null && !socket.isClosed()) { 
                allScores.removeIf(s -> s.playerName.equals(playerName));
                allScores.add(new ScoreUpdate(playerName, score));
                
                out.writeObject(allScores);
                out.reset();
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {  
                System.err.println("Error sending score: " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void receiveUpdates() {
        try {
            while (socket != null && socket.isConnected() && !socket.isClosed()) {
                try {
                    Object received = in.readObject();
                    if (received instanceof List<?>) {
                        this.allScores = (List<ScoreUpdate>) received;
                    }
                } catch (SocketException e) {
                    break;
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {
            if (socket != null && !socket.isClosed()) {
                System.err.println("Error receiving updates: " + e.getMessage());
            }
        } finally {
            close(); 
        }
    }
    
    public List<ScoreUpdate> getCurrentScores() {
        return new ArrayList<>(allScores);
    }
    
    @Override
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) out.close();
            if (in != null) in.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(null, message, "Network Error", JOptionPane.ERROR_MESSAGE));
    }
    
    public static class ScoreUpdate implements Serializable {
        private static final long serialVersionUID = 2952512144715252943L;
		public final String playerName;
        public final int score;
        
        public ScoreUpdate(String name, int score) {
            this.playerName = name;
            this.score = score;
        }
    }
}