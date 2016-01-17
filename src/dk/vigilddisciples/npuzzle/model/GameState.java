package dk.vigilddisciples.npuzzle.model;
import java.io.Serializable;
import java.util.Stack;

import dk.vigilddisciples.npuzzle.NPuzzle;

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Stack<Move> undoMoveStack;
	private Stack<Move> redoMoveStack;

	private Score score;
	private Board board;
	
	//GameState dependant settings
	private boolean useCornerLabels;
	private int gameDifficulty;
	
	private boolean isGameOver;
	
	public GameState (Board board, Score score) {
		this.undoMoveStack = new Stack<Move>();
		this.redoMoveStack = new Stack<Move>();
		
		this.score = score;
		this.board = board;
	}
	
	public void saveCurrentMove(Move move) {
		this.undoMoveStack.push(move);
		this.redoMoveStack.clear();
	}
	
	public void undoMove () {
		Move undoMove = this.undoMoveStack.pop();
		//Add current tilepositions to redo stack, if you want to redo your undoing :)
		this.redoMoveStack.push(undoMove);
		
		//Update current tiles to last added move in undoStack.
		this.board.setToAnimationState(undoMove.reverse());
		
		//You have undone a move. Let score know last move didn't happen
		this.score.addMoves(-1);
	}
	
	public void redoMove () {
		Move redoMove = this.redoMoveStack.pop();
		//Add current tilepositions to redo stack, if you want to redo your undoing :)
		this.undoMoveStack.push(redoMove);
		
		//Update current tiles to last added move in undoStack.
		this.board.setToAnimationState(redoMove);
		
		//You have redone a move. Let score know last move did actually happen afterall
		this.score.addMoves(1);
	}
	
	public boolean canUndo(){
		return (this.undoMoveStack.size() > 0 && this.score.getMoves() != 0) ? true : false;
	}
	
	public boolean canRedo(){
		return this.redoMoveStack.size() > 0 ? true : false;
	}
		
	public void restartGame() {
		this.board.reset();
		this.score.reset();
		
		this.undoMoveStack.clear();
		
		this.useCornerLabels = NPuzzle.getSettings().isLabelsOn();
		this.gameDifficulty = NPuzzle.getSettings().getDifficulty();
		
		this.setGameOver(false);
	}
	
	/// GETTERS FROM HERE ///
	
	public Score getScore() {
		return this.score;
	}
	
	public Board getBoard() {
		return this.board;
	}

	public boolean isGameOver() {
		return this.isGameOver;
	}
	
	public boolean isLabelsOn() {
		return this.useCornerLabels;
	}
	
	public int getDifficulty() {
		return this.gameDifficulty;
	}

	public void setGameOver(boolean isGameDone) {
		this.isGameOver = isGameDone;
	}
}