package dk.vigilddisciples.npuzzle;
import java.awt.Point;
import java.util.Arrays;

public class ObjectCopy {
	public static int[][] array2D(int[][] array) {
		int[][] newArray = new int[array.length][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = Arrays.copyOf(array[i], array[i].length);
		}
		return newArray;
	}
	public static Point point(Point point) {
		return new Point(point.x, point.y);
	}
}
