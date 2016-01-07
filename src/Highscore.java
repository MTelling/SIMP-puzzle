import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Highscore implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int HIGHSCORE_SIZE = 5;
	private int boardSize;
	private String[] highScoreNames;
	private int[] highScores;
	
	public Highscore(int boardSize) {
		this.boardSize = boardSize;
		
		this.highScoreNames = new String[HIGHSCORE_SIZE];
		this.highScores = new int[HIGHSCORE_SIZE];
	}
	
	public boolean isHighscore(int score) {
		
		return false;
	}
	
	public Map<String, Integer> getHighscores() {
		Map<String, Integer> temp = new HashMap<String, Integer>();
		for(int i = 0; i < HIGHSCORE_SIZE; i++) {
			temp.put(highScoreNames[i], highScores[i]);
		}
		
		return temp;
	}
	
}
