import java.awt.Point;
import java.util.ArrayList;

public class Node implements Comparable<Node>, Cloneable {
	private int moves;
	private Node prevNode;
	private int manhattan;
	private Tile[][] currBoard;
	private Move moveMade;
	private Point empty;
	private int hamming;

	public Node(int moves, Node prevNode, Tile[][] currBoard, Move moveMade, Point empty) {
		this.moves = moves;
		this.prevNode = prevNode;
		this.currBoard = ObjectCopy.tile2D(currBoard);
		this.manhattan = getManhattanDistance(this.currBoard);
		this.moveMade = moveMade;
		this.empty = ObjectCopy.point(empty);
	}
	
	public Node clone () {
		return new Node(this.moves, this.prevNode, this.currBoard, this.moveMade, this.empty);
	}
	
	public Tile[][] getBoard() {
		return currBoard;
	}
	
	public Point getEmpty() {
		return empty;
	}

	public Node getPrevNode() {
		return prevNode;
	}

	public int getMoves() {
		return moves;
	}
	
	public Move getLastMove() {
		return moveMade;
	}

	public int getManhattan() {
		return manhattan;
	}
	
	public int getHamming() {
		return hamming;
	}

	@Override
	public int compareTo(Node otherNode) {
		if( manhattan + moves > otherNode.getManhattan() + otherNode.getMoves()
				&& hamming + moves > otherNode.getHamming() + otherNode.getMoves()) {
			return 1;
		} else if ( manhattan + moves < otherNode.getManhattan() + otherNode.getMoves() 
				&& hamming + moves < otherNode.getHamming() + otherNode.getMoves()) {
			return -1;
		} else if (manhattan + moves > otherNode.getManhattan() + otherNode.getMoves()) {
			return 1;
		} else if (manhattan + moves < otherNode.getManhattan() + otherNode.getMoves()) {
			return -1;
		} else if (hamming + moves > otherNode.getHamming() + otherNode.getMoves()) {
			return 1;
		} else if (hamming + moves < otherNode.getHamming() + otherNode.getMoves()) {
			return -1;
		} else {
			return 0;
		}
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
	public int getManhattanDistance(Tile[][] currentTiles) {
		int size = currBoard.length;
		int sum = 0;
		int hamming = 0;
		int count = 1;
		int[] xRow = new int[size + 1];
		int[][] yRows = new int[size + 1][size + 1];
		//Go through the board.
		for(int y = 0; y < size; y++) {
				
			
			for(int x = 0; x < size; x++) {
				// Don't save distance for the empty tile!
				if(currentTiles[x][y].getNumber() != (size * size)){

					
					int targetX = (currBoard[x][y].getNumber() - 1) % size;
					int targetY = (currBoard[x][y].getNumber() - 1) / size;
					int xDistance = Math.abs(x - targetX);
					int yDistance = Math.abs(y - targetY);
					
					if(targetY == y) {
						xRow[x]= currentTiles[x][y].getNumber();
					}
					if(targetX == x) {
						yRows[x][y] = currentTiles[x][y].getNumber();
					}
					
					
					if(currentTiles[x][y].getNumber() != count) {
						hamming++;
					}
					
					if(containsHigher(xRow, currentTiles[x][y].getNumber())
							|| containsHigher(yRows[x], currentTiles[x][y].getNumber())) {
						sum += 2;
					}
					
					sum += xDistance + yDistance;
				}
				count++;
			}
		}
		this.hamming = hamming;
		return sum;
	}

}