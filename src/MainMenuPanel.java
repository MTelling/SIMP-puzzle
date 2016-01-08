import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel implements ActionListener {
	

	private static final long serialVersionUID = 1L;

	private GameState gameState;
	
	private JButton btnNewGame;
	private JButton btnLoadGame;
	private JButton btnHighscore;
	private JButton btnSettings;
	private JButton btnExit;
	
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

			private static final long serialVersionUID = 1L;

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

	public void updateGameState(GameState gs) {
		this.gameState = gs;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New Game")) {
			Window.swapView("puzzle");
		} else if (e.getActionCommand().equals("Load Game")) {
			//Load the game from file
			Object obj = SaveLoad.loadFromFile("SavedGame");
			if(obj instanceof GameState) {
				//gameState = (GameState) obj;
				Window.loadGame( (GameState) obj);
				gameState.getScore().setNewMoves(0);
				Window.swapView("puzzle");
			}
		} else if(e.getActionCommand().equals("Exit Game")) {
			System.exit(0);
		}
	}

}
