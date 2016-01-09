public class Move {
	public int dx;
	public int dy;
	
	public Move(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public Move reverse() {
		return new Move(-this.dx, -this.dy);
	}
}
