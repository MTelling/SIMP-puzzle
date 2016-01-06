import java.awt.event.KeyEvent;

public class Board {
	
	
	private int boardSize;
	private int[][] tiles;
	private int emptyX, emptyY;
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
		
		emptyX = emptyY = this.boardSize - 1;
	}
	
	
	//TOOD: doesn't work anymore because we inverted the controls. 
	public boolean moveTile(int x, int y) {
		if(x == emptyX - 1 && y == emptyY)
			return moveTile(KeyEvent.VK_LEFT);
		else if(x == emptyX + 1 && y == emptyY)
			return moveTile(KeyEvent.VK_RIGHT);
		else if(x == emptyX && y == emptyY - 1)
			return moveTile(KeyEvent.VK_UP);
		else if(x == emptyX && y == emptyY + 1)
			return moveTile(KeyEvent.VK_DOWN);
		else
			return false;
	}
	
	
	//TODO: Tobias can this be done more simple? 
	public boolean moveTile(int keyCode) {
		switch(keyCode) {
		case KeyEvent.VK_RIGHT:
			if(emptyX > 0) {
				this.tiles[emptyX][emptyY] = this.tiles[emptyX - 1][emptyY];
				this.tiles[emptyX - 1][emptyY] = (int) Math.pow(boardSize, 2);
				emptyX--;
				return true;
			} else {
				return false;
			}
		case KeyEvent.VK_LEFT:
			if(emptyX < this.boardSize - 1) {
				this.tiles[emptyX][emptyY] = this.tiles[emptyX + 1][emptyY];
				this.tiles[emptyX + 1][emptyY] = (int) Math.pow(boardSize, 2);
				emptyX++;
				return true;
			} else {
				return false;
			}
		case KeyEvent.VK_DOWN:
			if(emptyY > 0) {
				this.tiles[emptyX][emptyY] = this.tiles[emptyX][emptyY - 1];
				this.tiles[emptyX][emptyY - 1] = (int) Math.pow(boardSize, 2);
				emptyY--;
				return true;
			} else {
				return false;
			}
		case KeyEvent.VK_UP:
			if(emptyY < this.boardSize - 1) {
				this.tiles[emptyX][emptyY] = this.tiles[emptyX][emptyY + 1];
				this.tiles[emptyX][emptyY + 1] = (int) Math.pow(boardSize, 2);
				emptyY++;
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
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
}
