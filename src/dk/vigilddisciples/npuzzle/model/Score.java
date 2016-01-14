package dk.vigilddisciples.npuzzle.model;
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
	
	public int calculateScore(int boardSize) {
		int tempScore = 10000;
		int timePenalty = 0;
		int movePenalty = 300 / boardSize;
		
		tempScore -= this.moves * movePenalty;
		
		int freeTime = 20 * (boardSize - 2);
		if(this.seconds > freeTime) {
			timePenalty = 2;
			tempScore -= ((this.seconds - freeTime) % 60) * timePenalty;
		}
		
		for(int i = 1; i < (this.seconds - freeTime) / 60; i++) {
			tempScore -= timePenalty + i;
		}
		
		return tempScore;
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
