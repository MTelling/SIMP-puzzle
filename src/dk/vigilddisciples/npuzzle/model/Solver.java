package dk.vigilddisciples.npuzzle.model;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Solver {
	private LinkedList<Integer> tilesList;
	private LinkedList<Point> blockedTiles;
	private Point emptyTile;
	private int tilesPerRow;
	
	public Solver(Tile[][] tiles, Point empty){
		this.tilesPerRow = tiles.length;
		this.emptyTile = new Point(empty.x, empty.y);
		this.tilesList = makeListFromTileArray(tiles);
		this.blockedTiles = new LinkedList<Point>();
	}
	
	//Helper method to make a list from Tile[][]
	private LinkedList<Integer> makeListFromTileArray(Tile[][] array){
		LinkedList<Integer> newList = new LinkedList<Integer>();
		
		for(int y = 0; y < this.tilesPerRow; y++){
			for(int x = 0; x < this.tilesPerRow; x++){
				newList.add(new Integer(array[x][y].getNumber()));
			}
		}
		return newList;
	}
	
	//Helper method to get manhattan distance between 2 points
	private int manhattan(Point from, Point to){
		int xDistance = Math.abs(from.x - to.x);
		int yDistance = Math.abs(from.y - to.y);
		return xDistance + yDistance;
	}
	
	//Helper method to check for linear conflicts
		public boolean containsHigher(int[] start, int number) {
			for(int i = 0; i < start.length - 1; i++){
				if(start[i] > number){
					return true;
				}
			}
			return false;
		}
		
		public int getHeuristic(LinkedList<Integer> tiles){
			
			int size = this.tilesPerRow;
			int sum = 0;
			int[] xRow = new int[size];
			int[][] yRows = new int[size][size];
			
			//Iterate through board
			for(int y = 0; y < size; y++){
				for(int x = 0; x < size; x++){
					int currentNumber = tiles.get(size * y + x);
					//Don't save distance for empty tile
					if(currentNumber != size * size){
						int targetX = (currentNumber - 1) % size;
						int targetY = (currentNumber - 1) / size;
						
						if(targetY == y){
							//If tile is in right column, add it to array to check for linear conflicts
							xRow[x] = currentNumber;
						}
						if(targetX == x){
							//If tile is in right row, add it to array to check for linear conflicts
						}
						//It only checks for 1 linear conflict now.
						//This doesn't matter, as we're only using it on boardsizes up to 4x4.
						//On bigger boardsizes than 4x4 this would mean less accuracy, yet still admissible
						if(x == size - 1){ //When a row is done, check for conflicts
							if(containsHigher(xRow, currentNumber)){
								sum += 2;
							}
						}
						if(containsHigher(yRows[x], currentNumber)){
							sum += 2;
						}
						
						sum += manhattan(new Point(x,y), new Point (targetX,targetY)); 
						
					}
				}
			}
			return sum;
		}
	
	//Helper method to calculate Linear Conflict
	private int linearConflict(ArrayList<Integer> row, int number, boolean yAxis){
		int[] shouldBe = new int[this.tilesPerRow];
		int empty = this.tilesPerRow * this.tilesPerRow;
		int inversions = 0;
		for(int i = 0; i < this.tilesPerRow; i++) {
			if(yAxis){
				shouldBe[i] = i*this.tilesPerRow+number;
			} else {
				shouldBe[i] = number*this.tilesPerRow+i;
			}
		}
		
		for (int i = 1; i < this.tilesPerRow; i++) {
			//Check if number isnt the empty tile and check if the number should be on this row/column
			if (row.get(i) != empty && 0 <= Arrays.binarySearch(shouldBe, row.get(i))) {
				for (int j = 0; j < i; j++) {
					if (row.get(j) != empty && 0 <= Arrays.binarySearch(shouldBe, row.get(j))) {
						//We have 2 numbers and both should be in this row or column
						//Check if they are inverted
						if ((row.get(i) < row.get(j)) != (i < j)) {
							//Numbers are inverted!
							inversions++;
						}
					}
				}
			}
		}
		return inversions*2;
	}
	
	//Helper method to copy tiles list
	private LinkedList<Integer> copyTilesList(LinkedList<Integer> list){
		LinkedList<Integer> newList = new LinkedList<Integer>();
		for(int i = 0; i < list.size(); i++){
			newList.add(new Integer(list.get(i)));
		}
		return newList;
	}
	
	//Helper method to find legal moves from current position
	private List<Move> legalMoves(Point from) {
		List<Move> moves = new ArrayList<Move>();
		if(from.x > 0){
			moves.add(new Move(-1,0));
		}
		if(from.x < this.tilesPerRow - 1) {
			moves.add(new Move(1,0));
		}
		if(from.y > 0){
			moves.add(new Move(0,-1));
		}
		if(from.y < this.tilesPerRow - 1){
			moves.add(new Move(0,1));
		}
		return moves;
	}
	
	//Helper method to "Make a move". Used to create new nodes in AStarSearch
	private SearchNode makeMove(Point to, Move move, SearchNode current, boolean entireBoard){
		//Save current position in tilesList
		int currentPos = this.tilesPerRow * current.getEmpty().y + current.getEmpty().x;
		//"Make the move"
		current.getEmpty().translate(move.dx, move.dy);
		//Save next position in tilesList
		int nextPos = this.tilesPerRow * current.getEmpty().y + current.getEmpty().x;
		//Save next number from tilesList
		int currentNum = current.getTiles().get(currentPos);
		int nextNum = current.getTiles().get(nextPos);
		//Update tilesList with this move
		current.getTiles().set(currentPos, nextNum);
		current.getTiles().set(nextPos, currentNum);
		
		//get heuristic and weigh it drastically if tile is blocked
		int heuristic;
		if(entireBoard){
			long startTime = System.nanoTime();
			heuristic = getHeuristic(current.getTiles());
			//System.out.println("heuristic runtime in nanoseconds: " + (System.nanoTime() - startTime));
		} else {
			if(this.blockedTiles.contains(current.getEmpty())){
				heuristic = 99999999;
			} else {
				heuristic = manhattan(current.getEmpty(), to);
			}
		}
		
		//Make a node, with this move made
		SearchNode newNode = new SearchNode(current.getMoves() + 1, heuristic, current, copyTilesList(current.getTiles()), move, current.getEmpty());
		
		//Reverse the move and be ready for the next move
		//TODO: is this necessary? Do I ever use this nodes copy of tilesList and position again, or just the move? It IS necessary to reverse the move. But the list?
		current.getTiles().set(currentPos, currentNum);
		current.getTiles().set(nextPos, nextNum);
		current.getEmpty().translate(-move.dx, -move.dy);
		
		//Return the node to be put in queue
		return newNode;
	}
	
	//Helper method to backtrack moves from a goalnode. Used for AStarSearch
	private LinkedList<Move> backtrackMoves(SearchNode node){
		LinkedList<Move> moves = new LinkedList<Move>();
		SearchNode current = new SearchNode(0, 0, node.getPrevNode(), null, node.getLastMove(), null);
		if(current.getLastMove() != null){
			moves.add(current.getLastMove());
		}
		if(current.getPrevNode() != null){
			moves.addAll(backtrackMoves(current.getPrevNode()));
		}
		return moves;
	}
	
	//A Star algorithm. Can be used either for a specific route or to solve entire board
	public LinkedList<Move> AStarSearch(Point from, Point to, boolean entireBoard){
		PriorityQueue<SearchNode> queue = new PriorityQueue<SearchNode>();
		SearchNode first;
		if(entireBoard){
			first = new SearchNode(0, getHeuristic(this.tilesList), null, copyTilesList(this.tilesList), null, this.emptyTile);
		} else {
			first = new SearchNode(0, manhattan(from, to), null, copyTilesList(this.tilesList), null, from);
		}
		queue.add(first);
		//Keep exploring as long as there is still nodes to explore
		while(queue.peek() != null){
			//Next next node and remove from queue
			SearchNode current = queue.poll();
			
			//Check if current node is goalposition. Otherwise continue
			if(current.getHeuristic() != 0){
				for(Move move : legalMoves(current.getEmpty())){
					//TODO: Right now node is still created if tile is blocked, but the node is weighted so it will never be visited. I didn't add this at first, but it didn't work without for some reason.
					queue.add(makeMove(to, move, current, entireBoard));
				}
			} else {
				//This node is at goal position! Backtrack the moves made and return
				LinkedList<Move> result = backtrackMoves(current);
				Collections.reverse(result);
				return result;
			}
		}
		//This should never happen if used correctly. Only added for compile reasons
		return null;
	}
	
	//This needs to be called after this class has calculated moves, in order for this object to calc more moves
	private void updateState(LinkedList<Move> moves){
		for(int i = 0; i < moves.size(); i++) {
			//Save current position in tilesList
			int currentPos = this.tilesPerRow * this.emptyTile.y + this.emptyTile.x;
			//"Make the move"
			this.emptyTile.translate(moves.get(i).dx, moves.get(i).dy);
			//Save next position in tilesList
			int nextPos = this.tilesPerRow * this.emptyTile.y + this.emptyTile.x;
			//Save next number from tilesList
			int currentNum = this.tilesList.get(currentPos);
			int nextNum = this.tilesList.get(nextPos);
			//Update tilesList with this move
			this.tilesList.set(currentPos, nextNum);
			this.tilesList.set(nextPos, currentNum);
		}
	}
	
	//TODO: Fuck this though.
	private void updateState(Move move){
		LinkedList<Move> list = new LinkedList<Move>();
		list.add(move);
		updateState(list);
	}
	
	public LinkedList<Move> moveEmptyTo(Point to, Point avoidThis){
		LinkedList<Move> moveSequence;
		if(avoidThis != null){
			this.blockedTiles.addLast(avoidThis);
		}
		moveSequence = AStarSearch(this.emptyTile, to, false);
		if(avoidThis != null){
			this.blockedTiles.removeLast();
		}
		updateState(moveSequence);
		return moveSequence;
	}
	
	
	//SELF HELP! THIS WORKS
	public LinkedList<Move> test () {
		LinkedList<Move> moveSequence = new LinkedList<Move>();
		
		for(int i = 0; i < 2; i++){
			moveSequence.addAll(moveTileOnce(1,new Move(1,0)));
		}
		return moveSequence;
	}
	
	//Helper method to find Point for desired number
	private Point numberToPoint(int number){
		for(int y = 0; y < this.tilesPerRow; y++){
			for(int x = 0; x < this.tilesPerRow; x++){
				if(this.tilesList.get(this.tilesPerRow * y + x) == number){
					return new Point(x,y);
				}
			}
		}
		//This should never happen, but is added for compile reasons
		return new Point(13,37);
	}
	
	//Method to move one tile in one direction
	private LinkedList<Move> moveTileOnce(int tileToMove, Move direction){
		LinkedList<Move> moves = new LinkedList<Move>();
		
		//Get Point representation of desired tile
		Point tileAsPoint = numberToPoint(tileToMove);
		//Get Point representation of where empty has to be, to move that tile, in that direction
		Point emptyGoHere = new Point (tileAsPoint.x + direction.dx, tileAsPoint.y + direction.dy);
		
		//Let empty to where it should, and let it avoid the tile to move
		moves.addAll(moveEmptyTo(emptyGoHere, tileAsPoint));
		
		//Move empty in opposite direction, to move the right tile in correct direction
		Move lastMove = direction.reverse();
		
		//Update this objects state
		updateState(lastMove);
		
		moves.add(lastMove);
		
		return moves;
		
	}
	
	//Move specific tile to specific Point
	public LinkedList<Move> moveTile(int tile, Point target){
		//Get Point representation of tile
		Point tileToMove = numberToPoint(tile);
		//Init moveSequence
		LinkedList<Move> moveSequence = new LinkedList<Move>();
		
		//Find route
		LinkedList<Move> route = AStarSearch(tileToMove, target, false);
		
		for(int i = 0; i < route.size(); i++){
			moveSequence.addAll(moveTileOnce(tile, route.get(i)));
		}
		this.blockedTiles.add(target);
		return moveSequence;
	}
	
	//solve upper and left 
	public LinkedList<Move> solveUpperAndLeft(){
		LinkedList<Move> result = new LinkedList<Move>();

		for (int i = 0; i < this.tilesPerRow - 3; i++) {
			int x = i;
			while (x < this.tilesPerRow - 2){
				result.addAll(moveTile(i*this.tilesPerRow+1+x, new Point(x,i)));
				x++;
			}
			
			
			LinkedList<Point> illegals = new LinkedList<Point>();
			illegals.add(new Point(this.tilesPerRow - 2, i));
			illegals.add(new Point(this.tilesPerRow - 2, i+1));
			illegals.add(new Point(this.tilesPerRow - 1, i));
			illegals.add(new Point(this.tilesPerRow - 1, i+1));
			
			if(illegals.contains(numberToPoint(i*this.tilesPerRow+2+x))){
				result.addAll(moveTile(i*this.tilesPerRow+2+x, new Point(this.tilesPerRow-1,i+2)));
				unBlockPoint(new Point(this.tilesPerRow-1,i+2));
			}
			
			result.addAll(moveTile(i*this.tilesPerRow+1+x, new Point(x+1,i)));
			result.addAll(moveTile(i*this.tilesPerRow+2+x, new Point(x+1,i+1)));
			unBlockPoint(new Point(x+1, i));
			result.addAll(moveTile(i*this.tilesPerRow+1+x, new Point(x,i)));
			unBlockPoint(new Point(x+1,i+1));
			result.addAll(moveTile(i*this.tilesPerRow+2+x, new Point(x+1,i)));
			
			int y = i+1;
			while (y < this.tilesPerRow - 2) {
				result.addAll(moveTile((this.tilesPerRow*y)+1+i, new Point(i,y)));
				y++;
			}
			
			illegals.clear();
			illegals.add(new Point(i, this.tilesPerRow - 2));
			illegals.add(new Point(i+1, this.tilesPerRow - 2));
			illegals.add(new Point(i, this.tilesPerRow - 1));
			illegals.add(new Point(i+1, this.tilesPerRow - 1));
			
			boolean moved2 = false;
			if(illegals.contains(numberToPoint(this.tilesPerRow*(y+1)+1+i))){
				result.addAll(moveTile(this.tilesPerRow*(y+1)+1+i, new Point(i+3, this.tilesPerRow-1)));
				moved2 = true;
			}
			
			result.addAll(moveTile((this.tilesPerRow*y)+1+i, new Point(i,y+1)));
			if (moved2) unBlockPoint(new Point(i+3, this.tilesPerRow-1));
			result.addAll(moveTile((this.tilesPerRow*(1+y))+1+i, new Point(i+1,y+1)));
			unBlockPoint(new Point(i,y+1));
			result.addAll(moveTile((this.tilesPerRow*y)+1+i, new Point(i,y)));
			unBlockPoint(new Point(i+1,y+1));
			result.addAll(moveTile((this.tilesPerRow*(1+y))+1+i, new Point(i,y+1)));
		}
		
		return result;
	}
	
	public LinkedList<Move> solveEntireBoard() {
		LinkedList<Move> movesToSolve = new LinkedList<>();
		movesToSolve.addAll(solveUpperAndLeft());
		
		Tile[][] newTileArray = new Tile[3][3];
		Point newEmptyTile = new Point(0,0);
		
		//This goes through the lover right 3x3 corner. Takes the lowest to highest numbers and gives them numbers 1-9.
		//Then it makes a new tilearray of 3x3 and but the numbers in there. This lets the clever solver figure out
		//how to solve the last 3x3 in the grid. 
		int number = tilesPerRow * (tilesPerRow - 3) + tilesPerRow - 2;
		int counter = 1;
		for(int i = 0; i < 9; i++){
			for(int y = 0; y < 3; y++){
				for(int x = 0; x < 3; x++){
					if(this.tilesList.get((tilesPerRow - 1 - x)+(tilesPerRow - 1 - y)*tilesPerRow) == number){
						newTileArray[2-x][2-y] = new Tile(counter, 0, 0);
						counter++;
						if(counter == 4){
							number = tilesPerRow * (tilesPerRow - 2) + tilesPerRow - 2;
						} else if(counter == 7){
							number = tilesPerRow * (tilesPerRow - 1) + tilesPerRow - 2;
						} else {
							number++;
						}
					} 
					if(tilesPerRow*tilesPerRow == this.tilesList.get((tilesPerRow - 1 - x)+(tilesPerRow - 1 - y)*tilesPerRow)){
						newEmptyTile = new Point(2-x,2-y);
					}
				}
			}
		}
		//Now make a new solver that only works on the last 3x3 and let it solve that part. 
		Solver lastSolver = new Solver(newTileArray, newEmptyTile);
		movesToSolve.addAll(lastSolver.AStarSearch(null, null, true));
		
		
		return movesToSolve;
	}
	
	
	
	public void unBlockPoint(Point toUnblock){
		if(this.blockedTiles.contains(toUnblock)){
			this.blockedTiles.remove(this.blockedTiles.indexOf(toUnblock));
		}
	}
	
	public void clearBlocked() {
		this.blockedTiles.clear();
	}
	
}