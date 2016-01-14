
public enum AnimationSpeed {
	SLOW(60), MEDIUM(20), FAST(10);
	private final int stepsPerMove;

	private AnimationSpeed(int stepsPerMove) {
		this.stepsPerMove = stepsPerMove;
	}

	public static AnimationSpeed getAnimationSpeedFromOrdinal(int ordinal) {
		switch(ordinal) {
		case 0:
			return AnimationSpeed.SLOW;
		case 1:
			return AnimationSpeed.MEDIUM;
		case 2:
			return AnimationSpeed.FAST;
		default:
			return AnimationSpeed.MEDIUM;
		}
	}
	
	public int getValue() {
		int speed = (int)Math.round((Window.getSettings().getCurrWindowSize().getBOARD_SIZE() / Window.getSettings().getTilesPerRowInBoard()) / this.stepsPerMove);
		//If speed is set to 0 because of rounding, tell it to be 1 instead. 
		return (speed == 0) ? 1 : speed;
	}
}
