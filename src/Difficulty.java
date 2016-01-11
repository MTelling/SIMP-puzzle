
public enum Difficulty {
	EASY(1), MEDIUM(2), HARD(3);
	private final int value;
	
	private Difficulty(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
