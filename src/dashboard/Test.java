package dashboard;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test {

	public Test(Stage primaryStage) {
		Label label = new Label("Test");
		BorderPane bp = new BorderPane();
		bp.setCenter(label);
		primaryStage.setScene(new Scene(bp));
	}

}
