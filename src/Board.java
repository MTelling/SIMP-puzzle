import java.util.Random;

public class Board {

	protected Tile[][] grid;
	
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
				// Don't put a tile on the last space.
			}
		}
		
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
