import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {

	private ArrayList<int[][]> checkPoints;
	private int currPos;
	
	public GameState (Board board, Score score) {
		currPos = 0;
		checkPoints.add(board.getTiles());
	}
	
	public int[][] goBack (int howLong) {
		if(currPos - howLong >= 0)
		{
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
