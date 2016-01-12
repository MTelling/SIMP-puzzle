import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {

	// Helper method to get valid moves
	public Move[] legalMovies (Point emptyTile) {
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
		return moves.toArray();
	}

	// Helper method to backtrack moves
	public ArrayList<Move> backtrackMoves(Node node) {
		ArrayList<Move> moves = new ArrayList<Move>();
		if(node.prevNode = null) {
			moves.add(node.getLastMove());
		} else {
			moves.addAll(backtrackMoves(node.prevNode));
		}
		return moves;
	}

	public ArrayList<Move> Solve (int[][] currTiles, Point emptyTile) {


		int[][] board = currTiles;
		Point empty = emptyTile;
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		Node first = Node(0 , null , currTiles, null);
		
		
		queue.add(first);

		while(queue.peek() != null) {
			// Get node with highest priority
			Node current = queue.poll();
			
			// Check if we have a goal board yet.
			if(current.getManhattan != 0) {
				for( Move move : legalMoves(emptyTile) ) {

					Point nextEmpty = ObjectCopy.point(emptyTile);
					nextEmpty.translate(move.dx, move.dy);
					
					// Save current numbers
					int temp = board[empty.x][empty.y];
					int tempNext = board[nextEmpty.x][nextEmpty.y];

					// Make a move
					board[empty.x][empty.y] = board[nextEmpty.x][nextEmpty.y];
					board[nextEmpty.x][nextEmpty.y] = temp;
		
					// Put a new Node in queue, with that move
					queue.add(current.getMoves() + 1, current, board, move);

					// Reverse the move and be ready for the next move
					board[nextEmpty.x][nextEmpty.y] = tempNext;
					board[empty.x][empty.y] = temp;
				}
			} else {
				return Collections.reverse(backtrackMoves(current));
			}
		}
	}








	
}
