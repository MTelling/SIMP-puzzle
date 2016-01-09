import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Settings implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//Variables for controls. 
	private int[] controls;
	private static final int[] INVERTED_CONTROLS = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};
	private static final int[] NORMAL_CONTROLS = {KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_UP};
	
	//Variable for animation
	private int framesPerSecond = 100;
	private int refreshRate;
	
	//Move with or without animation
	private boolean isAnimationOn;
		
	//Set picture to off or on and decides if the picture should be with or without labels. 
	private boolean isPictureOn;
	private boolean isLabelsOn;
	
	
	public Settings() {
		//Set settings to Normal or inverted. 
		this.controls = NORMAL_CONTROLS;
		
		//Sets refreshRate for drawing.
		this.refreshRate = (int)Math.round(1000.0/framesPerSecond);
		
		//Turn animation on and off. 
		this.isAnimationOn = true;
		
		//Turn pictures on or off.
		this.isPictureOn = true;
		
		//Turn labels on or off when picture is active.
		this.isLabelsOn = true;
	}
	
	/// GETTERS FROM HERE /////
	
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
}
