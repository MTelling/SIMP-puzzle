import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private int boardSize;
	private int[][] tiles;
	private Point emptyTile;
	private int tileSize;
	
	public Board(int boardSize) {
		if(boardSize >= 3 && boardSize <= 100) {
			this.boardSize = boardSize;
			this.tiles = new int[boardSize][boardSize];
			this.tileSize = ((Window.WINDOW_WIDTH - (Window.GAME_BORDER * 2) - 2 * Window.BOARD_BORDER_SIZE) / (boardSize));
		} else {
			throw new IllegalArgumentException("Invalid board size");
		}
	}
	
	public boolean isGameOver(){		
		if( this.tiles[boardSize - 1][boardSize - 1] == Math.pow(boardSize, 2) ){
			int counter = 1;
			for(int y = 0; y < boardSize; y++){
				for(int x = 0; x < boardSize; x++){
					if( !(tiles[x][y] == counter) ){
						return false;
					}
					counter++;
				}
			}
			return true;
		}
		return false;
	}
	
	public void init() {
		this.tiles[0][0] = 2;
		this.tiles[1][0] = 3;
		this.tiles[2][0] = 1;
		
		int tileCount = 4;
		for(int y = 0; y < this.boardSize; y++) {
			for(int x = 0; x < this.boardSize; x++) {
				if((y == 0 && x > 2) || y > 0) {
					this.tiles[x][y] = tileCount;
					tileCount++;
				}
			}
		}
		
		emptyTile = new Point(this.boardSize - 1 , this.boardSize - 1);
	}
	
	
	//TOOD: doesn't work anymore because we inverted the controls. 
	/*public void moveTile(int x, int y) {
		if(x == emptyTile.x - 1 && y == emptyTile.y)
			moveTile(KeyEvent.VK_LEFT);
		else if(x == emptyTile.x + 1 && y == emptyTile.y)
			moveTile(KeyEvent.VK_RIGHT);
		else if(x == emptyTile.x && y == emptyTile.y - 1)
			moveTile(KeyEvent.VK_UP);
		else if(x == emptyTile.x && y == emptyTile.y + 1)
			moveTile(KeyEvent.VK_DOWN);
	}*/

	
	//TODO: Tobias can this be done more simple? Maybe we should just set dx dy in the switch case and then after, do the entire code? 
	//Should be pretty easy doable. Probably could eliminate some redundancy? 
	public void moveTile(int dx, int dy) {
		this.tiles[emptyTile.x][emptyTile.y] = this.tiles[emptyTile.x + dx][emptyTile.y + dy];
		this.tiles[emptyTile.x + dx][emptyTile.y + dy] = (int) Math.pow(boardSize, 2);
		emptyTile.x += dx;
		emptyTile.y += dy;
	}
	
	//Method to determine if a move should be made. 
	public boolean isMoveValid(int dx, int dy) {
		boolean shouldMove = false;
		if (dx == -1) { //Right arrow
			shouldMove = (emptyTile.x > 0)? true:false;
		} else if (dx == 1) { //Left arrow
			shouldMove = (emptyTile.x < this.boardSize - 1)? true:false;
		} else if (dy == -1) { //Down arrow
			shouldMove = (emptyTile.y > 0)? true:false;
		} else if (dy == 1) { //Up arrow
			shouldMove = (emptyTile.y < this.boardSize - 1)? true:false;
		}
		return shouldMove;
	}
	
	
	public void setTiles(int[][] tiles) {
		int[][] newArray = new int[tiles.length][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = Arrays.copyOf(tiles[i], tiles[i].length);
		}
		this.tiles = newArray;
	}
	
	public Point getEmptyTile() {
		return emptyTile;
	}
	
	public void setEmptyTile(Point newEmptyTile) {
		this.emptyTile = new Point(newEmptyTile.x, newEmptyTile.y);
	}
	

	public int[][] getTiles() {
		return this.tiles;
	}
	
	public int getBoardSize() {
		return this.boardSize;
	}

	public int getTileSize() {
		return this.tileSize;
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				str += this.tiles[i][j] + " ";
			}
			str += "\n";
		}
		
		return str;
	}
	
}
