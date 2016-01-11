
public enum AnimationSpeed {
	SLOW(2), MEDIUM(5), FAST(9);
	private final int value;
	
	private AnimationSpeed(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
