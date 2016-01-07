import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

public class GameState implements Serializable {

	private Stack<int[][]> undoMoveStack;
	private Stack<Point> undoEmptyTileStack;
	private Stack<int[][]> redoMoveStack;
	private Stack<Point> redoEmptyTileStack;
	private Score score;
	private Board board;
	
	//TODO: Bugs if you go all the way to start two times. 
	public GameState (Board board, Score score) {
		this.undoMoveStack = new Stack<int[][]>();
		this.undoEmptyTileStack = new Stack<Point>();
		this.redoMoveStack = new Stack<int[][]>();
		this.redoEmptyTileStack = new Stack<Point>();
		
		this.score = score;
		this.board = board;
				
		
		//TODO: Some redundancy could be removed by making helper function "addMoveToState". As this is the same in updateGameState. 
		//Remember the first position. 
		this.updateMoveStacks(this.board.getTiles(), this.board.getEmptyTile());
				
	}
	
	public int[][] getCurrentBoard() {
		return undoMoveStack.peek();
	}
	
	//
	private boolean updateMoveStacks(int[][] tiles, Point emptyTile) {
		System.out.println("Updating game state");
		this.undoMoveStack.add(copyOf2DArray(tiles));
		this.undoEmptyTileStack.add(copyOfPoint(emptyTile));
		
		//TODO: Is it correct to clear redo stack here? 
		this.redoEmptyTileStack.clear();
		this.redoMoveStack.clear();
		
		return true;
	}
	//TODO: comment in this.
	public void undoMove () {
		
		this.redoMoveStack.add(copyOf2DArray(this.board.getTiles()));
		this.redoEmptyTileStack.add(copyOfPoint(this.board.getEmptyTile()));
		
		board.setTiles(undoMoveStack.pop());
		board.setEmptyTile(undoEmptyTileStack.pop());
		
		score.addMoves(-1);
		}
	
	public void redoMove () {
		this.undoEmptyTileStack.add(copyOfPoint(this.board.getEmptyTile()));
		this.undoMoveStack.add(copyOf2DArray(this.board.getTiles()));
		
		board.setTiles(redoMoveStack.pop());
		board.setEmptyTile(redoEmptyTileStack.pop());
		
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

	public boolean moveMade(int keyCode) {
		//Before making a move, check if a move should be made. 
		//If it should be made saveGameState to the current board and then make the move.  
		if (this.board.shouldMove(keyCode)) {
			this.updateMoveStacks(board.getTiles(), board.getEmptyTile());
			return true;
		} else {
			return false;
		}
	}

}
