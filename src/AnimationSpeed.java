
public enum AnimationSpeed {
	SLOW(2), MEDIUM(5), FAST(9);
	private final int VALUE;
	
	private AnimationSpeed(int value) {
		this.VALUE = value;
	}
	
	public int getValue() {
		return this.VALUE;
	}
}
