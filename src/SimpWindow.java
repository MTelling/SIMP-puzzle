import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SimpWindow extends Application
{
	
	private static final double CONTROLPANEL_SIZE = 50;
	private static final double MIN_WINDOW_WIDTH = 400;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("SIMP-puzzle");
		
		//Create root
		Group root = new Group();
		
		
		//Set the minimum size of the window. 
		/*primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
		primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
		primaryStage.setWidth(MIN_WINDOW_WIDTH);
		*/
		
		//Create boardPane
		Pane boardPane = new BoardPane(MIN_WINDOW_WIDTH);
		boardPane.setLayoutY(CONTROLPANEL_SIZE);
		
		//Create controlPane
		Pane controlPane = new ControlPane(CONTROLPANEL_SIZE, MIN_WINDOW_WIDTH);
		controlPane.setLayoutY(0);
		
		//Add all panes to root. 
		root.getChildren().addAll(boardPane, controlPane);
		
		
		
		//Set stage and show. 
		primaryStage.sizeToScene();
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
