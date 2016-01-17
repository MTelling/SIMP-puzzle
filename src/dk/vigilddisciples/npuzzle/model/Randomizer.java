package dk.vigilddisciples.npuzzle.model;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import dk.vigilddisciples.npuzzle.ObjectCopy;
import dk.vigilddisciples.npuzzle.NPuzzle;

public class Randomizer {
	private Point emptyTile;
	private int tilesPerRow;
	private Board board;

	public Randomizer(Board board) {
		this.board = board;
		this.emptyTile = ObjectCopy.point(board.getCurrEmptyTile());
		this.tilesPerRow = board.getTilesPerRow();
	}

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

				if (board.isMoveValid(xMove, emptyTile) && !justMovedX) {
					makePseudoMove(moveSequence, xMove, emptyTile);
					justMovedX = true;
					hasNotMoved = false;
				} else if (board.isMoveValid(yMove, emptyTile) && justMovedX ) {
					makePseudoMove(moveSequence, yMove, emptyTile);

					justMovedX = false;
					hasNotMoved = false;
				}
			}
		}
	}

	//Returns a list of movements required to randomize the current board. 
	public LinkedList<Move> generateRandomizingMoveSequence() {
		LinkedList<Move> moveSequence = new LinkedList<Move>();
		Point empty = emptyTile;

		int difficulty = NPuzzle.getSettings().getDifficulty();
		//If the board is larger than 8, the difficulty shouldn't be linear. Only if difficulty is easy. 
		if  (tilesPerRow > 8 && difficulty != Difficulty.EASY.getValue()) {
			difficulty += 1 +  (tilesPerRow % 10);
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
}
