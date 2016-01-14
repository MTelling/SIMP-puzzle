import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ObjectCopy {
	public static int[][] array2D(int[][] array) {
		int[][] newArray = new int[array.length][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = Arrays.copyOf(array[i], array[i].length);
		}
		return newArray;
	}
	
	public static LinkedList<Integer> cloneList(LinkedList<Integer> list) {
	    LinkedList<Integer> clone = new LinkedList<Integer>();
	    for(Integer item: list) clone.add(new Integer(item));
	    return clone;
	}
	
	public static LinkedList<Move> cloneListMoves(LinkedList<Move> list) {
	    LinkedList<Move> clone = new LinkedList<Move>();
	    for(Move item: list) clone.add(new Move(item.dx, item.dy));
	    return clone;
	}
	
	public static Tile[][] tile2D(Tile[][] tiles) {
		Tile[][] newTiles = new Tile[tiles.length][tiles.length];
		for (int i = 0; i < newTiles.length; i++) {
			for(int j = 0; j < newTiles.length; j++) {
				newTiles[i][j] = tiles[i][j].clone();
			}
		}
		return newTiles;
	}
	
	public static Point point(Point point) {
		return new Point(point.x, point.y);
	}
}
