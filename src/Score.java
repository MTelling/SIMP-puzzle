
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Score {

	private int moves;
	private int seconds;
	
	private Timer timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Update the timer. +1 second
			addSeconds(1);
			
		}
		
	});
	



	public Score () {
		moves = 0;
		seconds = 0;
	}
	
	public Score (int seconds, int moves)
	{
		this.seconds = seconds;
		this.moves = moves;
	}
	
	public int getSeconds () {
		return seconds;
	}
	
	public int getMoves () {
		return moves;
	}
	
	private void addSeconds (int howMuch) {
		seconds += howMuch;
	}
	
	public void addMoves (int howMany) {
		moves += howMany;
	}
	
	public void startTiming () {
		timer.start();
	}
	
	public void stopTiming () {
		timer.stop();
	}
	
	public void reset () {
		moves = 0;
		seconds = 0;
	}
	
	
	
	
}
