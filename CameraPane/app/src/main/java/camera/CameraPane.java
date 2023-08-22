package camera;

import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class CameraPane extends Pane {

	private double shiftX;
	private double shiftY;
	private final SimpleObjectProperty<Double> zoom = new SimpleObjectProperty<>(1d);
	private final SimpleObjectProperty<Double> toCenterX = new SimpleObjectProperty<>(0d);
	private final SimpleObjectProperty<Double> toCenterY = new SimpleObjectProperty<>(0d);
	@Setter
	private double maxZoom;

	@Getter
	private final SimpleObjectProperty<Double> scrollSensitivity = new SimpleObjectProperty<>(1.00696);
	private static final double minimumSensitivity = 1.000001;

	private final double intrinsicBackstageWidth;
	private final double intrinsicBackstageHeight;

	private final ArrayList<CameraNode> cameraElements = new ArrayList<>();

	public CameraPane(double intrinsicBackstageWidth, double intrinsicBackstageHeight, double prefWidth, double prefHeight, CameraNode... cNodes) {
		super();
		if (prefWidth < intrinsicBackstageWidth || prefHeight < intrinsicBackstageHeight) {
			(new FitException("ERROR: Cannot create CameraPane, as intrinsic backstage is smaller than pane.")).quitProgram();
		}
		setPrefWidth(prefWidth);
		setPrefHeight(prefHeight);
		this.intrinsicBackstageWidth = intrinsicBackstageWidth;
		this.intrinsicBackstageHeight = intrinsicBackstageHeight;
		setInitialShift();
		initListeners();
		addElement(cNodes);
	}

	private void setInitialShift() {
		shiftX = getPrefWidth() / 2;
		shiftY = getPrefHeight() / 2;
	}

	private void initListeners() {
		scrollSensitivity.addListener((obs, oldV, newV) -> {
			if (newV < minimumSensitivity) {
				scrollSensitivity.set(minimumSensitivity);
			}
		});
		zoom.addListener((obs, oldV, newV) -> {
			// Adjust shifts so that it appears that the center of pane is point of origin of zoom.
			shiftX += toCenterX.get() * (oldV - newV);
			shiftY += toCenterY.get() * (oldV - newV);
		});
		toCenterX.addListener((obs, oldV, newV) -> {
			shiftX += zoom.get() * (oldV - newV);
		});
		toCenterY.addListener((obs, oldV, newV) -> {
			shiftY += zoom.get() * (oldV - newV);
		});
	}

	public final void addElement(CameraNode... cNodes) {
		cameraElements.addAll(cameraElements);
		getChildren().addAll(CameraNode.extractNodes(cNodes));
	}

	private void callibrateDisplay() {
		cameraElements.forEach(e -> e.callibrateDisplay(shiftX, shiftY, zoom.get()));
	}

	public void pan(double dx, double dy) {
		toCenterX.set(toCenterX.get() + dx / zoom.get());
		toCenterY.set(toCenterY.get() + dy / zoom.get());
		fitBackstage();
	}

	public void zoom(double dScroll) {
		double newZoom = zoom.get() * Math.pow(scrollSensitivity.get(), dScroll);
		zoom.set(newZoom < maxZoom ? newZoom : maxZoom);
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
