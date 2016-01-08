import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Board implements Serializable {
	

	private static final long 	serialVersionUID 	= 1L;
	private static final int 	MIN_BOARDSIZE 		= 3;
	private static final int 	MAX_BOARDSIZE 		= 100;
	
	
	private int 				boardSize;
	private int[][] 			tiles;
	private Point 				emptyTile;
	private int 				tileSize;
	
	
	public Board(int boardSize) {
		
		//Check if boardsize is within allowed range
		if(boardSize >= MIN_BOARDSIZE && boardSize <= MAX_BOARDSIZE) {
			this.boardSize 	= boardSize;
			this.tiles 		= new int[boardSize][boardSize];
			this.tileSize 	= (
					(Window.WINDOW_WIDTH - (Window.GAME_BORDER * 2) - 2 * Window.BOARD_BORDER_SIZE) / (boardSize)
					);
		} else {
			throw new IllegalArgumentException("Invalid board size");
		}
	}
	
	//Check if the game is won.
	//TODO: Should we allow a gamemode with a set time to win the game?
	public boolean isGameOver(){		
		//Only do the loops if the empty tile is in the bottom right cornor
		if( this.tiles[boardSize - 1][boardSize - 1] == Math.pow(boardSize, 2) ){
			
			//Check if the tiles are aligned from 1 to boardsize^2
			int counter = 1;
			for(int y = 0; y < boardSize; y++){
				for(int x = 0; x < boardSize; x++){
					if( !(tiles[x][y] == counter) ){
						return false;
					}
					counter++;
				}
			}
			return true;
		}
		return false;
	}
	
	//Helper method to init a solvable board
	//Aligns the tiles, to make a solved board.
	public void  makeSolvedBoard() {
		int tileCount = 1;
		for(int y = 0; y < this.boardSize; y++) {
			for(int x = 0; x < this.boardSize; x++) {
					this.tiles[x][y] = tileCount;
					tileCount++;
			}
		}
		
		//On a solved board, the emptytile is always in the bottom right cornor.
		emptyTile = new Point(this.boardSize - 1 , this.boardSize - 1);	
	}
	
	//Helper method to init a solvable board.
	public void makeRandomValidMoves(int howMany) {
		
		 Random rand = new Random();
		 
		 //Move x, then y, then x et cetera. Was x moved last?
		 boolean justMovedX = false;
		 
		 for(int i = 0; i < howMany; i++) {

			//Valid dx and dy.
			int[] validNums = { -1 , 1 };
			
			boolean hasNotMoved = true;
			while(hasNotMoved) {
				
				int randomNumber = validNums[rand.nextInt(2)];
				
				if(isMoveValid(randomNumber , 0)  && !justMovedX ) {
					moveTile(randomNumber , 0);
					justMovedX = true;
					hasNotMoved = false;
				} else if (isMoveValid(0 , randomNumber) && justMovedX ) {
					moveTile(0 , randomNumber);
					justMovedX = false;
					hasNotMoved = false;
				}

			}
		 }
	}
	
	// Make a new, solvable board.
	public void init() {

		makeSolvedBoard();
		makeRandomValidMoves(this.boardSize * 100);
		
	}

	
	public void moveTile(int dx, int dy) {
		this.tiles[emptyTile.x][emptyTile.y] = this.tiles[emptyTile.x + dx][emptyTile.y + dy];
		this.tiles[emptyTile.x + dx][emptyTile.y + dy] = (int) Math.pow(boardSize, 2);
		emptyTile.x += dx;
		emptyTile.y += dy;
	}
	
	//Method to determine if a move can be made. 
	public boolean isMoveValid(int dx, int dy) {
		boolean shouldMove = false;
		if (dx == -1) { //Right arrow
			shouldMove = (emptyTile.x > 0)? true:false;
		} else if (dx == 1) { //Left arrow
			shouldMove = (emptyTile.x < this.boardSize - 1)? true:false;
		} else if (dy == -1) { //Down arrow
			shouldMove = (emptyTile.y > 0)? true:false;
		} else if (dy == 1) { //Up arrow
			shouldMove = (emptyTile.y < this.boardSize - 1)? true:false;
		}
		return shouldMove;
	}
	
	//Discard current tile positions and update to new ones.
	public void setTiles(int[][] tiles) {
		int[][] newArray = new int[tiles.length][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = Arrays.copyOf(tiles[i], tiles[i].length);
		}
		this.tiles = newArray;
	}
	
	public Point getEmptyTile() {
		return emptyTile;
	}
	
	public void setEmptyTile(Point newEmptyTile) {
		this.emptyTile = new Point(newEmptyTile.x, newEmptyTile.y);
	}
	

	public int[][] getTiles() {
		return this.tiles;
	}
	
	public int getBoardSize() {
		return this.boardSize;
	}

	public int getTileSize() {
		return this.tileSize;
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				str += this.tiles[i][j] + " ";
			}
			str += "\n";
		}
		
		return str;
	}
	
}
