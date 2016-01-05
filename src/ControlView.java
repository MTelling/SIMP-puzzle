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
		
		JButton menuButton = new JButton("Menu");
		menuButton.setFocusable(false);
		
		this.movesLabel = new JLabel("Moves: " + this.score.getMoves()); 
		this.timeLabel = new JLabel("Time Spent: " + this.score.getSeconds()); 
		
		this.add(menuButton);
		this.add(movesLabel);
		this.add(timeLabel);
		
		this.startTiming();
		
		
	}
	
	private Timer timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Update the timer. +1 second
			updateSeconds();
		}
		
	});
	
	public void startTiming () {
		timer.start();
	}
	
	public void stopTiming () {
		timer.stop();
	}
	
	public void updateSeconds() {
		this.score.addSeconds(1);
		this.timeLabel.setText("Time Spent: " + score.getSeconds());
	}
	
	public void updateMoves() {
		score.addMoves(1);
		this.movesLabel.setText("Moves: " + this.score.getMoves());
		
	}
}
