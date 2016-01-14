package dk.vigilddisciples.npuzzle.model;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import dk.vigilddisciples.npuzzle.SaveLoad;
import dk.vigilddisciples.npuzzle.NPuzzle;

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
		if(new File(FILE_NAME + this.difficulty + "." + SaveLoad.FILE_EXT).exists()) {
			this.loadHighscores();
		} else {
			for(int i = 0; i <= 100; i++) {
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
	
	/**
	 * Loads the highscores from the difficulty determined file
	 */
	private void loadHighscores() {
		Object tempObj;
		try {
			tempObj = SaveLoad.loadFromFile(FILE_NAME + this.difficulty);
			if(tempObj instanceof Highscore) {
				this.highscores = ((Highscore)tempObj).getHighscores();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Couldn't load highscores", "Error loading highscores", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/*
	 * Writes the highscores to a difficulty dertermined file
	 */
	public void saveHighscores() {
		SaveLoad.saveToFile(this, FILE_NAME + this.difficulty);
	}
	
	/**
	 * Checks whether or not the score is better then any of the current highscores for the current boardSize
	 * @param score - The players score
	 * @return An int 1 and HIGHSCORE_SIZE depending on where the score is supposed to be, return -1 if it's not a new highscore
	 */
	public int isHighscore(int boardSize, int score) {
		int highscorePosition = -1;
		int i = 0;
		while(highscorePosition == -1 && i < HIGHSCORE_SIZE) {
			if(Integer.parseInt(getHighscoreAt(boardSize, i)[1]) < score) {
				highscorePosition = i;
			}
			i++;
		}
		
		return highscorePosition;
	}
	
	/**
	 * Adds a highscore linked with the players name to the approriate position and boardSize
	 * @param name - Name of the player
	 * @param score - The players Score
	 * @param index - Highscore Place
	 */
	public void addHighscore(int boardSize, String name, int score, int index) {
		LinkedList<String> currentHighscores = this.highscores.get(boardSize);
		currentHighscores.add(index, name + "," + score);
		if(currentHighscores.size() > HIGHSCORE_SIZE) {
			this.removeExtraScores(currentHighscores, Math.abs(HIGHSCORE_SIZE - currentHighscores.size()));
		}
		
		this.saveHighscores();
	}
	
	/**
	 * Gets the highscore name and score from the board size and position specified
	 * @param boardSize - numberOfTilesPerRow to get highscores for
	 * @param highscorePos - position of the highscore
	 * @return a 2 position String array with the name and the score
	 */
	public String[] getHighscoreAt(int boardSize, int highscorePos) {
		LinkedList<String> currentHighscores = this.highscores.get(boardSize);
		String[] highscore = currentHighscores.get(highscorePos).split(",");
		return highscore;
	}
	
	/**
	 * Adds default scores to fill out the scoreboards
	 * @param highscores - The LinkedList holding the highscores
	 * @param amount - The ammount of default scores to add
	 */
	private void addMissingScores(LinkedList<String> highscores, int amount) {
		for(int i = 0; i < amount; i++) {
			highscores.add(STD_NAME + "," + STD_SCORE);
		}
	}
	
	/**
	 * Removes scores incase there are too many
	 * @param highscores - The LinkedList holding the highscores
	 * @param amount - The amount of scores to remove
	 */
	private void removeExtraScores(LinkedList<String> highscores, int amount) {
		for(int i = 0; i < amount; i++) {
			highscores.removeLast();
		}
	}
	
	/**
	 * A function to fetch all of the highscores, used for loading
	 * @return - The entire list of highscores
	 */
	private ArrayList<LinkedList<String>> getHighscores() {
		return this.highscores;
	}
}
