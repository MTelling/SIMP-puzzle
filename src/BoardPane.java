import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardPane extends Pane {
	
	public static final double BOARD_MARGIN = 25;
	private double boardSize;
	
	public BoardPane(double height, double width) {
		
		this.boardSize = height;
		//Set minimum height and width + the starting width and height for the boardPane. 
		this.setMinHeight(height+BOARD_MARGIN*2);
		this.setMinWidth(width+BOARD_MARGIN*2);
		this.setHeight(height+BOARD_MARGIN*2);
		this.setWidth(width+BOARD_MARGIN*2);
		
		
		//Set background color
		this.setStyle("-fx-background-color: grey");
		
		
		//Rectangle for margin
		Rectangle margin = new Rectangle(height+(BOARD_MARGIN*2), width+(BOARD_MARGIN*2));
		this.getChildren().add(margin);
		margin.setFill(Color.RED);
		margin.toBack();
		
		
		createTiles(5);
		
	}
	
	public void createTiles(int size) {
		ArrayList<StackPane> tiles = new ArrayList<>();
		double tileSize = this.boardSize / size;
		
		int i = 0;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				tiles.add(new StackPane());
				tiles.get(i).setPrefHeight(tileSize);
				tiles.get(i).setPrefWidth(tileSize);
				tiles.get(i).setLayoutX(x*tileSize+BOARD_MARGIN);
				tiles.get(i).setLayoutY(y*tileSize+BOARD_MARGIN);
				tiles.get(i).setStyle("-fx-background-color: white");
				
				tiles.get(i).getChildren().add(new Label(""+ i));

				this.getChildren().add(tiles.get(i));
				
				i++;
			}
		}
		
		
	}
}
