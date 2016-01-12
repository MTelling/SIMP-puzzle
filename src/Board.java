import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MIN_BOARDSIZE = 3;
	private static final int MAX_BOARDSIZE = 100;
	
	private int tilesPerRow;
	private Tile[][] tiles;
	private Point currEmptyTile;
	private Point nextEmptyTile;
	private int tileSize;
	
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
	
	//TODO: There is no difference to init and reset right now .
	public void init() {
		this.reset();
	}
	
	//Helper method to make start new game more understandable. 
	public void reset() {
		// Make a new, solved board.
		this.makeSolvedBoard();		
	}
	
	
	//Helper method to init a solvable board
	//Aligns the tiles, to make a solved board.
	private void  makeSolvedBoard() {
		int tileCount = 1;
		for(int y = 0; y < this.tilesPerRow; y++) {
			for(int x = 0; x < this.tilesPerRow; x++) {
				//create all tiles in board. 
					int xCoord = Window.GAME_BORDER + Window.BOARD_BORDER_SIZE + (x * this.tileSize);
					int yCoord =  Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.tilesPerRow - y) * (this.tileSize)) - Window.BOARD_BORDER_SIZE;
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
	public LinkedList<Move> makeRandomValidMoves(int howMany) {
		
		 LinkedList<Move> scrambleMoves = new LinkedList<>();
		 Random rand = new Random();
		 howMany *= this.getTilesPerRow();
		 
		 //Move x, then y, then x etc. Was x moved last?
		 boolean justMovedX = false;
		 
		 for(int i = 0; i < howMany; i++) {

			//Valid dx and dy.
			int[] validNums = { -1 , 1 };
			
			boolean hasNotMoved = true;
			while(hasNotMoved) {
				
				int randomNumber = validNums[rand.nextInt(2)];
				
				Move xMove = new Move(randomNumber, 0);
				Move yMove = new Move(0, randomNumber);
				
				if(isMoveValid(xMove)  && !justMovedX ) {
					setToAnimationState(xMove);
					this.moveWithoutAnimation();
					
					scrambleMoves.add(xMove);
					
					justMovedX = true;
					hasNotMoved = false;
				} else if (isMoveValid(yMove) && justMovedX ) {
					setToAnimationState(yMove);
					this.moveWithoutAnimation();
					
					scrambleMoves.add(yMove);
					
					justMovedX = false;
					hasNotMoved = false;
				}

			}
		 }
		 
		 //Set board back to solved state.
		 this.reset();
		 
		 return scrambleMoves;
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
		this.tiles[currEmptyTile.x][currEmptyTile.y].setNumber(this.tiles[nextEmptyTile.x][nextEmptyTile.y].getNumber());
		this.tiles[nextEmptyTile.x][nextEmptyTile.y].setNumber((int) Math.pow(tilesPerRow, 2));
		
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
	
	//Method to determine if a move can be made. 
	public boolean isMoveValid(Move move) {
		boolean shouldMove = false;
		if (move.dx == -1) { //Right arrow
			shouldMove = (currEmptyTile.x > 0) ? true : false;
		} else if (move.dx == 1) { //Left arrow
			shouldMove = (currEmptyTile.x < this.tilesPerRow - 1) ? true : false;
		} else if (move.dy == -1) { //Down arrow
			shouldMove = (currEmptyTile.y > 0) ? true : false;
		} else if (move.dy == 1) { //Up arrow
			shouldMove = (currEmptyTile.y < this.tilesPerRow - 1) ? true : false;
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
	
	
	
	/////////////////// AI FROM HERE :D :D /////////////////////////
	
	

	// Helper method to get valid moves
	public ArrayList<Move> legalMoves (Point emptyTile) {
		ArrayList<Move> moves = new ArrayList<Move>();
		if (emptyTile.x > 0) {
			moves.add(new Move(-1 , 0));
		} 
		if (emptyTile.x < this.tilesPerRow - 1) {
			moves.add(new Move(1 , 0));
		} 
		if (emptyTile.y > 0) { //Down arrow
			moves.add(new Move(0 , -1));
		} 
		if (emptyTile.y < this.tilesPerRow - 1) {
			moves.add(new Move(0 , 1));
		}
		return moves;
	}

	// Helper method to backtrack moves
	public LinkedList<Move> backtrackMoves(Node node) {
		LinkedList<Move> moves = new LinkedList<Move>();
		Node current = node.clone();
		if(current.getLastMove() != null) {
			moves.add(current.getLastMove());
		}
		if(current.getPrevNode() != null) {
			moves.addAll(backtrackMoves(current.getPrevNode()));
		} 
		return moves;
	}
	
	//Helper method to make tiles[][] into a simple integer list
	public List<Integer> makeListFromTileArray(Tile[][] array) {
		List<Integer> integers = new ArrayList<Integer>();
		
		for(int y = 0; y < this.tilesPerRow; y++) {
			for(int x = 0; x < this.tilesPerRow; x++) {
				integers.add(array[x][y].getNumber());
			}
		}
		
		return integers;
	}

	public LinkedList<Move> Solve (Tile[][] currTiles, Point emptyTile) {

		
		List<Integer> tiles = makeListFromTileArray(currTiles);
		
		
		Point empty = emptyTile;
		
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		Queue<Node> visited = new LinkedList<Node>();
		
		
		Node first = new Node(0 , null , tiles, null, empty);
		
		queue.add(first);

		while(queue.peek() != null) {
			// Get node with highest priority
			Node current = queue.poll();
			

			tiles = current.getBoard();

			
			
			// Check if we have a goal board yet.
			if(current.getManhattan() != 0) {

				for( Move move : legalMoves(current.getEmpty()) ) {
					
					if(!(current.getLastMove() != null && current.getLastMove().reverse().equals(move))) {
					
						Point nextEmpty = ObjectCopy.point(current.getEmpty());
						nextEmpty.translate(move.dx, move.dy);
						
						// Save current numbers
						int temp = tiles.get(this.tilesPerRow * current.getEmpty().y + current.getEmpty().x);
						
						int tempNext = tiles.get(this.tilesPerRow * nextEmpty.y + nextEmpty.x);
						
						// Make a move
						tiles.set(this.tilesPerRow * current.getEmpty().y + current.getEmpty().x, tiles.get(this.tilesPerRow * nextEmpty.y + nextEmpty.x));
						tiles.set(this.tilesPerRow * nextEmpty.y + nextEmpty.x, this.tilesPerRow * this.tilesPerRow);
			
						// Put a new Node in queue, with that move
						Node tempNode = new Node(current.getMoves() + 1, current, tiles, move, nextEmpty);
						
						queue.add(tempNode);

						// Reverse the move and be ready for the next move
						tiles.set(this.tilesPerRow * nextEmpty.y + nextEmpty.x, tempNext);
						tiles.set(this.tilesPerRow * current.getEmpty().y + current.getEmpty().x, temp);
						
					}
				}
			} else {

				LinkedList<Move> result = backtrackMoves(current);
				Collections.reverse(result);
				System.out.println(result.size());
				
				return result;
			}
		}
		return null;
	}
	
}
