import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BoardPane extends Pane {

	public BoardPane(double height, double width) {
		//Set minimum height and width + the starting width and height for the boardPane. 
		this.setMinHeight(height);
		this.setMinWidth(width);
		this.setHeight(height);
		this.setWidth(width);
		
		
		//Set background color
		this.setStyle("-fx-background-color: grey");
		
		createTiles(5);
		
	}
	
	public void createTiles(int size) {
		ArrayList<StackPane> tiles = new ArrayList<>();
		double tileSize = this.getHeight() / size;
		System.out.println(tileSize);
		
		int i = 0;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				tiles.add(new StackPane());
				tiles.get(i).setPrefHeight(tileSize);
				tiles.get(i).setPrefWidth(tileSize);
				tiles.get(i).setLayoutX(x*tileSize);
				tiles.get(i).setLayoutY(y*tileSize);
				
				tiles.get(i).getChildren().add(new Label(""+ i));

				this.getChildren().add(tiles.get(i));
				
				i++;
			}
		}
		
		
	}
}
