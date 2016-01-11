
public enum Difficulty {
	EASY(1), MEDIUM(2), HARD(3);
	private final int VALUE;
	
	private Difficulty(int value) {
		this.VALUE = value;
	}
	
	public int getValue() {
		return this.VALUE;
	}
}
