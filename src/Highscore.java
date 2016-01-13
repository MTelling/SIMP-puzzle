import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Highscore implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final int HIGHSCORE_SIZE = 5;
	private static final String FILE_NAME = "Highscores";
	private static final String STD_NAME = "Robert";
	private static final int STD_SCORE = 0;
	
	private String difficulty;
	private ArrayList<LinkedList<String>> highscores = new ArrayList<LinkedList<String>>();
	
	public Highscore(String diff) {
		this.difficulty = diff;
		if(new File(FILE_NAME + "/" + this.difficulty + SaveLoad.FILE_EXT).exists()) {
			this.loadHighscores();
		} else {
			for(int i = 3; i <= 100; i++) {
				highscores.add(new LinkedList<String>());
			}
		}
		
		for(LinkedList<String> sizeDependantHighscores : highscores) {
			int sizeDifference = HIGHSCORE_SIZE - sizeDependantHighscores.size();
			if(sizeDifference > 0) {
				this.addMissingScores(sizeDependantHighscores, sizeDifference);
			} else if (sizeDifference < 0) {
				this.removeExtraScores(sizeDependantHighscores, Math.abs(sizeDifference));
			}
		}
		
		this.saveHighscores();
	}
	
	private void loadHighscores() {
		Object tempObj = SaveLoad.loadFromFile(FILE_NAME + "/" + this.difficulty);
		if(tempObj instanceof Highscore) {
			this.highscores = ((Highscore)tempObj).getHighscores();
		}
	}
	

	public void saveHighscores() {
		SaveLoad.saveToFile(this, FILE_NAME + "/" + this.difficulty);
	}
	
	public int isHighscore(int score) {
		int highscorePosition = -1;
		int i = 0;
		while(highscorePosition == -1 && i < HIGHSCORE_SIZE) {
			if(Integer.parseInt(getHighscoreAt(Window.getSettings().getTilesPerRowInBoard(), i)[1]) < score) {
				highscorePosition = i;
			}
			i++;
		}
		
		return highscorePosition;
	}
	
	public void addHighscore(String name, int score, int index) {
		LinkedList<String> currentHighscores = this.highscores.get(Window.getSettings().getTilesPerRowInBoard());
		currentHighscores.add(index, name + "," + score);
		if(currentHighscores.size() > HIGHSCORE_SIZE) {
			this.removeExtraScores(currentHighscores, Math.abs(HIGHSCORE_SIZE - currentHighscores.size()));
		}
		
		this.saveHighscores();
	}
	
	public String[] getHighscoreAt(int boardSize, int highscorePos) {
		LinkedList<String> currentHighscores = this.highscores.get(boardSize);
		String[] highscore = currentHighscores.get(highscorePos).split(",");
		return highscore;
	}
	
	private void addMissingScores(LinkedList<String> highscores, int amount) {
		for(int i = 0; i < amount; i++) {
			highscores.add(STD_NAME + "," + STD_SCORE);
		}
	}
	
	private void removeExtraScores(LinkedList<String> highscores, int amount) {
		for(int i = 0; i < amount; i++) {
			highscores.removeLast();
		}
	}
	
	private ArrayList<LinkedList<String>> getHighscores() {
		return this.highscores;
	}

	

}
