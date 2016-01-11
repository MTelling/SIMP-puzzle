
public enum AnimationSpeed {
	SLOW(1), MEDIUM(2), FAST(3);
	private final int value;
	
	private AnimationSpeed(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
