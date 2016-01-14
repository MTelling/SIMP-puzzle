package dk.vigilddisciples.npuzzle.model;
import java.io.Serializable;

public class Move implements Serializable{

	private static final long serialVersionUID = 1L;
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
