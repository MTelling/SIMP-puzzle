import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class TilePane extends Label{
	
	private String text;
	private Tile tile;
	
	public TilePane(Tile tile, double tileSize, int xPos, int yPos) {
		
		this.tile = tile;
		
		//Only make the tilePane if it is not the free spot. 
		if (tile.getTileNum() != 0) {
			this.text = "" + this.tile.getTileNum();
			
			this.setText(text);
			this.setId(text);
			this.setFont(new Font(25));
			this.setStyle("-fx-background-color: grey");
			this.setPrefSize(tileSize, tileSize);
			this.setAlignment(Pos.CENTER);
			this.setLayoutX(tileSize*xPos);
			this.setLayoutY(tileSize*yPos);

		}
		
		this.setId(""+ tile.getTileNum());
		
	}
}
