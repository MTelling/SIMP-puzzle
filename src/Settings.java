import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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
	private boolean isAnimationScramblingOn;
		
	//Set picture to off or on and decides if the picture should be with or without labels. 
	private boolean isPictureOn;
	private boolean isLabelsOn;
	
	//Picture to use as tiles if isPictureOn = true
	private String gamePicture;
	
	//Animation speeds
	private int scrambleAnimationSpeed;
	private int animationSpeed;
	
	//Board difficulty
	private int difficulty;
	private int tilesPerRowInBoard;
	
	
	private WindowSize CurrWindowSize;
	
	public Settings() {
		this.setCurrWindowSize(WindowSize.MEDIUM);
	}
	
	public void loadSettings() {
		Preferences settings = Preferences.userNodeForPackage(this.getClass());
		try {
			settings.sync();
		} catch (BackingStoreException e) {
			System.out.println("Error: Settings Sync Failed");
		}
		
		this.setCurrWindowSize(WindowSize.getWindowSizeFromOrdinal(settings.getInt("currWindowSize", 1)));
		
		this.setDifficulty(settings.getInt("gameDifficulty", Difficulty.EASY.getValue()));
		this.setTilesPerRowInBoard(settings.getInt("tilesPerRowInBoard", 3));
		
		if(settings.getBoolean("useNormalControls", true)) {
			this.setControlsNormal();
		} else {
			this.setControlsInverted();
		}
		
		this.setFramesPerSecond(settings.getInt("framesPerSecond", 90));
		this.refreshRate = settings.getInt("refreshRate", (int)Math.round(1000.0/framesPerSecond));
		
		this.setAnimationOn(settings.getBoolean("animateMoves", true));
		this.setAnimationSpeed(settings.getInt("animationSpeed", AnimationSpeed.MEDIUM.getValue()));
		this.setAnimationScramblingOn(settings.getBoolean("animateBoardScramble", true));
		this.setScrambleAnimationSpeed(settings.getInt("scrambleAnimationSpeed", 2));
		
		this.setPictureOn(settings.getBoolean("usePictureAsTiles", false));
		this.setGamePicture(settings.get("picturePath", "resources/pics/test.jpg"));
		this.setLabelsOn(settings.getBoolean("useCornerLabels", false));
	}
	
	public void saveSettings() {
		Preferences settings = Preferences.userNodeForPackage(this.getClass());
		
		settings.putBoolean("useNormalControls", this.controls.equals(NORMAL_CONTROLS) ? true : false);
		
		settings.putInt("framesPerSecond", this.framesPerSecond);
		settings.putInt("refreshRate", this.refreshRate);
		
		settings.putBoolean("animateMoves", this.isAnimationOn);
		settings.putInt("moveAnimationSpeed", this.animationSpeed);
		settings.putBoolean("animateBoardScramble", this.isAnimationScramblingOn);
		settings.putInt("scrambleAnimationSpeed", this.scrambleAnimationSpeed);
		
		settings.putBoolean("usePictureAsTiles", this.isPictureOn);
		settings.put("picturePath", this.gamePicture);
		settings.putBoolean("useCornerLabels", this.isLabelsOn);
		
		settings.putInt("gameDifficulty", this.difficulty);
		settings.putInt("tilesPerRowInBoard", this.tilesPerRowInBoard);
		
		settings.putInt("currWindowSize", this.CurrWindowSize.ordinal());
		
		try {
			settings.flush();
		} catch (BackingStoreException e) {
			System.out.println("Error: Could not save settings");
		}
	}
	
	/// SETTERS FROM HERE ///
	
	public void setControlsNormal() {
		this.controls = NORMAL_CONTROLS;
	}

	public void setCurrWindowSize(WindowSize currWindowSize) {
		CurrWindowSize = currWindowSize;
	}

	public void setControlsInverted() {
		this.controls = INVERTED_CONTROLS;
	}
	
	public void setScrambleAnimationSpeed(int movesPerAnimation) {
		this.scrambleAnimationSpeed = (getCurrWindowSize().getBOARD_SIZE() / getTilesPerRowInBoard()) / movesPerAnimation;
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
		this.refreshRate = (int)Math.round(1000.0/framesPerSecond);
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

	public void setAnimationScramblingOn(boolean isAnimationScramblingOn) {
		this.isAnimationScramblingOn = isAnimationScramblingOn;
	}
	
	public void setGamePicture(String picture) {
		this.gamePicture = picture;
	}
	
	/// GETTERS FROM HERE /////
	
	public WindowSize getCurrWindowSize() {
		return CurrWindowSize;
	}
	
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
	
	public String getGamePicture() {
		return this.gamePicture;
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

	public boolean isAnimationScramblingOn() {
		return isAnimationScramblingOn;
	}


}
