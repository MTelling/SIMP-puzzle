package dk.vigilddisciples.npuzzle.model;

import java.awt.Point;
import java.util.LinkedList;

public class SearchNode implements Comparable<SearchNode>{
	private int moves;
	private int heuristic;
	private LinkedList<Integer> tilesList;
	private SearchNode prevNode;
	private Move lastMove;
	private Point empty;
	
	public SearchNode(int moves, int heuristic, SearchNode prevNode, LinkedList<Integer> tilesList, Move lastMove, Point empty) {
		this.moves = moves;
		this.heuristic = heuristic;
		this.prevNode = prevNode;
		this.tilesList = tilesList;
		this.lastMove = lastMove;
		if(empty != null) {this.empty = new Point(empty.x,empty.y);}
	}
	
	@Override
	public int compareTo(SearchNode other){
		if(this.heuristic + this.moves > other.getHeuristic() + other.getMoves()){
			return 1;
		}else if(this.heuristic + this.moves < other.getHeuristic() + other.getMoves()){
			return -1;
		}else{
			return 0;
		}
	}
	
	/////// GETTERS FROM HERE ///////
	
	public LinkedList<Integer> getTiles(){
		return this.tilesList;
	}
	
	public Point getEmpty(){
		return this.empty;
	}
	
	public SearchNode getPrevNode(){
		return this.prevNode;
	}
	
	public int getMoves(){
		return this.moves;
	}
	
	public Move getLastMove(){
		return this.lastMove;
	}
	
	public int getHeuristic(){
		return this.heuristic;
	}
}