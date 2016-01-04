import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TilePane extends StackPane{
	
	private String text;
	
	public TilePane(int tileNum, double tileSize, int xPos, int yPos) {
		
		this.text = "" + tileNum;
		
		Label textLabel = new Label(""+ this.text);
		
		this.getChildren().add(textLabel);
		
		this.setPrefSize(tileSize, tileSize);
		this.setLayoutX(tileSize*xPos);
		this.setLayoutY(tileSize*yPos);
		
		
	}
}
