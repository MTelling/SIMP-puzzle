import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GameState implements Serializable {

	private ArrayList<int[][]> checkPoints;
	private ArrayList<Point> emptyTiles;
	private int currPos;
	private Score score;
	
	public GameState (Board board, Score score) {
		this.checkPoints = new ArrayList<int[][]>();
		this.currPos = 0;
		this.checkPoints.add(copyOf2DArray(board.getTiles()));
		this.score = score;
		this.emptyTiles = new ArrayList<Point>();
		this.emptyTiles.add(copyOfPoint(board.getEmptyTile()));
	}
	
	public int[][] getCurrentBoard() {
		return goBack(0);
	}
	
	public boolean updateGameState(int[][] tiles, Point emptyTile) {
		
		this.emptyTiles.add(copyOfPoint(emptyTile));
		this.currPos++;
		this.checkPoints.add(copyOf2DArray(tiles));

		return true;
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
	
	public int[][] goBack (int howLong) {
		if(currPos - howLong >= 0){
			int newPos = currPos;
			currPos -= howLong;
			this.score.takeMoves(1);
			return checkPoints.get(newPos - howLong);
		}else{
			throw new IllegalArgumentException("Can't go further back than beginning");
		}
	}
	
	public int[][] goForward (int howLong) {
		if(currPos + howLong < checkPoints.size()){
			int newPos = currPos;
			currPos += howLong;
			this.score.addMoves(1);
			return checkPoints.get(newPos + howLong);
		}else{
			throw new IllegalArgumentException("Can't go into the future!");
		}
	}
	
	// MUST BE USED BEFORE goForward(), as goForward is updating currPos!
	public Point goForwardEmpty(int howLong) {
		if(currPos + howLong < checkPoints.size()){
			return emptyTiles.get(currPos + howLong);
		}else{
			throw new IllegalArgumentException("Can't go into the future!");
		}
	}
	
	// MUST BE USED BEFORE goBack(), as goForward is updating currPos!
	public Point goBackEmpty(int howLong) {
		if(currPos - howLong < checkPoints.size()){
			return emptyTiles.get(currPos - howLong);
		}else{
			throw new IllegalArgumentException("Can't go future back than Big Bang!");
		}
	}
	
	public boolean canGoBack(){
		return currPos > 0;
	}
	
	public boolean canGoForward(){
		return currPos < checkPoints.size() - 1;
	}

}
