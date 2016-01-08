import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Highscore implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final int HIGHSCORE_SIZE = 5;
	private static final String FILE_NAME = "Highscores";
	private static final String STD_NAME = "Robert";
	private static final int STD_SCORE = 0;
		
	private int currentBoardSize;
	private String[] highscoreNames;
	private int[] scores;
	
	private Map<Integer, HashMap<String, Integer>> highscores = new HashMap<Integer, HashMap<String, Integer>>();
	
	
	public Highscore(int currentBoardSize) {
		
		//Try to load old Highscorefile
		Object obj = SaveLoad.loadFromFile(FILE_NAME);
		
	    scores = new int[HIGHSCORE_SIZE];
	    highscoreNames = new String[HIGHSCORE_SIZE];
	    
	    //If obj is not an instance of Highscore, it means the file doesn't exist (obj is null).
		if(obj instanceof Highscore) {
			
			Highscore temp = (Highscore) obj;
			this.highscores = temp.getAllHighscores();
			
			//Check if old Highscorefile contains highscores for the current board size
			if (this.highscores.get(currentBoardSize) != null) {
				// Get the previous Highscores if it exists
			    HashMap<String, Integer> tempScores = this.highscores.get(currentBoardSize);
			    
			    int tempCounter = 0; 
			    for (Map.Entry<String, Integer> entry : tempScores.entrySet()) {
			    	
			    	scores[tempCounter] = entry.getValue();
			    	highscoreNames[tempCounter] = entry.getKey();
			    	tempCounter++;
			    	
			    	 // Just a security measure in case you load in an old Highscorefile, with too many entries
			    	if(tempCounter == HIGHSCORE_SIZE)
			    		break;
			    }
			    
			    // Add possible missing scores.
			    if(tempCounter < HIGHSCORE_SIZE) {
			    	addMissingScores(tempCounter);
			    }
			} else {
			    // No such key; highscores for this board is nonexistent.
				// Add placeholder highscores.
			    addMissingScores(0);
				
			}
		} else {
			// No previous Highscores found; Create new list.
			addMissingScores(0);
			
			for(int i = 0; i < HIGHSCORE_SIZE; i++) {
				HashMap<String, Integer> temp = new HashMap<String, Integer>();
				temp.put(highscoreNames[i], scores[i]);
				highscores.put(currentBoardSize , temp);
			}
			
		}
		//Let the object know what current board size is. This is used to get the correct highscores.
		this.currentBoardSize = currentBoardSize;
	}
	
	//Helper method to add placeholder scores.
	public void addMissingScores(int fromWhere) {
		for(int i = fromWhere; i < HIGHSCORE_SIZE; i++) {
			this.scores[i] = STD_SCORE;
			this.highscoreNames[i] = STD_NAME;
		}
	}
	
	//Get hashmap with highscores for current boardsize.
	public HashMap<String, Integer> getHighscores() {
		return highscores.get(currentBoardSize);
	}
	
	//Get entire hashmap of all highscores for all boardsizes.
	public Map<Integer, HashMap<String, Integer>> getAllHighscores() {
		return highscores;
	}
	
	//Check if a score is higher than a current highscore within current board.
	public boolean isHighscore(int score) {
		for(int i = 0; i < HIGHSCORE_SIZE; i++) {
			if (score > scores[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	//Add highscore. 
	//Must be used after isHighScore, as it always adds any score to last place, if it can't put it higher.
	public void addHighScore (String name, int score) {
		int scoreSpot = HIGHSCORE_SIZE - 1;
		
		for(int i = scoreSpot; i >= 0 ; i++) {
			if(score > scores[i]) {
				scoreSpot = i;
			}
		}
		
		this.scores[scoreSpot] 			= score;
		this.highscoreNames[scoreSpot] 	= name;
	}
	
	//Transfer current scores to the hashmap of all scores and save the file.
	public void updateHighScores () {
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for(int i = 0; i < HIGHSCORE_SIZE; i++) {
			temp.put(highscoreNames[i], scores[i]);
		}
		highscores.put(currentBoardSize , temp);
		
		SaveLoad.saveToFile(this, FILE_NAME);
	}
}
