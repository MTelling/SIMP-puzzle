import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Board {
	
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
	public void moveTile(int x, int y) {
		if(x == emptyTile.x - 1 && y == emptyTile.y)
			moveTile(KeyEvent.VK_LEFT);
		else if(x == emptyTile.x + 1 && y == emptyTile.y)
			moveTile(KeyEvent.VK_RIGHT);
		else if(x == emptyTile.x && y == emptyTile.y - 1)
			moveTile(KeyEvent.VK_UP);
		else if(x == emptyTile.x && y == emptyTile.y + 1)
			moveTile(KeyEvent.VK_DOWN);
	}

	
	//TODO: Tobias can this be done more simple? 
	public void moveTile(int keyCode) {
		System.out.println("Got here");
		switch(keyCode) {
			case KeyEvent.VK_RIGHT:
				if(emptyTile.x > 0) {
					this.tiles[emptyTile.x][emptyTile.y] = this.tiles[emptyTile.x - 1][emptyTile.y];
					this.tiles[emptyTile.x - 1][emptyTile.y] = (int) Math.pow(boardSize, 2);
					emptyTile.x--;
				} 
				break;
			case KeyEvent.VK_LEFT:
				if(emptyTile.x < this.boardSize - 1) {
					this.tiles[emptyTile.x][emptyTile.y] = this.tiles[emptyTile.x + 1][emptyTile.y];
					this.tiles[emptyTile.x + 1][emptyTile.y] = (int) Math.pow(boardSize, 2);
					emptyTile.x++;
				}
				break;
			case KeyEvent.VK_DOWN:
				if(emptyTile.y > 0) {
					this.tiles[emptyTile.x][emptyTile.y] = this.tiles[emptyTile.x][emptyTile.y - 1];
					this.tiles[emptyTile.x][emptyTile.y - 1] = (int) Math.pow(boardSize, 2);
					emptyTile.y--;
				}
				break;
			case KeyEvent.VK_UP:
				if(emptyTile.y < this.boardSize - 1) {
					this.tiles[emptyTile.x][emptyTile.y] = this.tiles[emptyTile.x][emptyTile.y + 1];
					this.tiles[emptyTile.x][emptyTile.y + 1] = (int) Math.pow(boardSize, 2);
					emptyTile.y++;
				} 
				break;
			default: break;
		}
	}
	
	//Method to determine if a move should be made. 
	public boolean shouldMove(int keyCode) {
		boolean shouldMove = false;
		switch(keyCode) {
			case KeyEvent.VK_RIGHT:
				shouldMove = (emptyTile.x > 0)? true:false;
				break;
			case KeyEvent.VK_LEFT:
				shouldMove = (emptyTile.x < this.boardSize - 1)? true:false;
				break;
			case KeyEvent.VK_DOWN:
				shouldMove = (emptyTile.y > 0)? true:false;
				break;
			case KeyEvent.VK_UP:
				shouldMove = (emptyTile.y < this.boardSize - 1)? true:false;
				break;
			default: break;
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
