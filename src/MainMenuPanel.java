import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel implements ActionListener {
	
	JButton btnNewGame;
	JButton btnLoadGame;
	JButton btnSettings;
	JButton btnExit;
	
	public MainMenuPanel() {
		//Create "New Game" Button
		btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(this);
		
		//Create "Load Game" Button
		btnLoadGame = new JButton("Load Game");
		btnLoadGame.addActionListener(this);
				
		//Create "Settings" Button
		btnSettings = new JButton("Settings");
		btnSettings.addActionListener(this);
		
		//Create "Exit" Button
		btnExit = new JButton("Exit");
		btnExit.addActionListener(this);
		
		//Add the buttons to the panel
		this.add(btnNewGame);
		this.add(btnLoadGame);
		this.add(btnSettings);
		this.add(btnExit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Window.swapView("puzzle");
	}
}
