import java.util.ArrayList;

import javafx.scene.layout.Pane;

public class BoardPane extends Pane {
	
	public static final double BOARD_MARGIN = 25;
	private double boardSize;
	private Board board;
	private Pane boardContainer;
	
	public BoardPane(double size) {
		System.out.println("Got here");

		this.board = new Board(3);
		
		
		//Set minimum height and width + the starting width and height for the boardPane. 
		this.setMinHeight(size+BOARD_MARGIN*2);
		this.setMinWidth(size+BOARD_MARGIN*2);
		this.setHeight(size+BOARD_MARGIN*2);
		this.setWidth(size+BOARD_MARGIN*2);
		
		//Set color. 
		this.setStyle("-fx-background-color: red");
		
		
		//Make a container for the board. 
		this.boardContainer = new Pane();
		
		this.getChildren().add(boardContainer);
		boardContainer.setLayoutX(BOARD_MARGIN);
		boardContainer.setLayoutY(BOARD_MARGIN);
		
		
		System.out.println("got here as well");
		createTiles();
		
	}
	
	public void createTiles() {
		double tileSize = this.boardSize / this.board.gridSize;
		ArrayList<TilePane> tilePanes = new ArrayList<>();
		
		int i = 0;
		for (int y = 0; y < this.board.gridSize; y++) {
			for (int x = 0; x < this.board.gridSize; x++) {
				tilePanes.add(new TilePane(this.board.getGrid()[x][y].getTileNum(), tileSize, x, y));
				this.getChildren().add(tilePanes.get(i));
				
				i++;
				
			}
		}
		
		
	}
}
