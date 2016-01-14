import java.awt.Point;
import java.util.LinkedList;

public class Node implements Comparable<Node>, Cloneable {
	private int moves;
	private Node prevNode;
	private int manhattan;
	private LinkedList<Integer> currBoard;
	private Move moveMade;
	private Point empty;

	public Node(int moves, int manhattan, Node prevNode, LinkedList<Integer> currBoard, Move moveMade, Point empty) {
		this.moves = moves;
		this.prevNode = prevNode;
		this.currBoard = ObjectCopy.cloneList(currBoard);
		this.manhattan = manhattan;
		this.moveMade = moveMade;
		this.empty = ObjectCopy.point(empty);
	}
	
	public Node clone () {
		return new Node(this.moves, this.manhattan, this.prevNode, this.currBoard, this.moveMade, this.empty);
	}
	
	public LinkedList<Integer> getBoard() {
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

	@Override
	public int compareTo(Node otherNode) {
		if( manhattan + moves > otherNode.getManhattan() + otherNode.getMoves()) {
			return 1;
		} else if ( manhattan + moves < otherNode.getManhattan() + otherNode.getMoves()) {
			return -1;
		} else {
			return 0;
		}
	}
	


}