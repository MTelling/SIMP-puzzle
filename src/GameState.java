import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GameState implements Serializable {

	private ArrayList<int[][]> checkPoints;
	private int currPos;
	
	public GameState (Board board, Score score) {
		this.checkPoints = new ArrayList<int[][]>();
		this.currPos = 0;
		this.checkPoints.add(board.getTiles());
	}
	
	public int[][] getCurrentBoard() {
		return goBack(0);
	}
	
	public boolean updateGameState(int[][] tiles) {
		
		
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
		if(currPos - howLong >= 0)
		{
			currPos -= howLong;
			return checkPoints.get(currPos - howLong);
		}
		else
		{
			throw new IllegalArgumentException("Can't go further back than beginning");
		}
	}
	
	public int[][] goForward (int howLong) {
		if(currPos + howLong < checkPoints.size())
		{
			currPos -= howLong;
			return checkPoints.get(currPos + howLong);
		}
		else
		{
			throw new IllegalArgumentException("Can't go into the future!");
		}
	}
	
	public boolean canGoBack(){
		return currPos > 0;
	}
	
	public boolean canGoForward(){
		return currPos < checkPoints.size() - 1;
	}

}
