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
		MouseCameraPane mPane = new MouseCameraPane(1000, 700, 750, 500, 500, 350);
		CameraNode n1 = new CameraNode(new Rectangle(1000, 700, Color.ORANGE), 0, 0);
		CameraNode n2 = new CameraNode(new Rectangle(750, 500, Color.BLUE), 0, 0);
		CameraNode n3 = new CameraNode(new Rectangle(100, 200, Color.GREEN), 0, 0);
		n1.setIntrinsicCenter(500, 350);
		n2.setIntrinsicCenter(500, 350);
		n3.setIntrinsicCenter(500, 350);
		mPane.addElement(n1, n2, n3);

		primaryStage.setScene(new Scene(mPane));
		primaryStage.setTitle("Testing");
		primaryStage.show();
		primaryStage.widthProperty().addListener(l -> mPane.setPrefWidth(primaryStage.widthProperty().get()));
		primaryStage.heightProperty().addListener(l -> mPane.setPrefHeight(primaryStage.heightProperty().get()));
	}
}
