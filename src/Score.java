
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Score {

	private int moves;
	private int seconds;
	



	public Score () {
		reset();
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
	
	public void addSeconds (int howMuch) {
		seconds += howMuch;
	}
	
	public void addMoves (int howMany) {
		moves += howMany;
	}
	
	
	public void reset () {
		moves = 0;
		seconds = 0;
	}
	
	
	
	
}
