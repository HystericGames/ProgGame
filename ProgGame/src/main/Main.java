package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window  = new JFrame("Shooter Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setContentPane(new GamePanel());
		window.pack();
		window.setResizable(false);
		window.setVisible(true);
		window.pack();
	}

}
