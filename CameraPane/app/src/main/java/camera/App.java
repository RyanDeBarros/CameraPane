package camera;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		MouseCameraPane mPane = new MouseCameraPane(1000, 700, 750, 500, 500, 380);
		CameraNode n1 = new CameraNode(new Rectangle(1000, 700, Color.ORANGE), 0, 0);
		CameraNode n2 = new CameraNode(new Rectangle(750, 500, Color.BLUE), 0, 0);
		n1.setNodeCenter(500, 350);
		n2.setNodeCenter(500, 350);
		mPane.addElement(n1, n2);

		primaryStage.setScene(new Scene(mPane));
		primaryStage.setTitle("Testing");
		primaryStage.show();
	}
}
