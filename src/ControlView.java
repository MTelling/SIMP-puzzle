import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class ControlView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel movesLabel;
	private JLabel timeLabel;
	private Score score;

	public ControlView(Score score) {
		
		this.score = score;
		
		//Set layout for Jpanel container
		this.setLayout(new BorderLayout(30, 0));
		this.setBorder(new EmptyBorder(SimpWindow.GAME_BORDER, SimpWindow.GAME_BORDER, 0, SimpWindow.GAME_BORDER));
		
		//Create menuButton.
		JButton menuButton = new JButton("Menu");
		menuButton.setFocusable(false);

		
		//Create movesLabel and timeLabel. 
		this.movesLabel = new JLabel("Moves: " + this.score.getMoves()); 
		this.timeLabel = new JLabel("Time Spent: " + this.score.timeToString()); 
		
		//Add labels and buttons to view. 
		this.add("East", menuButton);
		this.add("West", movesLabel);
		this.add("Center", timeLabel);

	
	}
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
	private Timer timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
			updateTimeLabel();
		}
		
	});
	
	public void startTiming () {
		timer.start();
	}
	
	public void stopTiming() {
		timer.stop();
	}

	//Checks if it's first move and start timer if it is. 
	public void startTimerIfFirstMove() {
		if (this.score.getMoves() == 0) {
			this.startTiming();
		}
	}
	
	//Updates the timeLabel
	public void updateTimeLabel() {
		this.score.addSeconds(1);
		this.timeLabel.setText("Time Spent: " + score.timeToString());
	}
	
	
	public void updateMovesLabel() {
		score.addMoves(1);
		this.movesLabel.setText("Moves: " + score.getMoves());
	}
	
	

}
