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
	@Setter
	private double maxZoom = 4;

	@Getter
	private final SimpleObjectProperty<Double> scrollSensitivity = new SimpleObjectProperty<>(1.00696);
	private static final double minimumSensitivity = 1.000001;

	private final double intrinsicBackstageWidth;
	private final double intrinsicBackstageHeight;

	protected final ArrayList<CameraNode> cameraElements = new ArrayList<>();

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
			toCenterX = getPrefWidth() / 2;
			try {
				fitPanning();
			} catch (FitException fe) {
				fe.quitProgram();
			}
		});
		prefHeightProperty().addListener(l -> {
			validatePrefSize();
			toCenterY = getPrefHeight() / 2;
			try {
				fitPanning();
			} catch (FitException fe) {
				fe.quitProgram();
			}
		});
	}

	private void validatePrefSize() {
		if (getPrefWidth() > intrinsicBackstageWidth || getPrefHeight() > intrinsicBackstageHeight) {
			(new FitException("Cannot create CameraPane, as intrinsic backstage is smaller than pane.\n-->"
					+ " iW = " + intrinsicBackstageWidth + "."
					+ " iH = " + intrinsicBackstageHeight + "."
					+ " pW = " + getPrefWidth() + "."
					+ " pH = " + getPrefHeight() + ".")).quitProgram();
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
		try {
			fitPanning();
		} catch (FitException fe) {
			fe.quitProgram();
		}
	}

	public void zoom(double dScroll) {
		try {
			zoom *= Math.pow(scrollSensitivity.get(), dScroll);
			boolean errZ = false;
			if (zoom > maxZoom) {
				errZ = true;
				zoom = maxZoom;
			}
			if (zoom < minZoom()) {
				if (errZ) {
					throw new FitException("Could not set zoom");
				}
				zoom = minZoom();
			}
			fitPanning();
		} catch (FitException fe) {
			fe.quitProgram();
		}
	}

	private void fitPanning() throws FitException {
		boolean errX = false, errY = false;
		if (shiftX() > 0) {
			errX = true;
			toCenterX = getPrefWidth() / (2 * zoom);
		}
		if (zoom * intrinsicBackstageWidth + shiftX() < getPrefWidth()) {
			if (errX) {
				throw new FitException("Could not fit panning (horizontally).");
			}
			toCenterX = intrinsicBackstageWidth - getPrefWidth() / (2 * zoom);
		}
		if (shiftY() > 0) {
			errY = true;
			toCenterY = getPrefHeight() / (2 * zoom);
		}
		if (zoom * intrinsicBackstageHeight + shiftY() < getPrefHeight()) {
			if (errY) {
				throw new FitException("Could not fit panning (vertically).");
			}
			toCenterY = intrinsicBackstageHeight - getPrefHeight() / (2 * zoom);
		}
		callibrateDisplay();
	}

	private double shiftX() {
		return getPrefWidth() / 2 - zoom * toCenterX;
	}

	private double shiftY() {
		return getPrefHeight() / 2 - zoom * toCenterY;
	}

	private double minZoom() {
		double minZW = getPrefWidth() / intrinsicBackstageWidth;
		double minZH = getPrefHeight() / intrinsicBackstageHeight;
		return minZW > minZH ? minZW : minZH;
	}

	private static class FitException extends Exception {

		FitException(String message) {
			super(message);
		}

		void quitProgram() {
			printStackTrace();
			System.exit(2);
		}
	}

}
