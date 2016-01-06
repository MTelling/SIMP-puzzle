import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel implements ActionListener {

	JButton btnContinueGame;
	JButton btnNewGame;
	JButton btnSaveGame;
	JButton btnHighScore;
	JButton btnSettings;
	JButton btnQuitGame;
	
	public MenuPanel() {
		addButton(btnContinueGame, "Continue Game");
		addButton(btnNewGame, "New Game");
		addButton(btnSaveGame, "Save Game");
		addButton(btnHighScore, "Highscores");
		addButton(btnContinueGame, "Settings");
		addButton(btnSettings, "Exit to Main Menu");
		this.add(Box.createVerticalGlue());
		
		this.setBounds(Window.GAME_BORDER, Window.GAME_BORDER, Window.WINDOW_WIDTH - 2 * Window.GAME_BORDER, Window.WINDOW_HEIGHT  - 2 * Window.GAME_BORDER);
		this.setOpaque(false);
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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(128, 128, 128, 225));
		g.fillRect(0, 0, 448 - Window.GAME_BORDER * 2, 512 - Window.GAME_BORDER * 2);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
}
