package camera;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import lombok.Getter;

public class CameraNode {

	private final Node node;

	@Getter
	private final SimpleObjectProperty<Double> intrinsicCenterX = new SimpleObjectProperty<>(0d);
	@Getter
	private final SimpleObjectProperty<Double> intrinsicCenterY = new SimpleObjectProperty<>(0d);
	@Getter
	private final SimpleObjectProperty<Double> intrinsicScaleX = new SimpleObjectProperty<>(0d);
	@Getter
	private final SimpleObjectProperty<Double> intrinsicScaleY = new SimpleObjectProperty<>(0d);

	private final double positionReferenceX;
	private final double positionReferenceY;

	/**
	 * A wrapping class for a Node that is compatible with CameraPane.
	 * <br>
	 * <br>
	 *
	 * NOTE: When translating or scaling the Node, do NOT use the Node's translateX, translateY,
	 * scaleX, or scaleY property. Instead, use the CameraNode's intrinsicCenterX, intrinsicCenterY,
	 * intrinsicScaleX, and intrinsicScaleY.
	 *
	 * @param node A Node
	 * @param positionReferenceX Where the x position of the Node is determined. A value of 0
	 * represents the left edge of a node's layout bounds, 1 represents the right edge, 0.5
	 * represents the center, etc.
	 * @param positionReferenceY Where the y position of the Node is determined. A value of 0
	 * represents the top edge of a node's layout bounds, 1 represents the bottom edge, 0.5
	 * represents the center, etc.
	 */
	public CameraNode(Node node, double positionReferenceX, double positionReferenceY) {
		this.positionReferenceX = positionReferenceX;
		this.positionReferenceY = positionReferenceY;
		this.node = node;
		initPropertyListeners();
	}

	private void initPropertyListeners() {
		// TODO update display when intrinsic properties are changed.
	}

	public static Node[] extractNodes(CameraNode... cNodes) {
		Node nodes[] = new Node[cNodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = cNodes[i].node;
		}
		return nodes;
	}

	public void setNodeCenter(double cx, double cy) {
		double offsetX = node.getLayoutBounds().getWidth() * (positionReferenceX - 0.5);
		node.setLayoutX(cx + offsetX);
		double offsetY = node.getLayoutBounds().getHeight() * (positionReferenceY - 0.5);
		node.setLayoutY(cy + offsetY);
	}

	public void callibrateDisplay(double zoom, double shiftX, double shiftY) {
		node.setTranslateX(zoom * intrinsicCenterX.get() + shiftX);
		node.setTranslateY(zoom * intrinsicCenterY.get() + shiftY);
	}

}
