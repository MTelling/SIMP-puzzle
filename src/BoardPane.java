import java.awt.Point;
import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BoardPane extends Pane {
	
	public static final double BOARD_MARGIN = 25;
	private double boardSize;
	private Board board;
	private Pane boardContainer;
	private double tileSize;

	
	public BoardPane(double size) {
		
		this.boardSize = size;
		
		//Init board. 
		this.board = new Board(4);
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
		
		this.tileSize = this.boardSize / this.board.getGridSize();
		
		createTiles();
	
		boardContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				int xPosClicked = (int)(event.getX() / tileSize);
				int yPosClicked = (int)(event.getY() / tileSize);
				
				Point newPosition = new Point(board.getEmptySpot().x, board.getEmptySpot().y);

				if (board.moveTile(xPosClicked, yPosClicked)) {
					String clickedTile = "" + board.getGrid()[xPosClicked][yPosClicked].getTileNum();
					Node selectPane = getChildren().get(0).lookup("#" + clickedTile);
					TranslateTransition translateTile = new TranslateTransition(Duration.millis(1000), selectPane);
					translateTile.setByX(tileSize*(newPosition.x-xPosClicked));
					translateTile.setByY(tileSize*(newPosition.y-yPosClicked));
					translateTile.play();
					
					
				}
				
			}
		});
	}
	
	public void createTiles() {
		Tile tile;
		ArrayList<TilePane> tilePanes = new ArrayList<>();
		
		int i = 0;
		for (int y = 0; y < this.board.getGridSize(); y++) {
			for (int x = 0; x < this.board.getGridSize(); x++) {
				
				
				//Find the tileNumber for the current position.
				//If the current position is the free spot, a nullpointer exception is thrown.
				//In this case do nothing and let the tileNum be 0.

				if (this.board.getGrid()[x][y] != null) {
					tile = this.board.getGrid()[x][y];
				} else {
					tile = new Tile(0);
				}
				
				tilePanes.add(new TilePane(tile, tileSize, x, y));
				boardContainer.getChildren().add(tilePanes.get(i));
				
				i++;
				
			}
		}
		
		
	}
}
