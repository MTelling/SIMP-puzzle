import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MIN_BOARDSIZE = 3;
	private static final int MAX_BOARDSIZE = 100;
	private static final int DIFFICULTY = 100;
	
	private int tilesPerRow;
	private Tile[][] tiles;
	private Point currEmptyTile;
	private Point nextEmptyTile;
	private double tileSize;
	private double velocity;
	
	public Board(int tilesPerRow) {
		
		//Check if boardsize is within allowed range
		if(tilesPerRow >= MIN_BOARDSIZE && tilesPerRow <= MAX_BOARDSIZE) {
			this.tilesPerRow = tilesPerRow;
			this.tiles = new Tile[tilesPerRow][tilesPerRow];
			this.tileSize = (Window.WINDOW_WIDTH - Window.GAME_BORDER * 2 - Window.BOARD_BORDER_SIZE * 2) / tilesPerRow;
		} else {
			throw new IllegalArgumentException("Invalid board size");
		}
	}
	
	public void init() {
		this.velocity = 4;
		this.reset();
	}
	
	//Helper method to make start new game more understandable. 
	public void reset() {
		// Make a new, solvable board.
		this.makeSolvedBoard();
		this.makeRandomValidMoves(this.tilesPerRow * DIFFICULTY);
	}
	
	//Helper method to init a solvable board
	//Aligns the tiles, to make a solved board.
	private void  makeSolvedBoard() {
		int tileCount = 1;
		for(int y = 0; y < this.tilesPerRow; y++) {
			for(int x = 0; x < this.tilesPerRow; x++) {
				//create all tiles in board. 
					double xCoord = Window.GAME_BORDER + Window.BOARD_BORDER_SIZE + (x * this.tileSize);
					double yCoord =  Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.tilesPerRow - y) * (this.tileSize)) - Window.BOARD_BORDER_SIZE;
					this.tiles[x][y] = new Tile(tileCount, xCoord, yCoord);
					tileCount++;
			}
		}
		
		//On a solved board, the emptytile is always in the bottom right corner. 
		//Set both the nextEmptyTile and currEmptyTile to the same for animation purposes. 
		this.currEmptyTile = new Point(this.tilesPerRow - 1 , this.tilesPerRow - 1);
		this.nextEmptyTile = new Point(this.tilesPerRow - 1 , this.tilesPerRow - 1);	
	}
	
	//Helper method to init a solvable board.
	public void makeRandomValidMoves(int howMany) {
		
		 Random rand = new Random();
		 
		 //Move x, then y, then x etc. Was x moved last?
		 boolean justMovedX = false;
		 
		 for(int i = 0; i < howMany; i++) {

			//Valid dx and dy.
			int[] validNums = { -1 , 1 };
			
			boolean hasNotMoved = true;
			while(hasNotMoved) {
				
				int randomNumber = validNums[rand.nextInt(2)];
				
				if(isMoveValid(randomNumber , 0)  && !justMovedX ) {
					setToAnimationState(randomNumber , 0);
					this.moveWithoutAnimation(randomNumber, 0);
					
					justMovedX = true;
					hasNotMoved = false;
				} else if (isMoveValid(0 , randomNumber) && justMovedX ) {
					setToAnimationState(0 , randomNumber);
					this.moveWithoutAnimation(0, randomNumber);

					justMovedX = false;
					hasNotMoved = false;
				}
				
				

			}
		 }
	}
		
	//Moves tile to the currEmptyTile position and swaps in tileArray + sets what the nextEmptyTile should be.
	//This activates the animationStae
	public void setToAnimationState(int dx, int dy) {
		
		//Translate next empty tile to the clicked position. 
		nextEmptyTile.translate(dx, dy);
	}
	
	//Returns true if arrived at final coord. 
	public boolean moveWithAnimation(){
		int x = this.nextEmptyTile.x;
		int y = this.nextEmptyTile.y;
		double dx = 0, dy = 0;
		
		//Find out if the tile has moved up or down.
		if (this.currEmptyTile.x - 1 == x) { //Tile has moved to the right
			dx = velocity;
		} else if (this.currEmptyTile.x + 1 == x) { //Tile has moved to the left
			dx = -velocity;
		} else if (this.currEmptyTile.y - 1 == y) { //Tile has moved down
			dy = velocity;
		} else if (this.currEmptyTile.y + 1 == y) { //Tile has moved up
			dy = -velocity;
		} 
		
		this.tiles[x][y].translate(dx, dy);
				
		boolean isAtFinalPosition = true;
		//Check if the tile is now at the final position.
		double finalX = this.tiles[this.currEmptyTile.x][this.currEmptyTile.y].getX();
		double finalY = this.tiles[this.currEmptyTile.x][this.currEmptyTile.y].getY();
		
		if (dx > 0 && this.tiles[x][y].getX() >= finalX ){
		} else if (dx < 0 && this.tiles[x][y].getX() <= finalX) {
		} else if (dy > 0 && this.tiles[x][y].getY() >= finalY) {
		} else if (dy < 0 && this.tiles[x][y].getY() <= finalY) {
		} else { //Tile is not at final position. 
			isAtFinalPosition = false;
		}

		if (isAtFinalPosition) {
			System.out.println("arrived at final");
			//Secure that the new tile is placed exactly at the right spot.
			this.tiles[this.currEmptyTile.x][this.currEmptyTile.y].setCoords(finalX, finalY);
			//Place the next empty tile at new empty tile position. 
			this.tiles[this.nextEmptyTile.x][this.nextEmptyTile.y].setCoords(finalX-(tileSize*(dx/velocity)), finalY-(tileSize*(dy/velocity)));
			
			//Reset animationState so curr is equal to new. 
			//TODO: should just swap tiles really.
			this.setToDefaultState((int)(dx/velocity), (int)(dy/velocity));
		}
		return isAtFinalPosition;
	}
	
	//Moves coordinates without animating. 
	private void moveWithoutAnimation(int dx, int dy) {
		this.setToDefaultState(dx, dy);
	}
	
	//Goes from animationStates to defaultState
	private void setToDefaultState(int dx, int dy) {
		int tmpTileNum = this.tiles[currEmptyTile.x][currEmptyTile.y].getNumber();
		this.tiles[currEmptyTile.x][currEmptyTile.y].setNumber(this.tiles[nextEmptyTile.x][nextEmptyTile.y].getNumber());
		this.tiles[nextEmptyTile.x][nextEmptyTile.y].setNumber(tmpTileNum);
		
		currEmptyTile = ObjectCopy.point(nextEmptyTile);
		
		System.out.println(this.toString());
	}
	
	
	//Check if the game is won.
	//TODO: Should we allow a gamemode with a set time to win the game?
	public boolean isGameOver(){	
		System.out.println("called");
		//Only do the loops if the empty tile is in the bottom right corner
		if (this.tiles[tilesPerRow - 1][tilesPerRow - 1].getNumber() == Math.pow(tilesPerRow, 2) ) {
			System.out.println("got here");
			//Check if the tiles are aligned from 1 to tilesPerRow^2
			int counter = 1;
			for (int y = 0; y < tilesPerRow; y++){
				for (int x = 0; x < tilesPerRow; x++){
					if (tiles[x][y].getNumber() != counter) {
						return false;
					}
					counter++;
				}
			}
			return true;
		}
		return false;
	}
	
	//Method to determine if a move can be made. 
	public boolean isMoveValid(int dx, int dy) {
		boolean shouldMove = false;
		if (dx == -1) { //Right arrow
			shouldMove = (currEmptyTile.x > 0) ? true : false;
		} else if (dx == 1) { //Left arrow
			shouldMove = (currEmptyTile.x < this.tilesPerRow - 1) ? true : false;
		} else if (dy == -1) { //Down arrow
			shouldMove = (currEmptyTile.y > 0) ? true : false;
		} else if (dy == 1) { //Up arrow
			shouldMove = (currEmptyTile.y < this.tilesPerRow - 1) ? true : false;
		}
		return shouldMove;
	}
	
	
	////////Getter methods from here////////
	
	public Point getCurrEmptyTile() {
		return this.currEmptyTile;
	}
	
	public Tile[][] getTiles() {
		return this.tiles;
	}
	
	public int getTilesPerRow() {
		return this.tilesPerRow;
	}

	public double getTileSize() {
		return this.tileSize;
	}
	
	//ability to get tiles represented with their numbers as string
	public String toString() {
		String str = "";
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				str += this.tiles[j][i].getNumber() + " ";
			}
			str += "\n";
		}
		
		return str;
	}
	
	
	
	/*
	
	
	//Discard current tile positions and update to new ones.
	public void setTiles(int[][] tiles) {
		int[][] newArray = new int[tiles.length][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = Arrays.copyOf(tiles[i], tiles[i].length);
		}
		this.tiles = newArray;
	}
	
	*
	public void setEmptyTile(Point newEmptyTile) {
		this.emptyTile = new Point(newEmptyTile.x, newEmptyTile.y);
	}
	*
	*
	*/
}
