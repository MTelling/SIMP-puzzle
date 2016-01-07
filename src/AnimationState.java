import java.awt.Point;

public class AnimationState {
	private Point[][] tileCoords;
	private int[][] currTiles;
	private int[][] newTiles;
	private Point currEmptyTile;
	private Point newEmptyTile;
	private int tileSize;
	private int boardSize;
	private final int VELOCITY = 4;
	
	
	public AnimationState(Point emptyTile, int[][] tiles, int tileSize, int boardSize) {
		
		this.resetState(emptyTile, tiles);
		
		this.tileSize = tileSize;
		this.boardSize = boardSize;
		
		this.tileCoords = new Point[boardSize][boardSize];
		
		this.calcCoords();
	}
	
	//Calculates tiles X and Y coordinates and puts them into the tilePositionCoords array. This updates according to this.tiles. 
	public void calcCoords() {
		for(int y = 0; y < this.newTiles.length; y++) {
			for(int x = 0; x < this.newTiles.length; x++) {
				if(this.newTiles[x][y] != Math.pow(this.boardSize,2)) {
					int xPos = Window.GAME_BORDER + Window.BOARD_BORDER_SIZE + (x * this.tileSize);
					//Y position is gotten from the bottom and then up. This way it will always have exactly distance to bottom if the top is changed. 
					int yPos = Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.boardSize - y) * (this.tileSize)) - Window.BOARD_BORDER_SIZE;
					
					this.tileCoords[x][y] = new Point(xPos, yPos);
				} else {
					this.tileCoords[x][y] = new Point(0, 0);

				}
			}
		}
		
	}
	
	public void resetState(Point emptyTile, int[][] tiles) {
		this.currTiles = ObjectCopy.array2D(tiles);
		this.currEmptyTile = ObjectCopy.point(emptyTile);
		
		this.setNew(emptyTile, tiles);
	}
	
	public void setNew(Point emptyTile, int[][] tiles) {
		this.newTiles = ObjectCopy.array2D(tiles);
		this.newEmptyTile = ObjectCopy.point(emptyTile);
	}
	
	//Returns true if arrived at final coord. 
	public boolean calcMovingCoords(){
		boolean arrivedAtFinal = false;
		for(int y = 0; y < this.newTiles.length; y++) {
			for(int x = 0; x < this.newTiles.length; x++) {
				if(this.currTiles[x][y] != Math.pow(this.boardSize,2) && !this.currEmptyTile.equals(this.newEmptyTile) && this.newTiles[x][y] != this.currTiles[x][y]) {
					int dx = 0, dy = 0;
					
					//Find out if the tile has moved up or down.
					if (this.currEmptyTile.x - 1 == this.newEmptyTile.x) { //Tile has moved to the right
						dx = VELOCITY;
					} else if (this.currEmptyTile.x + 1 == this.newEmptyTile.x) { //Tile has moved to the left
						dx = -VELOCITY;
					} else if (this.currEmptyTile.y - 1 == this.newEmptyTile.y) { //Tile has moved down
						dy = VELOCITY;
					} else if (this.currEmptyTile.y + 1 == this.newEmptyTile.y) { //Tile has moved up
						dy = -VELOCITY;
					}
					this.tileCoords[x][y] = new Point(this.tileCoords[x][y].x + dx, this.tileCoords[x][y].y + dy);
					
					boolean atFinalPosition = true;
					//Check if the tile is now at the final position.
					int finalX = Window.GAME_BORDER + Window.BOARD_BORDER_SIZE + (this.currEmptyTile.x * this.tileSize);
					int finalY = Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.boardSize - this.currEmptyTile.y) * (this.tileSize)) - Window.BOARD_BORDER_SIZE;
					if (dx > 0 && this.tileCoords[x][y].x >= finalX ){
					} else if (dx < 0 && this.tileCoords[x][y].x <= finalX) {
					} else if (dy > 0 && this.tileCoords[x][y].y >= finalY) {
					} else if (dy < 0 && this.tileCoords[x][y].y <= finalY) {
					} else {
						atFinalPosition = false;
					}
					
					if (atFinalPosition) {
						this.currEmptyTile = new Point(this.newEmptyTile.x, this.newEmptyTile.y);
						
						this.resetState(newEmptyTile, newTiles);
						this.calcCoords();
						arrivedAtFinal = true;
					}
					
				}
			}
		}
		
		return arrivedAtFinal;
	}
	
	 
	public Point[][] getTileCoords() {
		return this.tileCoords;
	}
	
	public int[][] getCurrTiles() {
		return this.currTiles;
	}
}
