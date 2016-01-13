import java.io.Serializable;

public class Score implements Serializable{

	private static final long serialVersionUID = 1L;
	private int moves;
	private int newMoves;
	private int seconds;
	

	public Score () {
		reset();
	}
	
	public Score (int seconds, int moves)
	{
		this.seconds = seconds;
		this.moves = moves;
		this.newMoves = moves;
	}
	
	public int calculateScore() {
		if(this.seconds > Window.getSettings().getDifficulty() * Window.getSettings().getTilesPerRowInBoard()) {
			if(this.moves > (Window.getSettings().getTilesPerRowInBoard() - Window.getSettings().getDifficulty()) * 2) {
				return 10000 - (this.seconds * 2) - (this.moves * 10);
			} else {
				return 10000;
			}
		} else {
			return 10000;
		}
	}
	
	public int getSeconds () {
		return this.seconds;
	}
	
	public int getMoves () {
		return this.moves;
	}
	
	public int getNewMoves() {
		return this.newMoves;
	}
	
	public void setNewMoves(int newMoves) {
		this.newMoves = newMoves;
	}
	
	public void addSeconds (int seconds) {
		this.seconds += seconds;

	}
	
	public void addMoves (int moves) {
		this.moves += moves;
		this.newMoves += moves;
	}
	
	public void takeMoves (int moves) {
		this.moves -= moves;
		this.newMoves -= moves;
	}

	
	public void reset () {
		this.moves = 0;
		this.newMoves = 0;
		this.seconds = 0;
	}
	
	
	//Returns time as a string with format: (##:##). 
	public String timeToString() {
		int seconds = this.seconds;
		int minutes = 0;
		
		if (seconds > 59) {
			minutes = this.seconds / 60;
			seconds = this.seconds % 60;
		}
		
		return String.format("%02d:%02d", minutes, seconds);
		
	}
	
	
	
}
