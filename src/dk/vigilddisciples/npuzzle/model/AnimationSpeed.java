package dk.vigilddisciples.npuzzle.model;

import dk.vigilddisciples.npuzzle.NPuzzle;

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
		return getValue(NPuzzle.getSettings().getTilesPerRowInBoard());
	}
	
	public int getValue(int tilesPerRow) {
        //Calculate what speed to return depending on board size and tiles per row.
		int speed = (int)Math.round((NPuzzle.getSettings().getCurrWindowSize().getBOARD_SIZE() / tilesPerRow) / this.stepsPerMove);
		//If speed is set to 0 because of rounding, tell it to be 1 instead. 
		return (speed == 0) ? 1 : speed;
	}
}
