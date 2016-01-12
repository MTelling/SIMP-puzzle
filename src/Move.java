import java.io.Serializable;

public class Move implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	//These fields are public for easy access. 
	public int dx;
	public int dy;

	public Move(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public Move clone() {
		return new Move (this.dx, this.dy);
	}
	
	
	public boolean equals(Move other) {
		return (this.dx == other.dx && this.dy == other.dy);
	}
	
	//Returns the reverse of the move. 
	public Move reverse() {
		return new Move(-this.dx, -this.dy);
	}
	
	public String toString() {
		return "(" + dx + " , " + dy + ")";
	}
}
