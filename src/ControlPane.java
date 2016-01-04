import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ControlPane extends Pane {
	public ControlPane(double height, double width) {
		//Set height and set width. The width only has to set the minimal width, because it should be resizeable. 
		this.setHeight(height);
		this.setMaxHeight(height);
		this.setMinHeight(height);
		this.setWidth(width);
		this.setMinWidth(width);
		
		
		this.getChildren().add(menuButton());
	}
	
	
	public Button menuButton() {
		Button menuButton = new Button("Menu");
		
		return menuButton;
	}
	
}
