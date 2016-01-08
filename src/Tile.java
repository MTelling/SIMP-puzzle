import java.io.Serializable;

public class Tile implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private double xCoord;
	private double yCoord;
	
	public Tile (int tileNum, double xCoord, double yCoord) {
		this.number = tileNum;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public void translate (double dx, double dy) {
		this.xCoord += dx;
		this.yCoord += dy;
	}
	
	public void setCoords (double xCoord, double yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	

	
	@Override
	public Tile clone() {
		return new Tile(this.number, this.xCoord, this.yCoord);
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	
	//////GETTERS FROM HERE////
	
	public double getX() {
		return this.xCoord;
	}
	
	public double getY() {
		return this.yCoord;
	}
	
	public int getNumber() {
		return this.number;
	}
}
