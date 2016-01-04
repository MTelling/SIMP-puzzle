import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class TilePane extends StackPane{
	
	private String text;
	
	public TilePane(int tileNum, double tileSize, int xPos, int yPos) {
		
		//Only make the tilePane if it is not the free spot. 
		if (tileNum != 0) {
			this.text = "" + tileNum;
			
			Label textLabel = new Label(""+ this.text);
			textLabel.setFont(new Font(25));
			this.getChildren().add(textLabel);
			this.setStyle("-fx-background-color: grey");
			this.setPrefSize(tileSize, tileSize);
			this.setLayoutX(tileSize*xPos);
			this.setLayoutY(tileSize*yPos);
		}
		
		this.setId(""+ tileNum);

		
		
	}
}
