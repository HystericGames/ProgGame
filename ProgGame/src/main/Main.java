package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window  = new JFrame("Square Fighter: The Last Divison");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		GamePanel panel = new GamePanel();
		window.setContentPane(panel);
		window.pack();
		window.setResizable(false);
		window.setVisible(true);
		window.pack();
	} 

}
