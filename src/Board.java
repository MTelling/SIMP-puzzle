import java.awt.Point;
import java.util.Random;

public class Board {

	protected Tile[][] grid;
	protected Point emptySpot;
	
	public Board(int n)
	{
		
		grid = new Tile[n][n];
		
		int[] uniqueNumbers = getRandomSequence(n);
		
		int counter = 0;
		for(int y = 0; y < n; y++)
		{
			
			for(int x = 0; x < n; x++)
			{
				// We need only n^2 - 1 tiles. Last corner needs to be empty.
				if( !(x == n-1 && y == n-1) )
				{
					grid[x][y] = new Tile(uniqueNumbers[counter]);
					counter++;
				}
				else
				{
					// Don't put a tile on the last space.
					// save coordinates for the empty spot though
					emptySpot = new Point (x,y);
				}
			}
		}
		
	}
	
	public boolean moveTile(int x, int y)
	{
		// Check if the empty spot is a neighbor
		if ( (x == emptySpot.x && ( y == emptySpot.y - 1 || y == emptySpot.y + 1 ))
				|| (y == emptySpot.y && ( x == emptySpot.x - 1 || x == emptySpot.x + 1 )) )
		{
			// If so, switch tile with empty spot
			
			grid[emptySpot.x][emptySpot.y] = grid[x][y];
			
			emptySpot.setLocation(x, y);			
			
			// Return true
			return true;
		}
		else
		{
			// Empty spot was not a neighbor. Return false
			return false;
		}
	}
	
	public Point getEmptySpot()
	{
		return emptySpot;
	}
	
	private int[] getRandomSequence(int n)
	{
		if( !(n < 1) )
		{
			// Get random numbers, between 1 and n, make them distinct, and limit to n numbers. Return in an array.
			return new Random().ints(1, n).distinct().limit(n).toArray();
		}

		throw new IllegalArgumentException("Random Sequence Length cannot be less than one!");
	}
}
