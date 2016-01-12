
public enum AnimationSpeed {
	SLOW(60), MEDIUM(20), FAST(10);
	private final int stepsPerMove;
	
	private AnimationSpeed(int stepsPerMove) {
		this.stepsPerMove = stepsPerMove;
	}
	
	public int getValue() {
		return (Window.getSettings().getCurrWindowSize().getBOARD_SIZE() / Window.getSettings().getTilesPerRowInBoard()) / this.stepsPerMove;
	}
}
