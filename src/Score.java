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

	
	public void reset () {
		this.moves = 0;
		this.seconds = 0;
	}
	
	
	
	
}
