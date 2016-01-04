import java.util.ArrayList;

import javafx.scene.layout.Pane;

public class BoardPane extends Pane {
	
	public static final double BOARD_MARGIN = 25;
	private double boardSize;
	private Board board;
	private Pane boardContainer;
	
	public BoardPane(double size) {
		
		this.boardSize = size;
		this.board = new Board(10);
		board.init();
		
		
		//Set minimum height and width + the starting width and height for the boardPane. 
		this.setMinHeight(size+BOARD_MARGIN*2);
		this.setMinWidth(size+BOARD_MARGIN*2);
		this.setHeight(size+BOARD_MARGIN*2);
		this.setWidth(size+BOARD_MARGIN*2);
		
		//Set color. 
		this.setStyle("-fx-background-color: aliceblue");
		
		
		//Make a container for the board. 
		this.boardContainer = new Pane();
		
		this.getChildren().add(boardContainer);
		boardContainer.setLayoutX(BOARD_MARGIN);
		boardContainer.setLayoutY(BOARD_MARGIN);
		boardContainer.setStyle("-fx-background-color: lightgrey");
		
		createTiles();
		
	}
	
	public void createTiles() {
		double tileSize = this.boardSize / this.board.getGridSize();

		ArrayList<TilePane> tilePanes = new ArrayList<>();
		
		int i = 0;
		for (int y = 0; y < this.board.getGridSize(); y++) {
			for (int x = 0; x < this.board.getGridSize(); x++) {
				
				
				//Find the tileNumber for the current position.
				//If the current position is the free spot, a nullpointer exception is thrown.
				//In this case do nothing and let the tileNum be 0.
				int tileNum = 0;
				try {
					tileNum = this.board.getGrid()[x][y].getTileNum();
				} catch (NullPointerException e) {
					//Do nothing
				}
				
				tilePanes.add(new TilePane(tileNum, tileSize, x, y));
				boardContainer.getChildren().add(tilePanes.get(i));
				
				i++;
				
			}
		}
		
		
	}
}
