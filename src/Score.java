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
		return this.seconds;
	}
	
	public int getMoves () {
		return this.moves;
	}
	
	public void addSeconds (int seconds) {
		this.seconds += seconds;

	}
	
	public void addMoves (int moves) {
		this.moves += moves;
	}
	
	public void takeMoves (int moves) {
		this.moves -= moves;
	}

	
	public void reset () {
		this.moves = 0;
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
