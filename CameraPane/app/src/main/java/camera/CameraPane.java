package camera;

import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class CameraPane extends Pane {

	private double shiftX;
	private double shiftY;
	private double zoom;
	@Setter
	private double maxZoom;

	@Getter
	private final SimpleObjectProperty<Double> scrollSensitivity = new SimpleObjectProperty<>(1.00696);
	private static final double minimumSensitivity = 1.000001;

	private final SimpleObjectProperty<Double> intrinsicBackstageWidth;
	private final SimpleObjectProperty<Double> intrinsicBackstageHeight;

	private final ArrayList<CameraNode> cameraElements = new ArrayList<>();

	public CameraPane(double intrinsicBackstageWidth, double intrinsicBackstageHeight, double prefWidth, double prefHeight, CameraNode... cNodes) {
		super();
		if (prefWidth < intrinsicBackstageWidth || prefHeight < intrinsicBackstageHeight) {
			(new FitException("ERROR: Cannot create CameraPane, as intrinsic backstage is smaller than pane.")).quitProgram();
		}
		setPrefWidth(prefWidth);
		setPrefHeight(prefHeight);
		this.intrinsicBackstageWidth = new SimpleObjectProperty<>(intrinsicBackstageWidth);
		this.intrinsicBackstageHeight = new SimpleObjectProperty<>(intrinsicBackstageHeight);
		initListeners();
		addElement(cNodes);
	}

	private void initListeners() {
		scrollSensitivity.addListener((obs, oldV, newV) -> {
			if (newV < minimumSensitivity) {
				scrollSensitivity.set(minimumSensitivity);
			}
		});
	}

	public final void addElement(CameraNode... cNodes) {
		cameraElements.addAll(cameraElements);
		getChildren().addAll(CameraNode.extractNodes(cNodes));
	}

	private void callibrateDisplay() {
		cameraElements.forEach(e -> e.callibrateDisplay(shiftX, shiftY, zoom));
	}

	public void pan(double dx, double dy) {
		shiftX += dx;
		shiftY += dy;
		fitBackstage();
	}

	public void zoom(double dScroll) {
		double newZoom = zoom * Math.pow(scrollSensitivity.get(), dScroll);
		zoom = newZoom < maxZoom ? newZoom : maxZoom;
		fitBackstage();
	}

	private void fitBackstage() {
		try {
			fitPanning();
		} catch (FitException fep) {
			try {
				fitZooming();
			} catch (FitException fez) {
				fez.quitProgram();
			}
		}
		callibrateDisplay();
	}

	private void fitPanning() throws FitException {

	}

	private void fitZooming() throws FitException {

	}

	private static class FitException extends Exception {

		private final String message;

		FitException(String message) {
			super(message);
			this.message = message;
		}

		void quitProgram() {
			System.err.println(message + "\n");
			printStackTrace();
			System.exit(2);
		}
	}

}
