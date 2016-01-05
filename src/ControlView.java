import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ControlView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel movesLabel;
	private JLabel timeLabel;
	private Score score;

	public ControlView(Score score) {
		
		this.score = score;
		
		//Create menuButton.
		JButton menuButton = new JButton("Menu");
		menuButton.setFocusable(false);

		
		//Create movesLabel and timeLabel. 
		this.movesLabel = new JLabel("Moves: " + this.score.getMoves()); 
		this.timeLabel = new JLabel("Time Spent: " + this.score.getSeconds()); 
		
		//Add labels and buttons to view. 
		this.add(menuButton);
		this.add(movesLabel);
		this.add(timeLabel);

		
		//Start timer. 
		this.startTiming();
	
	}
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
	private Timer timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
			updateSeconds();
		}
		
	});
	
	public void startTiming () {
		timer.start();
	}

	
	public void updateSeconds() {
		this.score.addSeconds(1);
		this.timeLabel.setText("Time Spent: " + score.getSeconds());
	}
	
	
	public void updateMoves() {
		score.addMoves(1);
		this.movesLabel.setText("Moves: " + score.getMoves());
	}
	
	public void update() {
		//TODO: Add connection to model. 
		this.movesLabel.setText("Score: "); //add score.getScore()
		this.timeLabel.setText("Time Spent: "); //add score.getSeconds()

	}
}
