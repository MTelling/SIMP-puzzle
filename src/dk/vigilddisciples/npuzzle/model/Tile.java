package dk.vigilddisciples.npuzzle.model;
import java.io.Serializable;

public class Tile implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private int xCoord;
	private int yCoord;
	
	public Tile (int tileNum, int xCoord, int yCoord) {
		this.number = tileNum;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public void translate (int dx, int dy) {
		this.xCoord += dx;
		this.yCoord += dy;
	}
	
	/// SETTERS FROM HERE ///
	
	public void setCoords (int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	/// GETTERS FROM HERE ///
	
	public int getX() {
		return this.xCoord;
	}
	
	public int getY() {
		return this.yCoord;
	}
	
	public int getNumber() {
		return this.number;
	}
}
