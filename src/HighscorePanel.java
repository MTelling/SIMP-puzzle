import javax.swing.JPanel;

public class HighscorePanel extends JPanel{
	
	public HighscorePanel(Highscore highscore) {
		this.setPreferredSize(Window.getSettings().getCurrWindowSize().getDimension());
		
		
	}
}
