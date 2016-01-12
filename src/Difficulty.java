
public enum Difficulty {
	EASY(2), MEDIUM(6), HARD(12);
	private final int VALUE;
	
	private Difficulty(int value) {
		this.VALUE = value;
	}
	
	public int getValue() {
		return this.VALUE;
	}
}
