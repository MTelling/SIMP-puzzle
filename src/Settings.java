import java.awt.event.KeyEvent;

public class Settings {
	
	private int[] controls;
	private static final int[] INVERTED_CONTROLS = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};
	private static final int[] NORMAL_CONTROLS = {KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_UP};
	
	public Settings() {
		this.controls = NORMAL_CONTROLS;
	}
	
	public int[] getControls() {
		return this.controls;
	}
}
