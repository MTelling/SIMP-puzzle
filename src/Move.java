public class Move {
	//These fields are public for easy access. 
	public int dx;
	public int dy;

	public Move(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	//Returns the reverse of the move. 
	public Move reverse() {
		return new Move(-this.dx, -this.dy);
	}
}
