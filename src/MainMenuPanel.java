import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel implements ActionListener {
	
	JButton btnNewGame;
	JButton btnLoadGame;
	JButton btnHighscore;
	JButton btnSettings;
	JButton btnExit;
	
	public MainMenuPanel() {
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
		Window.swapView("puzzle");
	}
}
