import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>, Cloneable {
	private int moves;
	private Node prevNode;
	private int manhattan;
	private List<Integer> currBoard;
	private Move moveMade;
	private Point empty;
	private int hamming;

	public Node(int moves, Node prevNode, List<Integer> currBoard, Move moveMade, Point empty) {
		this.moves = moves;
		this.prevNode = prevNode;
		this.currBoard = ObjectCopy.cloneList(currBoard);
		this.manhattan = getManhattanDistance(this.currBoard);
		this.moveMade = moveMade;
		this.empty = ObjectCopy.point(empty);
	}
	
	public Node clone () {
		return new Node(this.moves, this.prevNode, this.currBoard, this.moveMade, this.empty);
	}
	
	public List<Integer> getBoard() {
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
	public int getManhattanDistance(List<Integer> currentTiles) {
		int size = (int) Math.sqrt(currBoard.size());
		int sum = 0;
		int hamming = 0;
		int count = 1;
		int[] xRow = new int[size + 1];
		int[][] yRows = new int[size + 1][size + 1];
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
					
					
					if(currentNumber != count) {
						hamming++;
					}
					
					if(containsHigher(xRow, currentNumber)
							|| containsHigher(yRows[x], currentNumber)) {
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