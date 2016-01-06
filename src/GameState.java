import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GameState implements Serializable {

	private ArrayList<int[][]> checkPoints;
	private int currPos;
	private Score score;
	
	public GameState (Board board, Score score) {
		this.checkPoints = new ArrayList<int[][]>();
		this.currPos = 0;
		this.checkPoints.add(board.getTiles());
		this.score = score;
	}
	
	public int[][] getCurrentBoard() {
		return goBack(0);
	}
	
	public void saveGameState() {
		SaveLoad.saveToFile(this, "SavedGame");
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
			int newPos = currPos;
			currPos -= howLong;
			this.score.takeMoves(1);
			return checkPoints.get(newPos - howLong);
		}
		else
		{
			throw new IllegalArgumentException("Can't go further back than beginning");
		}
	}
	
	public int[][] goForward (int howLong) {
		if(currPos + howLong < checkPoints.size())
		{
			int newPos = currPos;
			currPos += howLong;
			this.score.addMoves(1);
			return checkPoints.get(newPos + howLong);
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
