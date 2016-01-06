import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel implements ActionListener {
	
	GameState gameState;
	
	JButton btnNewGame;
	JButton btnLoadGame;
	JButton btnHighscore;
	JButton btnSettings;
	JButton btnExit;
	
	public MainMenuPanel(GameState gs) {
		this.gameState = gs;
		
		//Create "New Game" Button
		addButton(btnNewGame, "New Game");
		
		//Create "Load Game" Button
		addButton(btnLoadGame, "Load Game");
			
		//Create "Settings" Button
		addButton(btnHighscore, "Highscores");
		
		//Create "Settings" Button
		addButton(btnSettings, "Settings");
		
		//Create "Exit" Button
		addButton(btnExit, "Exit Game");
		this.add(Box.createVerticalGlue());
	}
	
	private void addButton(JButton button, String label) {
		button = new JButton(label) {
			{
				setSize(256, 48);
				setMaximumSize(getSize());
			}
		};
		button.addActionListener(this);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(256, 48));
		
		this.add(Box.createVerticalGlue());
		this.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New Game")) {
				Window.swapView("puzzle");
		} else if (e.getActionCommand().equals("Load Game")) {
				// LOAD THA GAME MUTHAFUCKAAA
			Object obj = SaveLoad.loadFromFile("SavedGame");
			if(obj instanceof GameState) {
				gameState = (GameState) obj;
				Window.swapView("puzzle");
			}
		} else if(e.getActionCommand().equals("Exit Game")) {
			System.exit(0);
		}
	}
}
