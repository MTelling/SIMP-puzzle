import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

public class GameState implements Serializable {

	private static final long 	serialVersionUID = 1L;
	private Stack<int[][]> 		undoMoveStack;
	private Stack<Point> 		undoEmptyTileStack;
	private Stack<int[][]> 		redoMoveStack;
	private Stack<Point> 		redoEmptyTileStack;

	private Score score;
	private Board board;
	
	private AnimationState animationState;
	
	
	public GameState (Board board, Score score, AnimationState animationState) {
		this.undoMoveStack 		= new Stack<int[][]>();
		this.undoEmptyTileStack = new Stack<Point>();
		this.redoMoveStack 		= new Stack<int[][]>();
		this.redoEmptyTileStack = new Stack<Point>();
		
		this.score = score;
		this.board = board;
		
		this.animationState = animationState;

	}
	
	public Score getScore() {
		return score;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public AnimationState getAnimationState() {
		return this.animationState;
	}
	
	public int[][] getCurrentBoard() {
		return undoMoveStack.peek();
	}
	
	//
	public void saveCurrentState() {
		this.undoMoveStack.add(copyOf2DArray(this.board.getTiles()));
		this.undoEmptyTileStack.add(copyOfPoint(this.board.getEmptyTile()));
		
		//TODO: Is it correct to clear redo stack here? 
		this.redoEmptyTileStack.clear();
		this.redoMoveStack.clear();
		
		//TODO: should this just get copies instead? 
	}
	
	public void undoMove () {
		
		//Add current tilepositions to redo stack, if you want to redo your undoing :)
		this.redoMoveStack.add(copyOf2DArray(this.board.getTiles()));
		this.redoEmptyTileStack.add(copyOfPoint(this.board.getEmptyTile()));
		
		//Update current tiles to last added move in undoStack.
		board.setTiles(undoMoveStack.pop());
		board.setEmptyTile(undoEmptyTileStack.pop());
		
		//You have undone a move. Let score know last move didn't happen
		score.addMoves(-1);
	}
	
	public void redoMove () {
		//Add current move to undo stack, if you wanna redo stack, if you wanna undo your redo.
		this.undoEmptyTileStack.add(copyOfPoint(this.board.getEmptyTile()));
		this.undoMoveStack.add(copyOf2DArray(this.board.getTiles()));
		
		//Update current tiles to last added move in redoStack.
		board.setTiles(redoMoveStack.pop());
		board.setEmptyTile(redoEmptyTileStack.pop());
		
		//You have redone a move. Let score know last move did happen after all.
		score.addMoves(1);
	}
	
	public boolean canUndo(){
		return (this.undoMoveStack.size() > 0 && this.score.getMoves() != 0)? true:false;
	}
	
	public boolean canRedo(){
		return this.redoMoveStack.size() > 0? true:false;
	}
	
	//Method to make a copy of a 2d array without just getting reference to the original array. 
	private int[][] copyOf2DArray(int[][] array) {
		int[][] newArray = new int[array.length][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = Arrays.copyOf(array[i], array[i].length);
		}
		return newArray;
	}
	
	//Method to make a copy of a point without just getting the reference. 
	private Point copyOfPoint(Point point) {
		return new Point(point.x, point.y);
	}
	
	public void restartGame() {
		this.board.init();
		this.score.reset();
		this.animationState.init(this.board.getEmptyTile(), this.board.getTiles());
	}

}
