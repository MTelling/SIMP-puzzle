import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class TilePane extends StackPane{
	
	private String text;
	private Tile tile;
	
	public TilePane(Tile tile, double tileSize, int xPos, int yPos) {
		
		this.tile = tile;
		
		//Only make the tilePane if it is not the free spot. 
		if (tile.getTileNum() != 0) {
			this.text = "" + this.tile.getTileNum();
			
			Label textLabel = new Label(""+ this.text);
			textLabel.setFont(new Font(25));
			this.getChildren().add(textLabel);
			this.setStyle("-fx-background-color: grey");
			this.setPrefSize(tileSize, tileSize);
			this.setLayoutX(tileSize*xPos);
			this.setLayoutY(tileSize*yPos);
		}
		
		this.setId(""+ tile.getTileNum());

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println(getId());
				
			}
			
		});
		
	}
}
