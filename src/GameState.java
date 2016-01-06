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
		this.checkPoints.add(board.getTiles());
		this.score = score;
		this.emptyTiles = new ArrayList<Point>();
		this.emptyTiles.add(new Point(board.getEmptyTile().x, board.getEmptyTile().y));
	}
	
	public int[][] getCurrentBoard() {
		return goBack(0);
	}
	
	public boolean updateGameState(int[][] tiles, Point emptyTile) {
		
		this.emptyTiles.add(new Point(emptyTile.x, emptyTile.y));
		this.currPos++;
		
		//Arrays are objects, so we can't just add it. We need to add a copy of it
		int[][] newTile = new int[tiles.length][];
		for (int i = 0; i < newTile.length; i++) {
			newTile[i] = Arrays.copyOf(tiles[i], tiles[i].length);
		}
		this.checkPoints.add(newTile);

		return true;
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
