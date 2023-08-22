package camera;

import java.util.ArrayList;
import javafx.scene.layout.Pane;

public class CameraPane extends Pane {

	private double shiftX;
	private double shiftY;
	private double zoom;

	private final ArrayList<CameraNode> cameraElements = new ArrayList<>();

	public CameraPane(CameraNode... cNodes) {
		super();
		addElement(cNodes);
	}

	public final void addElement(CameraNode... cNodes) {
		cameraElements.addAll(cameraElements);
		getChildren().addAll(CameraNode.extractNodes(cNodes));
	}

	private void callibrateDisplay() {
		cameraElements.forEach(e -> e.callibrateDisplay(shiftX, shiftY, zoom));
	}

	public void pan(double dx, double dy) {
		fitBackstage();
	}

	public void zoom(double dScroll) {
		fitBackstage();
	}

	private void fitBackstage() {
		try {
			fitPanning();
		} catch (FitException fep) {
			try {
				fitZooming();
			} catch (FitException fez) {
				System.err.println("ERROR: Could not fit CameraPane backstage.");
				System.exit(2);
			}
		}
		callibrateDisplay();
	}

	private void fitPanning() throws FitException {

	}

	private void fitZooming() throws FitException {

	}

	private static class FitException extends Exception {
	}

}
