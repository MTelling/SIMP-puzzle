import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Stack;

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Stack<Move> undoMoveStack;
	private Stack<Move> redoMoveStack;

	private Score score;
	private Board board;	
	private Settings settings;
	
	private boolean isGameDone;
	
	public GameState (Board board, Score score, Settings settings) {
		this.undoMoveStack = new Stack<Move>();
		this.redoMoveStack = new Stack<Move>();
		
		this.score = score;
		this.board = board;
		this.settings = settings;
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
		
		//You have undone a move. Let score know last move didn't happen
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
		this.setGameDone(false);
	}
	

	
	////// GETTERS FROM HERE //////
	

	public Score getScore() {
		return this.score;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Settings getSettings() {
		return this.settings;
	}

	public boolean isGameDone() {
		return this.isGameDone;
	}

	public void setGameDone(boolean isGameDone) {
		this.isGameDone = isGameDone;
	}
	
}
