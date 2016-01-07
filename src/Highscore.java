import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Highscore implements Serializable {
	private static final long 	serialVersionUID = 1L;
	
	private static final int 	HIGHSCORE_SIZE = 5;
	
	private static final String FILE_NAME = "resources/Highscores";
	
	private static final String STD_NAME = "Robert";
		
	private int 		currentBoardSize;
	private String[] 	highScoreNames;
	private int[] 		scores;
	
	private Map<Integer, HashMap<String, Integer>> highScores = new HashMap<Integer, HashMap<String, Integer>>();
	
	
	public Highscore(int currentBoardSize) {
		
		Object obj = SaveLoad.loadFromFile(FILE_NAME);
		
	    scores 			= new int[HIGHSCORE_SIZE];
	    highScoreNames 	= new String[HIGHSCORE_SIZE];
	    
	    
		if(obj instanceof Highscore) {
			// Get the previous Highscores if it exists
			Highscore temp = (Highscore) obj;
			this.highScores = temp.getHighscoreMap();
			
			if (this.highScores.get(currentBoardSize) != null) {
			    HashMap<String, Integer> tempScores = this.highScores.get(currentBoardSize);
			    
			    
			    int tempCounter = 0; //TODO: I know this is fugly. Can anyone make it prettier?
			    for (Map.Entry<String, Integer> entry : tempScores.entrySet()) {
			    	
			    	scores[tempCounter] 		= entry.getValue();
			    	highScoreNames[tempCounter] = entry.getKey();
			    	tempCounter++;
			    	
			    	 // Just a security measure in case you load in an old Highscorefile, with too many entries
			    	if(tempCounter == HIGHSCORE_SIZE) { break; }
			    }
			    
			    // Add possible missing scores.
			    if(tempCounter < HIGHSCORE_SIZE) {
			    	addMissingScores(tempCounter);
			    }
			    
			} else {
			    // No such key; highscores for this board is nonexistent.
				
			    addMissingScores(0);
				
			}
			
		} else {
			// No previous Highscores found; Create new list.
			
			addMissingScores(0);
			
			for(int i = 0; i < HIGHSCORE_SIZE; i++) {
				HashMap<String, Integer> temp = new HashMap<String, Integer>();
				temp.put(highScoreNames[i], scores[i]);
				highScores.put(currentBoardSize , temp);
			}
			
		}
		
		this.currentBoardSize = currentBoardSize;
	}
	
	public void addMissingScores(int fromWhere) {
		for(int i = fromWhere; i < HIGHSCORE_SIZE; i++) {
			this.scores[i] = 0;
			this.highScoreNames[i] = STD_NAME;
		}
	}
	
	public HashMap<String, Integer> getHighscores() {
		return highScores.get(currentBoardSize);
	}
	
	public Map<Integer, HashMap<String, Integer>> getHighscoreMap() {
		return highScores;
	}
	
	public boolean isHighscore(int score) {
		
		
		for(int i = 0; i < HIGHSCORE_SIZE; i++) {
			if (score > scores[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addHighScore (String name, int score) {
		int scoreSpot = HIGHSCORE_SIZE - 1;
		
		for(int i = scoreSpot; i >= 0 ; i++) {
			if(score > scores[i]) {
				scoreSpot = i;
			}
		}
		
		scores[scoreSpot] = score;
		this.highScoreNames[scoreSpot] = name;
		
	}
	
	public void updateHighScores () {
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for(int i = 0; i < HIGHSCORE_SIZE; i++) {
			temp.put(highScoreNames[i], scores[i]);
		}
		highScores.put(currentBoardSize , temp);
	}
	

	
}
