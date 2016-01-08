import java.awt.Point;

public class Tile {

	private int number;
	private Point coords;
	
	public Tile (int tileNum, Point tilePos) {
		this.number = tileNum;
		this.coords = tilePos;
	}
	
	public void translate (int dx, int dy) {
		this.coords.translate(dx, dy);
	}
	
	public Point getCoords() {
		return this.coords;
	}
	
	public int getNumber() {
		return this.number;
	}
	
}
