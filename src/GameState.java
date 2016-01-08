import java.awt.Point;
import java.io.Serializable;
import java.util.Stack;

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	private Stack<Point> undoMoveStack;
	private Stack<Point> redoMoveStack;

	private Score score;
	private Board board;	
	
	public GameState (Board board, Score score) {
		this.undoMoveStack = new Stack<Point>();
		this.redoMoveStack = new Stack<Point>();
		
		this.score = score;
		this.board = board;
	}
	
	public void saveCurrentMove(int dx, int dy) {
		this.undoMoveStack.push(new Point(dx, dy));
		
		this.redoMoveStack.clear();
	}
	
	public void undoMove () {
		Point undoMove = this.undoMoveStack.pop();
		//Add current tilepositions to redo stack, if you want to redo your undoing :)
		this.redoMoveStack.push(undoMove);
		
		//Update current tiles to last added move in undoStack.
		this.board.setToAnimationState(-undoMove.x, -undoMove.y);
		
		//You have undone a move. Let score know last move didn't happen
		this.score.addMoves(-1);
	}
	
	public void redoMove () {
		Point redoMove = this.redoMoveStack.pop();
		//Add current tilepositions to redo stack, if you want to redo your undoing :)
		this.undoMoveStack.push(redoMove);
		
		//Update current tiles to last added move in undoStack.
		this.board.setToAnimationState(redoMove.x, redoMove.y);
		
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
	}

	
	////// GETTERS FROM HERE //////
	

	public Score getScore() {
		return this.score;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
}
