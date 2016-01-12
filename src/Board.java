import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MIN_BOARDSIZE = 3;
	private static final int MAX_BOARDSIZE = 100;
	
	private int tilesPerRow;
	private Tile[][] tiles;
	private Point currEmptyTile;
	private Point nextEmptyTile;
	private int tileSize;
	
	public Board() {	
		//Check if boardsize is within allowed range
		if(Window.getSettings().getTilesPerRowInBoard() >= MIN_BOARDSIZE 
				&& Window.getSettings().getTilesPerRowInBoard() <= MAX_BOARDSIZE) {
			this.tilesPerRow = Window.getSettings().getTilesPerRowInBoard();
			this.tiles = new Tile[tilesPerRow][tilesPerRow];
			this.tileSize = (Window.getSettings().getCurrWindowSize().getWINDOW_WIDTH() - Window.getSettings().getCurrWindowSize().getGAME_BORDER() * 2 - Window.getSettings().getCurrWindowSize().getBOARD_BORDER_SIZE() * 2) / tilesPerRow;
		} else {
			throw new IllegalArgumentException("Invalid board size");
		}
	}
	
	//TODO: There is no difference to init and reset right now .
	public void init() {
		this.reset();
	}
	
	//Helper method to make start new game more understandable. 
	public void reset() {
		// Make a new, solved board.
		this.tilesPerRow = Window.getSettings().getTilesPerRowInBoard();
		this.tiles = new Tile[tilesPerRow][tilesPerRow];
		this.tileSize = (Window.getSettings().getCurrWindowSize().getWINDOW_WIDTH() 
				- Window.getSettings().getCurrWindowSize().getGAME_BORDER() * 2 
				- Window.getSettings().getCurrWindowSize().getBOARD_BORDER_SIZE() * 2) / tilesPerRow;

		this.makeSolvedBoard();		
	}
	
	
	//Helper method to init a solvable board
	//Aligns the tiles, to make a solved board.
	private void  makeSolvedBoard() {
		int tileCount = 1;
		for(int y = 0; y < this.tilesPerRow; y++) {
			for(int x = 0; x < this.tilesPerRow; x++) {
				//create all tiles in board. 
					int xCoord = Window.getSettings().getCurrWindowSize().getGAME_BORDER() + Window.getSettings().getCurrWindowSize().getBOARD_BORDER_SIZE() + (x * this.tileSize);
					int yCoord =  Window.getSettings().getCurrWindowSize().getWINDOW_HEIGHT() - Window.getSettings().getCurrWindowSize().getGAME_BORDER() - ((this.tilesPerRow - y) * (this.tileSize)) - Window.getSettings().getCurrWindowSize().getBOARD_BORDER_SIZE();
					this.tiles[x][y] = new Tile(tileCount, xCoord, yCoord);
					tileCount++;
			}
		}
		
		//On a solved board, the emptytile is always in the bottom right corner. 
		//Set both the nextEmptyTile and currEmptyTile to the same for animation purposes. 
		this.currEmptyTile = new Point(this.tilesPerRow - 1 , this.tilesPerRow - 1);
		this.nextEmptyTile = new Point(this.tilesPerRow - 1 , this.tilesPerRow - 1);	
	}
	
	
	/// Methods for randomizer ///
	private void moveEmptyTo(Point target, Point emptyTile, LinkedList<Move> moveSequence) {
		
		while(emptyTile.x < target.x) {
			makePseudoMove(moveSequence, new Move(1, 0), emptyTile);
		}
		
		while(emptyTile.x > target.x) {
			makePseudoMove(moveSequence, new Move(-1, 0), emptyTile);
		}
		
		while(emptyTile.y < target.y) {
			makePseudoMove(moveSequence, new Move(0, 1), emptyTile);
		}
		
		while(emptyTile.y > target.y) {
			makePseudoMove(moveSequence, new Move(0, -1), emptyTile);
		}
		
	}

	private void makePseudoMove(LinkedList<Move> moveSequence, Move move, Point emptyTile) {
		emptyTile.translate(move.dx, move.dy);
		moveSequence.add(move);
	}

	private void generateRandomMovesInCloseArea(LinkedList<Move> moveSequence, Point emptyTile, int numberOfMoves) {
	//Move x, then y, then x etc. Was x moved last?
	 boolean justMovedX = false;
	 Random rand = new Random();
	 for(int i = 0; i < numberOfMoves; i++) {
		 
		//Valid dx and dy.
		int[] validNums = { -1 , 1 };
		
		boolean hasNotMoved = true;
		while(hasNotMoved) {
			
			int randomNumber = validNums[rand.nextInt(2)];
			
			Move xMove = new Move(randomNumber, 0);
			Move yMove = new Move(0, randomNumber);
			
			if (isMoveValid(xMove, emptyTile) && !justMovedX) {
				makePseudoMove(moveSequence, xMove, emptyTile);
				justMovedX = true;
				hasNotMoved = false;
			} else if (isMoveValid(yMove, emptyTile) && justMovedX ) {
				
				makePseudoMove(moveSequence, yMove, emptyTile);
				
				justMovedX = false;
				hasNotMoved = false;
			}

		}
	 }
	}


	public LinkedList<Move> getRandomizingMoveSequence() {
		LinkedList<Move> moveSequence = new LinkedList<Move>();
		Point empty = ObjectCopy.point(currEmptyTile);
		

		int difficulty = Window.getSettings().getDifficulty();
		//If the board is larger than 8, the difficulty shouldn't be linear. Only if difficulty is easy. 
		if  (tilesPerRow > 8 && difficulty != Difficulty.EASY.getValue()) {
			difficulty += 1 +  (this.tiles.length % 10);
		}

		//Create queue of places to visit
		Stack<Point> placesToVisit = new Stack<>();
		
		int size = tilesPerRow - 1;
		int quarterSize = size/4;
		
		//Calculate x and y points for all corners, center of quadrants and center of board.
		Point center = new Point(size/2, size/2);
		Point upperLeftCorner = new Point(0, 0);
		Point upperRightCorner = new Point(size, 0); 
		Point lowerLeftCorner = new Point(0, size);
		Point lowerRightCorner = new Point(size,size);
		Point secondQuadrant = new Point(quarterSize, quarterSize);
		Point thirdQuadrant = new Point(quarterSize, size - quarterSize);
		Point fourthQuadrant = new Point(size - quarterSize, size - quarterSize);
		Point firstQuadrant = new Point(size - quarterSize, quarterSize);
		
		//Make a list of all those points.
		Point[] points = {center, upperLeftCorner, upperRightCorner, lowerLeftCorner, lowerRightCorner, secondQuadrant, thirdQuadrant, fourthQuadrant, firstQuadrant};
		
		//For difficulty add the points a number of times. 
		for (int i = 0; i < difficulty; i++) {
			//Shuffle the list and then add all the points to the visitStack.
			Collections.shuffle(Arrays.asList(points));

			for (Point point: points) {
				placesToVisit.push(point);
			}		
		}
		
		//Visit each place in the visitStack and on each place make a lot of random moves in the area around the point.
		Point next;
		while(!placesToVisit.isEmpty()) {
			next = placesToVisit.pop();
			moveEmptyTo(next, empty, moveSequence);
			int areaSizeToRandomize = difficulty*difficulty;
			//If the board is bigger than 25, it needs to make more moves unless difficulty is easy. 
			if  (tilesPerRow > 25 && difficulty != Difficulty.EASY.getValue()) {
				areaSizeToRandomize *= difficulty;
			}
			generateRandomMovesInCloseArea(moveSequence, empty, areaSizeToRandomize);
		}
		
		return moveSequence;
	}

	//Moves tile to the currEmptyTile position and swaps in tileArray + sets what the nextEmptyTile should be.
	//This activates the animationStae
	public void setToAnimationState(Move move) {
		
		//Translate next empty tile to the clicked position. 
		nextEmptyTile.translate(move.dx, move.dy);
	}
	
	//Returns true if arrived at final coord. 
	public boolean moveWithAnimation(int pixelsMovedPerRedraw){
		int x = this.nextEmptyTile.x;
		int y = this.nextEmptyTile.y;
		int dx = 0, dy = 0;
		
		//Find out if the tile has moved up or down.
		if (this.currEmptyTile.x - 1 == x) { //Tile has moved to the right
			dx = pixelsMovedPerRedraw;
		} else if (this.currEmptyTile.x + 1 == x) { //Tile has moved to the left
			dx = -pixelsMovedPerRedraw;
		} else if (this.currEmptyTile.y - 1 == y) { //Tile has moved down
			dy = pixelsMovedPerRedraw;
		} else if (this.currEmptyTile.y + 1 == y) { //Tile has moved up
			dy = -pixelsMovedPerRedraw;
		} 
		
		this.tiles[x][y].translate(dx, dy);
				
		boolean isAtFinalPosition = true;
		//Check if the tile is now at the final position.
		int finalX = this.tiles[this.currEmptyTile.x][this.currEmptyTile.y].getX();
		int finalY = this.tiles[this.currEmptyTile.x][this.currEmptyTile.y].getY();
		
		if (dx > 0 && this.tiles[x][y].getX() >= finalX ){
		} else if (dx < 0 && this.tiles[x][y].getX() <= finalX) {
		} else if (dy > 0 && this.tiles[x][y].getY() >= finalY) {
		} else if (dy < 0 && this.tiles[x][y].getY() <= finalY) {
		} else { //Tile is not at final position. 
			isAtFinalPosition = false;
		}

		if (isAtFinalPosition) {
			//Secure that the new tile is placed exactly at the right spot.
			this.tiles[this.currEmptyTile.x][this.currEmptyTile.y].setCoords(finalX, finalY);
			//Place the next empty tile at new empty tile position. 
			this.tiles[this.nextEmptyTile.x][this.nextEmptyTile.y].setCoords(finalX-(tileSize*(dx/pixelsMovedPerRedraw)), finalY-(tileSize*(dy/pixelsMovedPerRedraw)));
			
			//Return to default state. 
			this.setToDefaultState();
		}
		return isAtFinalPosition;
	}
	
	//Moves coordinates without animating. 
	public void moveWithoutAnimation() {
		this.setToDefaultState();
	}
	
	//Goes from animationState to defaultState
	private void setToDefaultState() {
		int tmpTileNum = this.tiles[currEmptyTile.x][currEmptyTile.y].getNumber();
		this.tiles[currEmptyTile.x][currEmptyTile.y].setNumber(this.tiles[nextEmptyTile.x][nextEmptyTile.y].getNumber());
		this.tiles[nextEmptyTile.x][nextEmptyTile.y].setNumber(tmpTileNum);
		
		currEmptyTile = ObjectCopy.point(nextEmptyTile);
	}
	
	
	//Check if the game is won.
	//TODO: Should we allow a gamemode with a set time to win the game?
	public boolean isGameOver(){	
		//Only do the loops if the empty tile is in the bottom right corner
		if (this.tiles[tilesPerRow - 1][tilesPerRow - 1].getNumber() == tilesPerRow*tilesPerRow ) {
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
	
	//Method to determine if a move can be made on the current board. 
	public boolean isMoveValid(Move move) {
		return isMoveValid(move, this.currEmptyTile);
	}
	
	//Method to determine if a move can be made from an empty tile in a given board size. 
	private boolean isMoveValid(Move move, Point emptyTile) {
		boolean shouldMove = false;
		if (move.dx == -1) { //Right arrow
			shouldMove = (emptyTile.x > 0) ? true : false;
		} else if (move.dx == 1) { //Left arrow
			shouldMove = (emptyTile.x < tilesPerRow - 1) ? true : false;
		} else if (move.dy == -1) { //Down arrow
			shouldMove = (emptyTile.y > 0) ? true : false;
		} else if (move.dy == 1) { //Up arrow
			shouldMove = (emptyTile.y < tilesPerRow - 1) ? true : false;
		}
		return shouldMove;
	}

	
	
	/// Getters from here ///
	
	public Point getCurrEmptyTile() {
		return this.currEmptyTile;
	}
	
	public Tile[][] getTiles() {
		return this.tiles;
	}
	
	public int getTilesPerRow() {
		return this.tilesPerRow;
	}

	public int getTileSize() {
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
	
}
