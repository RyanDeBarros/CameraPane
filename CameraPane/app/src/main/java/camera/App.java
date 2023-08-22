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
		MouseCameraPane mPane = new MouseCameraPane(1000, 700, 500, 350);
		Rectangle r1 = new Rectangle(600, 250, Color.ORANGE);
		Rectangle r2 = new Rectangle(200, 100, Color.BLUE);
		mPane.addElement(new CameraNode(r1, 0, 0), new CameraNode(r2, 0, 0));

		primaryStage.setScene(new Scene(mPane));
		primaryStage.setTitle("Testing");
		primaryStage.show();
	}
}
