package camera;

import lombok.Getter;
import lombok.Setter;

public class MouseCameraPane extends CameraPane {

	@Setter
	private boolean mouseAvailable = true;
	@Getter
	private boolean lockedMouse = false;

	private double prevX;
	private double prevY;

	public MouseCameraPane(double intrinsicBackstageWidth, double intrinsicBackstageHeight, double prefWidth, double prefHeight, CameraNode... cNodes) {
		super(intrinsicBackstageWidth, intrinsicBackstageHeight, prefWidth, prefHeight, cNodes);
		initMouseListeners();
	}

	private void initMouseListeners() {
		setOnMousePressed(mouse -> {
			if (!mouseAvailable) {
				return;
			}
			lockedMouse = true;
			prevX = mouse.getX();
			prevY = mouse.getY();
		});
		setOnMouseDragged(mouse -> {
			if (!mouseAvailable) {
				return;
			}
			super.pan(mouse.getX() - prevX, mouse.getY() - prevY);
			prevX = mouse.getX();
			prevY = mouse.getY();
		});
		setOnMouseReleased(mouse -> {
			if (!mouseAvailable) {
				return;
			}
			lockedMouse = false;
			super.pan(mouse.getX() - prevX, mouse.getY() - prevY);
		});
		setOnScroll(scroll -> {
			if (!mouseAvailable) {
				return;
			}
			super.zoom(scroll.getDeltaY());
		});
	}

}
