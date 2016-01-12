
public enum AnimationSpeed {
	SLOW(60), MEDIUM(20), FAST(10);
	private final int stepsPerMove;
	
	private AnimationSpeed(int stepsPerMove) {
		this.stepsPerMove = stepsPerMove;
	}
	
	public int getValue() {
		int speed = (Window.getSettings().getCurrWindowSize().getBOARD_SIZE() / Window.getSettings().getTilesPerRowInBoard()) / this.stepsPerMove;
		return (speed == 0) ? 1 : speed;
	}
}
