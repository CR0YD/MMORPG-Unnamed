package testing;

import javafx.scene.shape.Rectangle;

public class ObjectBox extends Rectangle {

	public final double OFFSET_X, OFFSET_Y;

	public ObjectBox(double x, double y, double width, double height) {
		super(0, 0, width, height);
		setTranslateX(x);
		setTranslateY(y);
		OFFSET_X = x;
		OFFSET_Y = y;
		return;
	}
	
	public void move(double x, double y) {
		setTranslateX(getTranslateX() + x);
		setTranslateY(getTranslateY() + y);
	}
	public void moveTo(double x, double y) {
		setTranslateX(x + OFFSET_X);
		setTranslateY(y + OFFSET_Y);
	}
	
}
