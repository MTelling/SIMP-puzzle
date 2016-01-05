import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel movesLabel;
	private JLabel timeLabel;
	private Score score;

	public ControlView(Score score) {
		
		JButton menuButton = new JButton("Menu");
		menuButton.setFocusable(false);
		//TODO: add score.getScore() and score.getSeconds()
		this.movesLabel = new JLabel("Score: "); 
		this.timeLabel = new JLabel("Time Spent: "); 
		
		this.add(menuButton);
		this.add(movesLabel);
		this.add(timeLabel);
	}
	
	
	public void update() {
		//TODO: Add connection to model. 
		this.movesLabel.setText("Score: "); //add score.getScore()
		this.timeLabel.setText("Time Spent: "); //add score.getSeconds()
	}
}
