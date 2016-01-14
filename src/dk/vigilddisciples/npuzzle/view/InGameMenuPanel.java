package dk.vigilddisciples.npuzzle.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import dk.vigilddisciples.npuzzle.NPuzzle;
import dk.vigilddisciples.npuzzle.controller.SimpController;
import dk.vigilddisciples.npuzzle.model.WindowSize;

public class InGameMenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	SimpController controller;
	
	private JButton btnContinueGame;
	private JButton btnNewGame;
	private JButton btnSaveGame;
	private JButton btnHighScore;
	private JButton btnSolver;
	private JButton btnQuitGame;
	
	public InGameMenuPanel(SimpController controller) {
		this.controller = controller;
		
		//Create Buttons
		this.addButton(btnContinueGame, "Continue Game", "ContinueGame");
		this.addButton(btnNewGame, "New Game", "NewGame");
		this.addButton(btnSaveGame, "Save Game", "SaveGame");
		this.addButton(btnSolver, "Solve Game", "SolveGame");
		this.addButton(btnHighScore, "Highscores", "Highscores");
		this.addButton(btnQuitGame, "Exit to Main Menu", "ExitToMainMenu");
		this.add(Box.createVerticalGlue());
		
		//Set boundaries for the ingame menu
		this.resetBounds();
		this.setOpaque(false);
	}
	
	public void resetBounds() {
		WindowSize currWindowSize = NPuzzle.getSettings().getCurrWindowSize();
		this.setBounds(currWindowSize.getGAME_BORDER(), 
				currWindowSize.getGAME_BORDER(), 
				currWindowSize.getWINDOW_WIDTH() - 2 * currWindowSize.getGAME_BORDER(), 
				currWindowSize.getWINDOW_HEIGHT()  - 2 * currWindowSize.getGAME_BORDER());
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
		button.setFocusable(false);
		button.setActionCommand("inGame" + actionCommand);
		
		this.add(Box.createVerticalGlue());
		this.add(button);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(128, 128, 128, 225));
		WindowSize currWindowSize = NPuzzle.getSettings().getCurrWindowSize();
		g.fillRect(0, 0, currWindowSize.getWINDOW_WIDTH() - currWindowSize.getGAME_BORDER() * 2, currWindowSize.getWINDOW_HEIGHT() - currWindowSize.getGAME_BORDER() * 2);
	}	
}
