import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
	
	// Helper method to get valid moves while avoiding blocked tiles
	public ArrayList<Move> legalMoves (Point emptyTile, List<Point> blockedTiles) {
		ArrayList<Move> moves = new ArrayList<Move>();
		if (emptyTile.x > 0 && !blockedTiles.contains(new Point(emptyTile.x - 1, emptyTile.y))) {
			moves.add(new Move(-1 , 0));
		} 
		if (emptyTile.x < this.tilesPerRow - 1 && !blockedTiles.contains(new Point(emptyTile.x + 1, emptyTile.y))) {
			moves.add(new Move(1 , 0));
		} 
		if (emptyTile.y > 0 && !blockedTiles.contains(new Point(emptyTile.x, emptyTile.y - 1))) { //Down arrow
			moves.add(new Move(0 , -1));
		} 
		if (emptyTile.y < this.tilesPerRow - 1 && !blockedTiles.contains(new Point(emptyTile.x, emptyTile.y + 1))) {
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
	public LinkedList<Integer> makeListFromTileArray(Tile[][] array) {
		LinkedList<Integer> integers = new LinkedList<Integer>();
		
		for(int y = 0; y < this.tilesPerRow; y++) {
			for(int x = 0; x < this.tilesPerRow; x++) {
				integers.add(new Integer(array[x][y].getNumber()));
			}
		}
		
		return integers;
	}
	
	//Helper method to "Make a move"
	public Node makeTheMove(Point emptyPos, Move move, LinkedList<Integer> tiles, Node current) {
		
		int emptyPosNum = this.tilesPerRow * emptyPos.y + emptyPos.x;
	//	Point nextEmpty = current.getEmpty();
		emptyPos.translate(move.dx, move.dy);
		int nextEmptyPosNum = this.tilesPerRow * emptyPos.y + emptyPos.x;
		
		// Save current number
		int tempNext = tiles.get(this.tilesPerRow * emptyPos.y + emptyPos.x);
		
		// Make a move
		tiles.set(emptyPosNum, tiles.get(nextEmptyPosNum));
		tiles.set(nextEmptyPosNum, this.tilesPerRow * this.tilesPerRow);

		// Put a new Node in queue, with that move
		Node tempNode = new Node(current.getMoves() + 1, getManhattanDistance(tiles), current, tiles, move, emptyPos);

		// Reverse the move and be ready for the next move
		tiles.set(nextEmptyPosNum, tempNext);
		emptyPos.translate(-move.dx, -move.dy);
		
		return tempNode;
	}
	
	//Helper method to "Make the N'th move"
	public Node makeTheMoveWithBlocked(Point emptyPos, Point goal, Move move, LinkedList<Integer> tiles, Node current, boolean blocked) {
		
		int emptyPosNum = this.tilesPerRow * emptyPos.y + emptyPos.x;
	//	Point nextEmpty = current.getEmpty();
		emptyPos.translate(move.dx, move.dy);
		int nextEmptyPosNum = this.tilesPerRow * emptyPos.y + emptyPos.x;
		
		// Save current number
		int tempNext = tiles.get(this.tilesPerRow * emptyPos.y + emptyPos.x);
		
		// Make a move
		tiles.set(emptyPosNum, tiles.get(nextEmptyPosNum));
		tiles.set(nextEmptyPosNum, this.tilesPerRow * this.tilesPerRow);

		//Get heuristic and account for blocked tile
		int heuristic;
		if(blocked) {
			heuristic = 2000;
		} else {
			heuristic = heuristic1Piece(emptyPos, goal);
		}
		
		// Put a new Node in queue, with that move
		Node tempNode = new Node(current.getMoves() + 1, heuristic, current, tiles, move, emptyPos);

		// Reverse the move and be ready for the next move
		tiles.set(nextEmptyPosNum, tempNext);
		emptyPos.translate(-move.dx, -move.dy);
		
		return tempNode;
	}
	
	//Helper method to init blockedTiles-list
	public LinkedList<Point> initBlockedTiles() {
		LinkedList<Point> blockedTiles = new LinkedList<Point>();
		
		for(int i = 0; i < this.tilesPerRow; i++) {
			blockedTiles.add(new Point(-1 , i));
			blockedTiles.add(new Point(i, this.tilesPerRow));
			blockedTiles.add(new Point(i, -1));
			blockedTiles.add(new Point(this.tilesPerRow, i));
		}
		
		return blockedTiles;
	}
	
	
	//Helper method to make AI solve sequentially.
	private LinkedList<Move> moveEmptyTo(Point target, Point empty, LinkedList<Integer> tilesList, LinkedList<Point> blockedTiles, Point tempBlocked) {
		
		LinkedList<Move> moveSequence;
		blockedTiles.addLast(tempBlocked);
		
		moveSequence = findRouteAStar(empty, target, tilesList, blockedTiles);
		
		blockedTiles.removeLast();
		
		return moveSequence;

	}
	
	
	//Find best route between 2 points
	private LinkedList<Move> findRouteAStar(Point from, Point to, LinkedList<Integer> tilesList, LinkedList<Point> blockedTiles) {
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		Node first = new Node(0, heuristic1Piece(from, to), null , tilesList, null, from);
		queue.add(first);
		// Keep checking for a long as there is nodes to check
		while(queue.peek() != null) {
			Node current = queue.poll();
			tilesList = current.getBoard();

			// Only continue if we haven't found the goal yet
			if(current.getManhattan() != 0) {
				for( Move move : legalMoves(current.getEmpty()) ) {
					if(!(current.getLastMove() != null && current.getLastMove().reverse().equals(move))) {
						Point tempPoint = new Point(current.getEmpty().x + move.dx, current.getEmpty().y + move.dy);
						//If the tile is blocked, weigh the heuristic negatively
						if(blockedTiles.contains(tempPoint)) {
							queue.add(makeTheMoveWithBlocked(current.getEmpty(), to, move, tilesList, current, true));
						} else {
							queue.add(makeTheMoveWithBlocked(current.getEmpty(), to, move, tilesList, current, false));
						}
					}
					
				}
			} else {
				LinkedList<Move> result = backtrackMoves(current);
				Collections.reverse(result);
				return result;
			}
		}
		return null;
	}
	
	//Helper method for moveEmptyTo
	private void makePseudoMove(LinkedList<Move> moveSequence, LinkedList<Integer> tilesList, Move move, Point empty) {
		moveSequence.add(move);
		
		int emptyPosNum = this.tilesPerRow * empty.y + empty.x;
		empty.translate(move.dx, move.dy);
		int nextEmptyPosNum = this.tilesPerRow * empty.y + empty.x;
		tilesList.set(emptyPosNum, tilesList.get(nextEmptyPosNum));
		tilesList.set(nextEmptyPosNum, this.tilesPerRow * this.tilesPerRow);
	}
	
	//Helper method for moveEmptyTo
	private void makePseudoMove(LinkedList<Move> moveSequence, LinkedList<Integer> tilesList, Move move, Point empty, boolean goBack) {
		moveSequence.add(move);
		
		System.out.println(tilesList);
		
		int emptyPosNum = this.tilesPerRow * empty.y + empty.x;
		empty.translate(move.dx, move.dy);
		int nextEmptyPosNum = this.tilesPerRow * empty.y + empty.x;
		int tempNext = tilesList.get(nextEmptyPosNum);
		tilesList.set(emptyPosNum, tempNext);
		tilesList.set(nextEmptyPosNum, this.tilesPerRow * this.tilesPerRow);
		
		if(goBack) {
			// Reverse the move and be ready for the next move
			tilesList.set(nextEmptyPosNum, tempNext);
			tilesList.set(emptyPosNum, this.tilesPerRow * this.tilesPerRow);
			empty.translate(-move.dx, -move.dy);
		}
		
		System.out.println(tilesList);
		
	}
	
	
	//Helper method to get closest neighbor
	public Point closestGoalNeighbor(LinkedList<Integer> tilesList, Point from, Point target, List<Point> blockedTiles) {
		ArrayList<Move> neighbors = legalMoves (target);
		int closestDistance = 999999999;
		Point closestNeighbor = target;
		
		for( int i = 0; i < neighbors.size(); i++ ) {
			target.translate(neighbors.get(i).dx, neighbors.get(i).dy);
			if(Heuristic1Piece(from, target) < closestDistance && !blockedTiles.contains(new Point(target.x , target.y))) {
				closestNeighbor = new Point(target.x , target.y);
			}
			target.translate(neighbors.get(i).reverse().dx, neighbors.get(i).reverse().dy);
		}
		
		return closestNeighbor;
	}
	
	//Helper method to get heuristic for just 1 tile
	public int Heuristic1PieceWeighted(Point curr, Point goal, Point empty) {
		int xDistance = Math.abs(curr.x - goal.x);
		int yDistance = Math.abs(curr.y - goal.y);
		
		int xEmptyDistance = Math.abs(curr.x - empty.x);
		int yEmptyDistance = Math.abs(curr.y - empty.y);
		
		int emptyHeu;
		if((xEmptyDistance + yEmptyDistance) == 1) {
			emptyHeu = 0;
		} else {
			emptyHeu = xEmptyDistance + yEmptyDistance;
		}
		
		int result = (xDistance + yDistance) + emptyHeu;
		
		return result;
	}
	
	//Helper method to get heuristic for just 1 tile
	public int heuristic1Piece(Point from, Point to) {
		int xDistance = Math.abs(from.x - to.x);
		int yDistance = Math.abs(from.y - to.y);

		return (xDistance + yDistance);
	}
	
	//Helper method to check if contains higher number
	public boolean containsHigher(int[] start, int number) {
		for(int i : start) {
			if (i > number) {
				return true;
			}
		}
		return false;
	}

	//Helper method to get manhattan distance for a given board
	public int getManhattanDistance(List<Integer> currentTiles) {

		int size = this.tilesPerRow;
		int sum = 0;
		int[] xRow = new int[size];
		int[][] yRows = new int[size][size];
		
		//Go through the board.
		for(int y = 0; y < size; y++) {
				
			for(int x = 0; x < size; x++) {
				int currentNumber = currentTiles.get(size * y + x);
				// Don't save distance for the empty tile!
				if(currentTiles.get(size * y + x) != (size * size)){

					int targetX = (currentNumber - 1) % size;
					int targetY = (currentNumber - 1) / size;
					int xDistance = Math.abs(x - targetX);
					int yDistance = Math.abs(y - targetY);
					
					if(targetY == y) {
						xRow[x]= currentNumber;
					}
					if(targetX == x) {
						yRows[x][y] = currentNumber;
					}
					
					if(x == size - 1){
						if(containsHigher(xRow, currentNumber)) {
							sum += 2;
						}
					}
					
					if(containsHigher(yRows[x], currentNumber)) {
						sum += 2;
					}
					
				//	sum += (xDistance + yDistance) * ((x*x) + (y*y));
					sum += (xDistance + yDistance);
				}
			}
			
		}
		return sum;
	}
		
	public LinkedList<Move> moveOneTile(LinkedList<Integer> tilesList, LinkedList<Point> blockedTiles, Point curr, Move direction, Point empty) {
		LinkedList<Move> actualMoves = new LinkedList<Move>();
		
		actualMoves.addAll(moveEmptyTo(new Point (curr.x + direction.dx, curr.y + direction.dy), empty, tilesList, blockedTiles, curr));
		
		//makePseudoMove(actualMoves, tilesList, direction.reverse(), empty, true);
		actualMoves.add(direction.reverse());
		return actualMoves;
		
		
	}
	
	//Helper method to find point for desired number
	public Point numberToPoint(int number, LinkedList<Integer> tilesList) {
		for(int y = 0; y < this.tilesPerRow; y++) {
			for(int x = 0; x < this.tilesPerRow; x++) {
				if(tilesList.get(this.tilesPerRow * y + x) == number) {
					return new Point(x, y);
				}
			}
		}
		return new Point(0, 0);
	}
	
	public LinkedList<Move> doItOnce(LinkedList<Integer> tilesList, LinkedList<Point> blockedTiles, Point tileToMove, Move direction, Point emptyPos) {
		LinkedList<Integer> listOfTiles = new LinkedList<Integer>();
		listOfTiles.addAll(listOfTiles);
		
		LinkedList<Point> tilesBlocked = new LinkedList<Point>();
		tilesBlocked.addAll(blockedTiles);
		
		Point from = new Point(tileToMove.x, tileToMove.y);
		
		Move move = new Move(direction.dx, direction.dy);
		
		Point safeEmpty = new Point(emptyPos.x, emptyPos.y);
		
		return moveOneTile(listOfTiles, tilesBlocked, from, move, safeEmpty);
		
		
	}
	
	
	public void makeMovesInBoard(LinkedList<Move> moveSequence) {
		while (!moveSequence.isEmpty()){
			this.setToAnimationState(moveSequence.get(0));
			this.moveWithoutAnimation();
			moveSequence.remove(0);
		}
	}
	
	public LinkedList<Move> Solve(Tile[][] currTiles, Point emptyTile, boolean k) {
		LinkedList<Integer> tilesList = makeListFromTileArray(currTiles);
		LinkedList<Point> blockedTiles = initBlockedTiles();
		LinkedList<Move> moves = new LinkedList<Move>();
		
		Point tileToMove = numberToPoint(1, tilesList);
		
		LinkedList<Move> route = findRouteAStar(new Point (tileToMove.x, tileToMove.y), new Point(0,0), tilesList, blockedTiles);
		
		Point methodsOwnEmpty = new Point(emptyTile.x,emptyTile.y);
		
		Move direction = route.get(0);
		
		moves.addAll(moveOneTile(tilesList, blockedTiles, numberToPoint(1, tilesList), direction, methodsOwnEmpty));
		
		return moves;
	}
	
	public LinkedList<Move> Solve(Tile[][] currTiles, Point emptyTile)  {
		LinkedList<Move> finalList = new LinkedList<Move>();

		Tile[][] tileCopy = ObjectCopy.tile2D(this.tiles);
		Point copyOfEmpty = new Point(emptyTile.x, emptyTile.y);
		
		for(int i = 0; i < 2; i++) {
			LinkedList<Move> moves = ObjectCopy.cloneListMoves(Solve(currTiles, emptyTile, true));
			finalList.addAll(moves);
			makeMovesInBoard(moves);
		}
		emptyTile = copyOfEmpty;
		currTiles = ObjectCopy.tile2D(tileCopy);
		return finalList;
	}
		
		
		
		
		
	
//	public LinkedList<Move> Solve (Tile[][] currTiles, Point emptyTile) {
//
//		LinkedList<Integer> tiles = makeListFromTileArray(currTiles);
//		
//		Point empty = emptyTile;
//		
//		PriorityQueue<Node> queue = new PriorityQueue<Node>();		
//		
//		Node first = new Node(0, getManhattanDistance(tiles), null , tiles, null, empty);
//		
//		queue.add(first);
//
//		while(queue.peek() != null) {
//
//			// Get node with highest priority
//			Node current = queue.poll();
//			
//			tiles = current.getBoard();
//
//			// Check if we have a goal board yet.
//			if(current.getManhattan() != 0) {
//
//				for( Move move : legalMoves(current.getEmpty()) ) {
//					
//					if(!(current.getLastMove() != null && current.getLastMove().reverse().equals(move))) {
//						queue.add(makeTheMove(current.getEmpty(), move, tiles, current));
//					}
//				}
//			} else {
//
//				LinkedList<Move> result = backtrackMoves(current);
//				Collections.reverse(result);
//				System.out.println(result.size());
//				
//				return result;
//			}
//		}
//		return null;
//	}
//	
}
