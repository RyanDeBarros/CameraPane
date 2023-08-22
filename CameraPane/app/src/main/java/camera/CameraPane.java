package camera;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class CameraPane extends Pane {

	private double toCenterX;
	private double toCenterY;
	private double zoom = 1;

	@Getter
	private final SimpleObjectProperty<Double> scrollSensitivity = new SimpleObjectProperty<>(1.00696);
	private static final double minimumSensitivity = 1.000001;

	private final double intrinsicBackstageWidth;
	private final double intrinsicBackstageHeight;

	private final ArrayList<CameraNode> cameraElements = new ArrayList<>();

	public CameraPane(double intrinsicBackstageWidth, double intrinsicBackstageHeight,
			double prefWidth, double prefHeight, double toCenterX, double toCenterY, CameraNode... cNodes) {
		super();
		this.intrinsicBackstageWidth = intrinsicBackstageWidth;
		this.intrinsicBackstageHeight = intrinsicBackstageHeight;
		setPrefWidth(prefWidth);
		setPrefHeight(prefHeight);
		this.toCenterX = toCenterX;
		this.toCenterY = toCenterY;
		initListeners();
		addElement(cNodes);
	}

	private void initListeners() {
		scrollSensitivity.addListener((obs, oldV, newV) -> {
			if (newV < minimumSensitivity) {
				scrollSensitivity.set(minimumSensitivity);
			}
		});
		prefWidthProperty().addListener(l -> {
			validatePrefSize();
			callibrateDisplay();
		});
		prefHeightProperty().addListener(l -> {
			validatePrefSize();
			callibrateDisplay();
		});
	}

	private void validatePrefSize() {
		if (getPrefWidth() < intrinsicBackstageWidth || getPrefHeight() < intrinsicBackstageHeight) {
			(new FitException("Cannot create CameraPane, as intrinsic backstage is smaller than pane.\n-->"
					+ " iW = " + intrinsicBackstageWidth
					+ ". iH = " + intrinsicBackstageHeight
					+ ". pW = " + getPrefWidth()
					+ ". pH = " + getPrefHeight() + ".")).quitProgram();
		}
	}

	public final void addElement(CameraNode... cNodes) {
		getChildren().addAll(CameraNode.extractNodes(cNodes));
		cameraElements.addAll(Arrays.asList(cNodes));
		callibrateDisplay();
	}

	private void callibrateDisplay() {
		cameraElements.forEach(e -> e.callibrateDisplay(zoom, shiftX(), shiftY()));
	}

	public void pan(double dx, double dy) {
		toCenterX -= dx / zoom;
		toCenterY -= dy / zoom;
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
				fez.quitProgram();
			}
		}
		callibrateDisplay();
	}

	private void fitPanning() throws FitException {
		boolean errX = false, errY = false;
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
			printStackTrace();
			System.exit(2);
		}
	}

	private double shiftX() {
		return getPrefWidth() / 2 - zoom * toCenterX;
	}

	private double shiftY() {
		return getPrefHeight() / 2 - zoom * toCenterY;
	}

}
