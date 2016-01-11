import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Settings implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//Variables for controls. 
	private int[] controls;
	private static final int[] INVERTED_CONTROLS = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};
	private static final int[] NORMAL_CONTROLS = {KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_UP};
	
	//Variable for animation
	private int framesPerSecond;
	private int refreshRate;
	
	//Move with or without animation
	private boolean isAnimationOn;
		
	//Set picture to off or on and decides if the picture should be with or without labels. 
	private boolean isPictureOn;
	private boolean isLabelsOn;
	
	//Animation speeds
	private int scrambleAnimationSpeed;
	private int animationSpeed;
	
	//Board difficulty
	private int difficulty;
	private int tilesPerRowInBoard;
	
	public Settings() {
		
		//Set settings to Normal or inverted. 
		this.setControlsNormal();
		
		//Set difficulty
		this.setDifficulty(19);
		
		//Sets tiles per row in the board
		this.setTilesPerRowInBoard(4);
		
		//Set speed for the scrambling animation
		this.setScrambleAnimationSpeed(difficulty/2 + tilesPerRowInBoard*5);
		
		//Set speed for normal move animation
		this.setAnimationSpeed(2);
		
		//Turn animation on and off. 
		this.isAnimationOn = true;
		
		//Turn pictures on or off.
		this.isPictureOn = true;
		
		//Turn labels on or off when picture is active.
		this.isLabelsOn = false;
		
		//Set frames per second
		this.setFramesPerSecond(90);
		
		//calculate refreshRate for drawing.
		this.refreshRate = (int)Math.round(1000.0/framesPerSecond);
	}
	
	/// SETTERS FROM HERE ///
	
	public void setControlsNormal() {
		this.controls = NORMAL_CONTROLS;
	}
	
	public void setControlsInverted() {
		this.controls = INVERTED_CONTROLS;
	}
	
	public void setScrambleAnimationSpeed(int scrambleAnimationSpeed) {
		this.scrambleAnimationSpeed = scrambleAnimationSpeed;
	}
	
	public void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public void setTilesPerRowInBoard(int tilesPerRowInBoard) {
		this.tilesPerRowInBoard = tilesPerRowInBoard;
	}
	
	public void setFramesPerSecond(int framesPerSecond) {
		this.framesPerSecond = framesPerSecond;
	}

	public void setAnimationOn(boolean isAnimationOn) {
		this.isAnimationOn = isAnimationOn;
	}

	public void setPictureOn(boolean isPictureOn) {
		this.isPictureOn = isPictureOn;
	}

	public void setLabelsOn(boolean isLabelsOn) {
		this.isLabelsOn = isLabelsOn;
	}
	
	/// GETTERS FROM HERE /////
	
	public int getFramesPerSecond() {
		return framesPerSecond;
	}

	public int[] getControls() {
		return this.controls;
	}
	
	public int getRefreshRate() {
		return this.refreshRate;
	}
	
	public boolean isAnimationOn() {
		return this.isAnimationOn;
	}

	public boolean isLabelsOn() {
		return this.isLabelsOn;
	}

	public boolean isPictureOn() {
		return this.isPictureOn;
	}
	
	public boolean isControlsNormal() {
		if (this.controls.equals(NORMAL_CONTROLS)) {
			return true;
		} else {
			return false;
		}
	}

	public int getScrambleAnimationSpeed() {
		return scrambleAnimationSpeed;
	}

	public int getAnimationSpeed() {
		return animationSpeed;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getTilesPerRowInBoard() {
		return tilesPerRowInBoard;
	}

}
