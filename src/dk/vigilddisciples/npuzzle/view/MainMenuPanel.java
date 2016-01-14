package dk.vigilddisciples.npuzzle.view;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import dk.vigilddisciples.npuzzle.controller.SimpController;

public class MainMenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private SimpController controller;
	
	private JButton btnNewGame;
	private JButton btnLoadGame;
	private JButton btnHighscore;
	private JButton btnSettings;
	private JButton btnExit;
	
	public MainMenuPanel(SimpController controller) {	
		this.controller = controller;
		
		//Create Buttons
		addButton(btnNewGame, "New Game", "NewGame");
		addButton(btnLoadGame, "Load Game", "LoadGame");
		addButton(btnHighscore, "Highscore", "Highscore");
		addButton(btnSettings, "Settings", "Settings");
		addButton(btnExit, "Exit Game", "ExitGame");
		this.add(Box.createVerticalGlue());
	}
	
	private void addButton(JButton button, String label, String actionCommand) {
		button = new JButton(label) {
			private static final long serialVersionUID = 1L;

			{
				setSize(256, 48);
				setMaximumSize(getSize());
			}
		};
		button.addActionListener(this.controller);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(256, 48));
		button.setActionCommand("mainMenu" + actionCommand);
		
		this.add(Box.createVerticalGlue());
		this.add(button);
	}

}
